package assure.dao;

import assure.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(InventoryPojo inventoryPojo){
        entityManager.persist(inventoryPojo);
    }

    @Transactional
    public InventoryPojo select(Long globalSkuId){
        String select="select p from InventoryPojo p where globalSkuId=:globalSkuId";
        TypedQuery<InventoryPojo> query=getQuery(select,InventoryPojo.class);
        query.setParameter("globalSkuId",globalSkuId);
        List<InventoryPojo> inventoryPojoList= query.getResultList();
        if(inventoryPojoList.size()>0)
            return inventoryPojoList.get(0);
        return null;
    }
    @Transactional
    public void update(Long globalSkuId, InventoryPojo inventoryPojo){
        InventoryPojo inventoryPojo1=select(globalSkuId);
        inventoryPojo1.setAvailableQuantity(inventoryPojo.getAvailableQuantity());
        inventoryPojo1.setFulfilledQuantity(inventoryPojo.getFulfilledQuantity());
        inventoryPojo1.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity());
        entityManager.merge(inventoryPojo1);
    }
}
