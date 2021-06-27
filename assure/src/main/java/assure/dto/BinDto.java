package assure.dto;

import assure.pojo.BinPojo;
import assure.pojo.BinSkuPojo;
import assure.pojo.ProductPojo;
import assure.service.ApiException;
import assure.service.BinService;
import assure.service.ProductService;
import assure.util.ConversionUtil;
import common.model.BinSkuData;
import common.model.BinSkuForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BinDto {
    @Autowired
    private BinService binService;

    @Autowired
    private ProductService productService;

    @Transactional(rollbackFor = ApiException.class)
    public void add(){
        BinPojo binPojo=new BinPojo();
        binService.add(binPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(BinSkuForm binSkuForm) throws ApiException {
        check(binSkuForm);
        ProductPojo productPojo=productService.getFromClientSkuId(binSkuForm.getClientSkuId());
        binService.add(ConversionUtil.convert(productPojo,binSkuForm));
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(List<BinSkuForm> binSkuFormList) throws ApiException {
        List<BinSkuPojo> binSkuPojoList=new ArrayList<>();
        for (BinSkuForm binSkuForm:binSkuFormList){
            check(binSkuForm);
            ProductPojo productPojo=productService.getFromClientSkuId(binSkuForm.getClientSkuId());
            binSkuPojoList.add(ConversionUtil.convert(productPojo,binSkuForm));
        }
        binService.add(binSkuPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<BinSkuData> get() throws ApiException {
        List<BinSkuPojo> binSkuPojoList= binService.getAll();
        Map<BinSkuPojo,ProductPojo> binSkuPojoProductPojoMap=binService.getMap(binSkuPojoList);
        return ConversionUtil.convert(binSkuPojoProductPojoMap);
    }
    private void check(BinSkuForm binSkuForm) throws ApiException {
        if(binSkuForm.getBinId()==null){
            throw new ApiException("Bin id cannot be empty");
        }
        if(binSkuForm.getClientSkuId()==null){
            throw new ApiException("Client sku id cannot be empty");
        }
        if(binSkuForm.getQuantity()==null){
            throw new ApiException("Quantity cannot be empty");
        }
        if(binSkuForm.getQuantity()<=0){
            throw new ApiException("Quantity should be positive");
        }
    }
}
