package assure.dto;

import assure.pojo.ProductPojo;
import assure.pojo.UserPojo;
import assure.service.ApiException;
import assure.service.ProductService;
import assure.service.UserService;
import assure.util.ConversionUtil;
import assure.util.StringUtil;
import common.model.ProductData;
import common.model.ProductForm;
import common.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDto {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @Transactional(rollbackFor = ApiException.class)
    public void add(String clientName, ProductForm productForm) throws ApiException {
        normalize(productForm);
        check(productForm);
        UserPojo userPojo=userService.get(clientName, Type.CLIENT);
        productService.add(ConversionUtil.convert(userPojo.getId(), productForm));
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(String clientName, List<ProductForm> productFormList) throws ApiException {
        List<ProductPojo> productPojoList=new ArrayList<>();
        for(ProductForm productForm:productFormList)
        {
            normalize(productForm);
            check(productForm);
            UserPojo userPojo=userService.get(clientName, Type.CLIENT);
            productService.add(ConversionUtil.convert(userPojo.getId(), productForm));
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
        normalize(productForm);
        check(productForm);
        ProductPojo productPojo= productService.get(id);
        productService.update(id,ConversionUtil.convert(productPojo.getClientId(),productForm));
    }

    public void check(ProductForm productForm) throws ApiException {

        if(StringUtil.isEmpty(productForm.getBrandId())){
            throw new ApiException("Brand Id cannot be empty");
        }
        if(StringUtil.isEmpty(productForm.getDescription())){
            throw new ApiException("Description cannot be empty");
        }
        if(StringUtil.isEmpty(productForm.getClientSkuId())){
            throw new ApiException("Client Sku Id cannot be empty");
        }
        if(productForm.getMrp()==null){
            throw new ApiException("Mrp cannot be empty");
        }
        if(productForm.getMrp()<=0){
            throw new ApiException("Mrp should be positive");
        }
        productForm.setMrp(Math.round(productForm.getMrp()*100.0)/100.0);
        if(StringUtil.isEmpty(productForm.getName())){
            throw new ApiException("Name cannot be empty");
        }
    }

    public  void normalize(ProductForm productForm){
        productForm.setBrandId(StringUtil.trim(productForm.getBrandId()));
        productForm.setDescription(StringUtil.trim(productForm.getDescription()));
        productForm.setClientSkuId(StringUtil.trim(productForm.getClientSkuId()));
        productForm.setName(StringUtil.trim(productForm.getName()));
    }
}
