package assure.service;

import assure.dao.OrderDao;
import assure.pojo.InventoryPojo;
import assure.pojo.OrderItemPojo;
import assure.pojo.OrderPojo;
import assure.pojo.ProductPojo;
import common.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BinService binService;

    @Transactional(rollbackFor = ApiException.class)
    public void add(OrderPojo orderPojo,OrderItemPojo orderItemPojo) throws ApiException {
        check(orderPojo.getChannelOrderId());
        Long orderId=orderDao.insert(orderPojo);
        orderItemPojo.setOrderId(orderId);
        orderDao.insert(orderItemPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(OrderPojo orderPojo,List<OrderItemPojo> orderItemPojoList) throws ApiException {
        check(orderPojo.getChannelOrderId());
        Long orderId=orderDao.insert(orderPojo);
        for (OrderItemPojo orderItemPojo:orderItemPojoList){
            orderItemPojo.setOrderId(orderId);
            orderDao.insert(orderItemPojo);
        }
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderPojo get(Long id) throws ApiException {
        return check(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<OrderPojo> get(){
        return orderDao.get();
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<OrderItemPojo> getOrderItems(Long orderId) throws ApiException {
        OrderPojo orderPojo=check(orderId);
        return orderDao.getOrderItemsByOrderId(orderId);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void allocate(Long id) throws ApiException {
        OrderPojo orderPojo=check(id);
        checkAllocated(orderPojo);
        List<OrderItemPojo> orderItemPojoList=getOrderItems(id);
        long count = 0L;
        for (OrderItemPojo orderItemPojo:orderItemPojoList){
            InventoryPojo inventoryPojo=inventoryService.get(orderItemPojo.getGlobalSkuId());
            Long allocate=Math.min(inventoryPojo.getAvailableQuantity(),orderItemPojo.getOrderedQuantity()-orderItemPojo.getAllocatedQuantity());
                inventoryPojo.setAvailableQuantity(inventoryPojo.getAvailableQuantity()-allocate);
                inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity()+allocate);
                inventoryPojo.setFulfilledQuantity(inventoryPojo.getFulfilledQuantity());
                inventoryService.allocate(orderItemPojo.getGlobalSkuId(), inventoryPojo);
                binService.update(orderItemPojo.getGlobalSkuId(),allocate);
                orderItemPojo.setAllocatedQuantity(orderItemPojo.getAllocatedQuantity()+allocate);
                orderItemPojo.setFulfilledQuantity(0L);
                update(orderItemPojo.getId(), orderItemPojo);
                if(allocate.equals(orderItemPojo.getOrderedQuantity()))
                    count++;
        }
       if(count==orderItemPojoList.size())
       {
           orderPojo.setStatus(Status.ALLOCATED);
           orderDao.updateOrder(id,orderPojo);
       }
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(Long id,OrderItemPojo orderItemPojo) throws ApiException {
        OrderItemPojo orderItemPojo1=checkOrderItem(id);
        orderItemPojo1.setFulfilledQuantity(orderItemPojo.getFulfilledQuantity());
        orderItemPojo1.setAllocatedQuantity(orderItemPojo.getAllocatedQuantity());
        orderDao.update(id, orderItemPojo1);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void fulfill(Long id,OrderPojo orderPojo) throws ApiException {
        OrderPojo orderPojo1=check(id);
        List<OrderItemPojo> orderItemPojoList=orderDao.getOrderItemsByOrderId(id);
        for (OrderItemPojo orderItemPojo:orderItemPojoList){

            orderItemPojo.setFulfilledQuantity(orderItemPojo.getOrderedQuantity());
            orderItemPojo.setAllocatedQuantity(orderItemPojo.getAllocatedQuantity()-orderItemPojo.getOrderedQuantity());
            update(orderItemPojo.getId(), orderItemPojo);
            InventoryPojo inventoryPojo=inventoryService.get(orderItemPojo.getGlobalSkuId());

            inventoryPojo.setFulfilledQuantity(inventoryPojo.getFulfilledQuantity()+orderItemPojo.getOrderedQuantity());
            inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity()-orderItemPojo.getOrderedQuantity());
            inventoryService.fulfill(orderItemPojo.getGlobalSkuId(), inventoryPojo);
        }
        orderPojo1.setStatus(orderPojo.getStatus());
        orderDao.updateOrder(id, orderPojo1);
    }

    private OrderPojo check(Long id) throws ApiException {
        OrderPojo orderPojo= orderDao.get(id);
        if(orderPojo==null)
            throw new ApiException("Order with given id does not exist: "+id);
        return orderPojo;
    }

    private void check(String channelOrderId) throws ApiException {
        OrderPojo orderPojo=orderDao.getFromChannelOrderId(channelOrderId);
        if(orderPojo!=null)
            throw new ApiException("Order with given channel order id already exists: "+channelOrderId);
    }

    private OrderItemPojo checkOrderItem(Long id) throws ApiException {
        OrderItemPojo orderItemPojo=orderDao.getOrderItem(id);
        if(orderItemPojo==null)
            throw new ApiException("Order Item with given id does not exist: "+id);
        return orderItemPojo;
    }

    private void checkAllocated(OrderPojo orderPojo) throws ApiException {
        if(orderPojo.getStatus().equals(Status.ALLOCATED)){
            throw new ApiException("Order already allocated");
        }
    }

    public Map<OrderItemPojo, ProductPojo> getProductPojoFromOrderItem(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        Map<OrderItemPojo, ProductPojo> orderItemPojoProductPojoMap=new HashMap<>();
        for (OrderItemPojo orderItemPojo:orderItemPojoList){
            ProductPojo productPojo=productService.get(orderItemPojo.getGlobalSkuId());
            orderItemPojoProductPojoMap.put(orderItemPojo,productPojo);
        }
        return orderItemPojoProductPojoMap;
    }
}
