package assure.dao;

import assure.pojo.OrderItemPojo;
import assure.pojo.OrderPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Long insert(OrderPojo orderPojo)
    {
        entityManager.persist(orderPojo);
        entityManager.flush();
        return orderPojo.getId();
    }

    @Transactional
    public void insert(OrderItemPojo orderItemPojo){
        entityManager.persist(orderItemPojo);
    }

    @Transactional
    public OrderPojo get(Long id){
        return entityManager.find(OrderPojo.class,id);
    }

    @Transactional
    public List<OrderPojo> get(){
        String select="select p from OrderPojo p";
        TypedQuery<OrderPojo> query=getQuery(select,OrderPojo.class);
        if(query == null){
            return new ArrayList<>();
        }
        return query.getResultList();
    }

    @Transactional
    public OrderItemPojo getOrderItem(Long id){
        return entityManager.find(OrderItemPojo.class,id);
    }

    @Transactional
    public void update(Long id,OrderItemPojo orderItemPojo){
        OrderItemPojo orderItemPojo1=entityManager.find(OrderItemPojo.class,id);
        orderItemPojo1.setAllocatedQuantity(orderItemPojo.getAllocatedQuantity());
        orderItemPojo1.setFulfilledQuantity(orderItemPojo.getFulfilledQuantity());
        entityManager.merge(orderItemPojo1);
    }

    @Transactional
    public void updateOrder(Long id,OrderPojo orderPojo){
        OrderPojo orderPojo1=entityManager.find(OrderPojo.class,id);
        orderPojo1.setStatus(orderPojo.getStatus());
        entityManager.merge(orderPojo1);
    }

    @Transactional
    public List<OrderItemPojo> getOrderItemsByOrderId(Long orderId){
        String select="select p from OrderItemPojo p where orderId=:orderId";
        TypedQuery<OrderItemPojo> query=getQuery(select,OrderItemPojo.class);
        query.setParameter("orderId",orderId);
        return query.getResultList();
    }
    @Transactional
    public OrderPojo getFromChannelOrderId(String channelOrderId){
        String select="select p from OrderPojo p where channelOrderId=:channelOrderId";
        TypedQuery<OrderPojo> query=getQuery(select,OrderPojo.class);
        query.setParameter("channelOrderId",channelOrderId);
        List<OrderPojo> orderPojoList=query.getResultList();
        if(orderPojoList.size()>0)
            return orderPojoList.get(0);
        else
            return null;
    }
}
