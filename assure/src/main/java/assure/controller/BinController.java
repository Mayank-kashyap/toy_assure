package assure.controller;

import assure.dto.BinDto;
import common.model.BinSkuData;
import common.model.BinSkuForm;
import assure.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api
public class BinController extends ExceptionHandler {
    @Autowired
    private BinDto binDto;

    @ApiOperation("Add a bin")
    @RequestMapping(path = "/api/bin", method = RequestMethod.POST)
    public Long add(){
        return binDto.add();
    }

    @ApiOperation("Add a bin inventory")
    @RequestMapping(path = "/api/binSku",method = RequestMethod.POST)
    public void add(@RequestBody BinSkuForm binSkuForm) throws ApiException {
        binDto.add(binSkuForm);
    }

    @ApiOperation("Get list of inventory")
    @RequestMapping(path = "/api/binSku",method = RequestMethod.GET)
    public List<BinSkuData> getAll() throws ApiException {
        return binDto.get();
    }

    @ApiOperation("Add list of bin inventory")
    @RequestMapping(path = "/api/binSku/list",method = RequestMethod.POST)
    public void add(@RequestBody List<BinSkuForm> binSkuFormList) throws ApiException {
        binDto.add(binSkuFormList);
    }
}
