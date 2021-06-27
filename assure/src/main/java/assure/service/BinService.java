package assure.service;

import assure.dao.BinDao;
import assure.pojo.BinPojo;
import assure.pojo.BinSkuPojo;
import assure.pojo.InventoryPojo;
import assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BinService {
    @Autowired
    private BinDao binDao;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;

    //Add bin pojo
    @Transactional(rollbackFor = ApiException.class)
    public void add(BinPojo binPojo){
        binDao.insert(binPojo);
    }

    //Add bin sku
    @Transactional(rollbackFor = ApiException.class)
    public void add(BinSkuPojo binSkuPojo) throws ApiException {
        checkBin(binSkuPojo.getBinId());
        if(checkAlreadyExist(binSkuPojo)==null){
            binDao.insert(binSkuPojo);
            InventoryPojo inventoryPojo=new InventoryPojo();
            inventoryPojo.setAvailableQuantity(binSkuPojo.getQuantity());
            inventoryService.update(binSkuPojo.getGlobalSkuId(),inventoryPojo);
        }

        else
        {
            update(checkAlreadyExist(binSkuPojo).getId(),binSkuPojo);
            InventoryPojo inventoryPojo=new InventoryPojo();
            inventoryPojo.setAvailableQuantity(binSkuPojo.getQuantity());
            inventoryService.update(binSkuPojo.getGlobalSkuId(),inventoryPojo);
        }

    }

    //Add list of bin sku
    @Transactional(rollbackFor = ApiException.class)
    public void add(List<BinSkuPojo> binSkuPojoList) throws ApiException {
        for (BinSkuPojo binSkuPojo:binSkuPojoList){
            checkBin(binSkuPojo.getBinId());
        }
        for (BinSkuPojo binSkuPojo:binSkuPojoList){
            if(checkAlreadyExist(binSkuPojo)==null)
                binDao.insert(binSkuPojo);
            else
                update(checkAlreadyExist(binSkuPojo).getId(),binSkuPojo);
        }
    }
    //Update bin sku pojo
    @Transactional(rollbackFor = ApiException.class)
    public void update(Long id,BinSkuPojo binSkuPojo) throws ApiException {
        BinSkuPojo binSkuPojo1=check(id);
        binSkuPojo1.setQuantity(binSkuPojo1.getQuantity()+ binSkuPojo.getQuantity());
        binDao.update(id,binSkuPojo1);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(Long globalSkuId,Long allocatedQuantity) throws ApiException {
        List<BinSkuPojo> binSkuPojoList=binDao.getFromGlobalSkuId(globalSkuId);
        for (BinSkuPojo binSkuPojo:binSkuPojoList){
            if(allocatedQuantity>0)
            {
                Long allocate=Math.min(binSkuPojo.getQuantity(),allocatedQuantity);
                allocatedQuantity = allocatedQuantity - allocate;
                BinSkuPojo binSkuPojo1=check(binSkuPojo.getId());
                binSkuPojo1.setQuantity(binSkuPojo1.getQuantity()-allocate);
                binDao.update(binSkuPojo.getId(),binSkuPojo1);
            }
            else
                break;
        }
    }
    @Transactional(rollbackFor = ApiException.class)
    public List<BinSkuPojo> getAll(){
        return binDao.getALl();
    }


    public Map<BinSkuPojo, ProductPojo> getMap(List<BinSkuPojo> binSkuPojoList) throws ApiException {
        Map<BinSkuPojo,ProductPojo> binSkuPojoProductPojoMap=new HashMap<>();
        for (BinSkuPojo binSkuPojo:binSkuPojoList){
            ProductPojo productPojo=productService.get(binSkuPojo.getGlobalSkuId());
            binSkuPojoProductPojoMap.put(binSkuPojo,productPojo);
        }
        return binSkuPojoProductPojoMap;
    }
    //Check whether binSku with given binId and globalSkuId already exists
    public BinSkuPojo checkAlreadyExist(BinSkuPojo binSkuPojo){
        return binDao.getFromBinIdGlobalSkuId(binSkuPojo.getBinId(),binSkuPojo.getGlobalSkuId());
    }

    //Check whether binSku with given id exists or not
    public BinSkuPojo check(Long id) throws ApiException {
        BinSkuPojo binSkuPojo=binDao.get(id);
        if (binSkuPojo==null){
            throw new ApiException("Bin sku with given id does not exist: "+id);
        }
        return binSkuPojo;
    }

    //Check whether bin with given binId exists or not
    public void checkBin(Long binId) throws ApiException {
        BinPojo binPojo=binDao.getBin(binId);
        if (binPojo==null)
            throw new ApiException("Bin with given binId does not exist: "+binId);
    }
}
