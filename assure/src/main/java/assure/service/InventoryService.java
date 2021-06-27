package assure.service;

import assure.dao.InventoryDao;
import assure.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private BinService binService;

    @Transactional(rollbackFor = ApiException.class)
    public void add(InventoryPojo inventoryPojo){
        inventoryPojo.setAllocatedQuantity(0L);
        inventoryPojo.setAvailableQuantity(0L);
        inventoryPojo.setFulfilledQuantity(0L);
        inventoryDao.insert(inventoryPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(Long globalSkuId,InventoryPojo inventoryPojo) throws ApiException {
        check(globalSkuId);
        InventoryPojo inventoryPojo1=inventoryDao.select(globalSkuId);
        inventoryPojo1.setAvailableQuantity(inventoryPojo1.getAvailableQuantity()+ inventoryPojo.getAvailableQuantity());
        inventoryDao.update(globalSkuId,inventoryPojo1);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void allocate(Long globalSkuId,InventoryPojo inventoryPojo) throws ApiException {
        check(globalSkuId);
        InventoryPojo inventoryPojo1=inventoryDao.select(globalSkuId);
        inventoryPojo1.setAvailableQuantity(inventoryPojo.getAvailableQuantity());
        inventoryPojo1.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity());
        inventoryPojo1.setFulfilledQuantity(inventoryPojo.getFulfilledQuantity());
        inventoryDao.update(globalSkuId,inventoryPojo1);
        binService.update(globalSkuId, inventoryPojo.getAllocatedQuantity());
    }

    @Transactional(rollbackFor = ApiException.class)
    public InventoryPojo get(Long globalSkuId) throws ApiException {
        return check(globalSkuId);
    }

    public InventoryPojo check(Long globalSkuId) throws ApiException {
        InventoryPojo inventoryPojo=inventoryDao.select(globalSkuId);
        if(inventoryPojo==null)
            throw new ApiException("Inventory with given globalSkuId does not exist: "+ globalSkuId);
        return inventoryPojo;
    }
}
