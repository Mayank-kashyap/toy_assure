package assure.dto;

import common.model.ProductData;
import common.model.ProductForm;
import assure.pojo.ProductPojo;
import assure.service.ApiException;
import assure.service.ProductService;
import assure.util.ConversionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDto {
    @Autowired
    private ProductService productService;

    @Transactional(rollbackFor = ApiException.class)
    public void add(Long clientId, ProductForm productForm) throws ApiException {
        check(productForm);
        productService.add(ConversionUtil.convert(clientId, productForm));
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(Long clientId, List<ProductForm> productFormList) throws ApiException {
        List<ProductPojo> productPojoList=new ArrayList<>();
        for(ProductForm productForm:productFormList)
        {
            check(productForm);
            productPojoList.add(ConversionUtil.convert(clientId, productForm));
        }
        productService.add(productPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public ProductData get(Long id) throws ApiException {
        return ConversionUtil.convert(productService.get(id));
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<ProductData> getAll(){
        return ConversionUtil.convertProductList(productService.getAll());
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(Long id, ProductForm productForm) throws ApiException {
        check(productForm);
        ProductPojo productPojo= productService.get(id);
        productService.update(id,ConversionUtil.convert(productPojo.getClientId(),productForm));
    }
    public void check(ProductForm productForm) throws ApiException {
        if(productForm.getBrandId()==null){
            throw new ApiException("Brand Id cannot be empty");
        }
        if(productForm.getDescription()==null){
            throw new ApiException("Description cannot be empty");
        }
        if(productForm.getClientSkuId()==null){
            throw new ApiException("Client Sku Id cannot be empty");
        }
        if(productForm.getMrp()==null){
            throw new ApiException("Mrp cannot be empty");
        }
        if(productForm.getMrp()<=0){
            throw new ApiException("Mrp should be positive");
        }
        if(productForm.getName()==null){
            throw new ApiException("Name cannot be empty");
        }
    }
}
