package assure.dto;

import assure.pojo.*;
import assure.service.*;
import assure.util.ConversionUtil;
import assure.util.StringUtil;
import common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ProductService productService;

    @Transactional(rollbackFor = ApiException.class)
    public void add(String clientName, String channelOrderId, String customerName, OrderItemForm orderItemForm) throws ApiException {
        normalize(orderItemForm);
        check(orderItemForm);
        UserPojo userPojo=userService.get(clientName, Type.CLIENT);
        UserPojo userPojo1=userService.get(customerName,Type.CUSTOMER);
        ChannelPojo channelPojo=channelService.get("INTERNAL");
        ProductPojo productPojo= productService.getFromClientSkuId(orderItemForm.getClientSkuId());
        OrderPojo orderPojo= ConversionUtil.convert(userPojo,userPojo1,channelPojo,channelOrderId);
        OrderItemPojo orderItemPojo=ConversionUtil.convert(orderPojo.getId(),productPojo, orderItemForm);
        orderService.add(orderPojo,orderItemPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(String clientName, String channelOrderId, String customerName, List<OrderItemForm> orderItemFormList) throws ApiException {
        for (OrderItemForm orderItemForm:orderItemFormList){
            normalize(orderItemForm);
            check(orderItemForm);
        }
        UserPojo userPojo=userService.get(clientName, Type.CLIENT);
        UserPojo userPojo1=userService.get(customerName,Type.CUSTOMER);
        ChannelPojo channelPojo=channelService.get("INTERNAL");
        OrderPojo orderPojo= ConversionUtil.convert(userPojo,userPojo1,channelPojo,channelOrderId);
        List<OrderItemPojo> orderItemPojoList=new ArrayList<>();
        for (OrderItemForm orderItemForm:orderItemFormList){
            ProductPojo productPojo= productService.getFromClientSkuId(orderItemForm.getClientSkuId());
            OrderItemPojo orderItemPojo=ConversionUtil.convert(orderPojo.getId(),productPojo, orderItemForm);
            orderItemPojoList.add(orderItemPojo);
        }
        orderService.add(orderPojo,orderItemPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(String channelName,String clientName,String customerName,String channelOrderId,List<OrderItemsForm> orderItemsFormList) throws ApiException {
        for (OrderItemsForm orderItemsForm:orderItemsFormList){
            normalize(orderItemsForm);
            check(orderItemsForm);
        }
        UserPojo userPojo=userService.get(clientName,Type.CLIENT);
        UserPojo userPojo1=userService.get(customerName,Type.CUSTOMER);
        ChannelPojo channelPojo=channelService.get(channelName);
        OrderPojo orderPojo=ConversionUtil.convert(userPojo,userPojo1,channelPojo,channelOrderId);
        List<OrderItemPojo> orderItemPojoList=new ArrayList<>();
        for (OrderItemsForm orderItemsForm:orderItemsFormList){
            ChannelListingPojo channelListingPojo=channelService.getChannelListing(orderItemsForm.getChannelSkuId());
            OrderItemPojo orderItemPojo=ConversionUtil.convert(orderPojo.getId(),channelListingPojo, orderItemsForm);
            orderItemPojoList.add(orderItemPojo);
        }
        orderService.add(orderPojo,orderItemPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderData get(Long id) throws ApiException {
        OrderPojo orderPojo=orderService.get(id);
        UserPojo userPojo=userService.get(orderPojo.getClientId());
        UserPojo userPojo1=userService.get(orderPojo.getCustomerId());
        ChannelPojo channelPojo=channelService.get(orderPojo.getChannelId());
        return ConversionUtil.convert(userPojo,userPojo1,channelPojo,orderPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<OrderData> get() throws ApiException {
        List<OrderPojo> orderPojoList=orderService.get();
        List<OrderData> orderDataList =new ArrayList<>();
        for (OrderPojo orderPojo:orderPojoList){
            OrderData orderData=get(orderPojo.getId());
            orderDataList.add(orderData);
        }
        return orderDataList;
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<OrderItemData> getOrderItems(Long orderId) throws ApiException {
        List<OrderItemPojo> orderItemPojoList=orderService.getOrderItems(orderId);
        Map<OrderItemPojo,ProductPojo> orderItemPojoProductPojoMap=orderService.getProductPojoFromOrderItem(orderItemPojoList);
        return ConversionUtil.convertOrderItemsList(orderItemPojoList,orderItemPojoProductPojoMap);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void allocate(Long id) throws ApiException {
        orderService.allocate(id);
    }

    private void check(OrderItemForm orderItemForm) throws ApiException {
        if(StringUtil.isEmpty(orderItemForm.getClientSkuId()))
            throw new ApiException("Client sku id cannot be empty");
        if(orderItemForm.getOrderedQuantity()==null)
            throw new ApiException("Ordered Quantity cannot be empty");
        if(orderItemForm.getSellingPricePerUnit()==null)
            throw new ApiException("Selling price cannot be empty");
        if(orderItemForm.getOrderedQuantity()<=0)
            throw new ApiException("Ordered quantity should be positive");
        if(orderItemForm.getSellingPricePerUnit()<=0.0)
            throw new ApiException("Selling price should be positive");
    }

    private void check(OrderItemsForm orderItemsForm) throws ApiException {
        if (StringUtil.isEmpty(orderItemsForm.getChannelSkuId()))
            throw new ApiException("Channel skuId cannot be empty");
        if (orderItemsForm.getQuantity()==null)
            throw new ApiException("Channel skuId cannot be empty");
        if (orderItemsForm.getSellingPricePerUnit()==null)
            throw new ApiException("Selling price cannot be empty");
        if (orderItemsForm.getQuantity()<=0)
            throw new ApiException("Quantity should be positive");
        if (orderItemsForm.getSellingPricePerUnit()<=0)
            throw new ApiException("Selling price should be positive");
    }

    public void normalize(OrderItemForm orderItemForm){
        orderItemForm.setClientSkuId(StringUtil.trim(orderItemForm.getClientSkuId()));
    }

    public void normalize(OrderItemsForm orderItemsForm){
        orderItemsForm.setChannelSkuId(StringUtil.trim(orderItemsForm.getChannelSkuId()));
    }
}
