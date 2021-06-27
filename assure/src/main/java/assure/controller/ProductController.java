package assure.controller;

import assure.dto.ProductDto;
import common.model.ProductData;
import common.model.ProductForm;
import assure.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api
public class ProductController extends ExceptionHandler {
    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "Adds a product")
    @RequestMapping(path = "/api/product/user/{clientId}", method = RequestMethod.POST)
    public void add(@PathVariable Long clientId, @RequestBody ProductForm productForm) throws ApiException {
        productDto.add(clientId,productForm);
    }

    @ApiOperation(value = "Adds product list")
    @RequestMapping(path = "/api/product/list/{clientId}", method = RequestMethod.POST)
    public void add(@PathVariable Long clientId,@RequestBody List<ProductForm> productFormList) throws ApiException {
        productDto.add(clientId, productFormList);
    }

    @ApiOperation(value = "Get a product by Id")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Long id) throws ApiException {
        return productDto.get(id);
    }

    @ApiOperation(value = "Get list of all products")
    @RequestMapping(path = "/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll(){
        return productDto.getAll();
    }

    @ApiOperation(value = "Updates a product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Long id, @RequestBody ProductForm productForm) throws ApiException {
        productDto.update(id,productForm);
    }
}
