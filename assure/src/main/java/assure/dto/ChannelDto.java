package assure.dto;

import assure.pojo.ChannelListingPojo;
import assure.pojo.ChannelPojo;
import assure.pojo.ProductPojo;
import assure.pojo.UserPojo;
import assure.service.ApiException;
import assure.service.ChannelService;
import assure.service.ProductService;
import assure.service.UserService;
import assure.util.ConversionUtil;
import assure.util.StringUtil;
import common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChannelDto {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @Transactional(rollbackFor = ApiException.class)
    public void add(ChannelForm channelForm) throws ApiException {
        normalize(channelForm);
        check(channelForm);
        channelService.add(ConversionUtil.convert(channelForm));
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(String clientName,String channelName,ChannelListingForm channelListingForm) throws ApiException {
        normalize(channelListingForm);
        check(channelListingForm);
        ChannelPojo channelPojo=channelService.get(channelName);
        ProductPojo productPojo=productService.getFromClientSkuId(channelListingForm.getClientSkuId());
        UserPojo userPojo=userService.get(clientName, Type.CLIENT);
        channelService.add(ConversionUtil.convert(channelPojo,productPojo,userPojo,channelListingForm));
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(String clientName, String channelName, List<ChannelListingForm> channelListingFormList) throws ApiException {
        ChannelPojo channelPojo=channelService.get(channelName);
        UserPojo userPojo=userService.get(clientName, Type.CLIENT);
        List<ChannelListingPojo> channelListingPojoList=new ArrayList<>();
        for (ChannelListingForm channelListingForm:channelListingFormList){
            normalize(channelListingForm);
            check(channelListingForm);
        }
        for(ChannelListingForm channelListingForm:channelListingFormList){
            ProductPojo productPojo=productService.getFromClientSkuId(channelListingForm.getClientSkuId());
            channelListingPojoList.add(ConversionUtil.convert(channelPojo,productPojo,userPojo,channelListingForm));
        }
        channelService.add(channelListingPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<ChannelData> get(){
        List<ChannelPojo> channelPojoList= channelService.getAll();
        return ConversionUtil.convertChannel(channelPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public ChannelData getByName(String name) throws ApiException {
        ChannelPojo channelPojo=channelService.get(name);
        return ConversionUtil.convert(channelPojo);
    }
    @Transactional(rollbackFor = ApiException.class)
    public List<ChannelListingData> get(Long channelId) throws ApiException {
        List<ChannelListingPojo> channelListingPojoList=channelService.getChannelListing(channelId);
        Map<ChannelListingPojo,ProductPojo> channelListingPojoProductPojoMap=channelService.getMap(channelListingPojoList);
        return ConversionUtil.convertChannelListing(channelListingPojoProductPojoMap);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<ChannelListingData> getAllChannelListing() throws ApiException {
        List<ChannelListingPojo> channelListingPojoList= channelService.getAllChannelListing();
        Map<ChannelListingPojo,ProductPojo> channelListingPojoProductPojoMap=channelService.getMap(channelListingPojoList);
        return ConversionUtil.convertChannelListing(channelListingPojoProductPojoMap);
    }

    private void check(ChannelForm channelForm) throws ApiException {
        if(StringUtil.isEmpty(channelForm.getName()))
            throw new ApiException("Name cannot be empty");
        if(channelForm.getInvoiceType()==null)
            throw new ApiException("Invoice type cannot be empty");
    }

    private void check(ChannelListingForm channelListingForm) throws ApiException {
        if(StringUtil.isEmpty(channelListingForm.getChannelSkuId()))
            throw new ApiException("Channel sku id cannot be empty");
        if(StringUtil.isEmpty(channelListingForm.getClientSkuId()))
            throw new ApiException("Client sku id cannot be empty");
    }

    public void normalize(ChannelForm channelForm){
        channelForm.setName(StringUtil.trim(channelForm.getName()));
    }

    public void normalize(ChannelListingForm channelListingForm){
       channelListingForm.setChannelSkuId(StringUtil.trim(channelListingForm.getChannelSkuId()));
       channelListingForm.setClientSkuId(StringUtil.trim(channelListingForm.getClientSkuId()));
    }
}