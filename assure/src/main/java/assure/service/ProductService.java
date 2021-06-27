package assure.service;

import assure.dao.ProductDao;
import assure.dao.UserDao;
import assure.pojo.InventoryPojo;
import assure.pojo.ProductPojo;
import assure.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackFor = ApiException.class)
    public void add(ProductPojo productPojo) throws ApiException {
        checkAlreadyExist(productPojo);
        checkInvalidClient(productPojo);
        productDao.insert(productPojo);
        InventoryPojo inventoryPojo=new InventoryPojo();
        inventoryPojo.setGlobalSkuId(productPojo.getId());
        inventoryService.add(inventoryPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(List<ProductPojo> productPojoList) throws ApiException {
        for (ProductPojo productPojo:productPojoList)
        {
            checkAlreadyExist(productPojo);
            checkInvalidClient(productPojo);
        }
        for (ProductPojo productPojo:productPojoList){
            productDao.insert(productPojo);
            InventoryPojo inventoryPojo=new InventoryPojo();
            inventoryPojo.setGlobalSkuId(productPojo.getId());
            inventoryService.add(inventoryPojo);
        }
    }

    @Transactional(rollbackFor = ApiException.class )
    public ProductPojo get(Long id) throws ApiException {
        return check(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<ProductPojo> getAll(){
        return productDao.selectAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(Long id,ProductPojo productPojo) throws ApiException {
        ProductPojo productPojo1=check(id);
        productPojo1.setName(productPojo.getName());
        productPojo1.setDescription(productPojo.getDescription());
        productPojo1.setMrp(productPojo.getMrp());
        productPojo1.setBrandId(productPojo.getBrandId());
        productDao.update(id,productPojo1);
    }

    @Transactional(rollbackFor = ApiException.class)
    public ProductPojo getFromClientSkuId(String clientSkuId) throws ApiException {
        ProductPojo productPojo=productDao.getFromClientSkuId(clientSkuId);
        if(productPojo==null){
            throw new ApiException("Product with given client sku id does not exist: "+clientSkuId);
        }
        return productPojo;
    }

    public ProductPojo check(Long id) throws ApiException {
        ProductPojo productPojo= productDao.select(id);
        if(productPojo==null)
            throw new ApiException("Product with given id does not exist: "+id);
        return productPojo;
    }
    public void checkAlreadyExist(ProductPojo productPojo) throws ApiException{
        List<ProductPojo> productPojoList = productDao.getFromClientIdClientSkuId(productPojo.getClientId(),productPojo.getClientSkuId());
        if(!productPojoList.isEmpty()){
            throw new ApiException("Product with given clientId and clientSkuId already exists: "+productPojo.getClientId()+" "+ productPojo.getClientSkuId());
        }
    }

    public void checkInvalidClient(ProductPojo productPojo) throws ApiException{
        UserPojo userPojo= userDao.select(productPojo.getClientId());
        if(userPojo==null)
            throw new ApiException("Client with given id does not exist: "+productPojo.getClientId());
    }
}
