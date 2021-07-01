package channel.dto;

import channel.assure.AssureClient;
import channel.util.ApiException;
import channel.util.StringUtil;
import common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelDto {
    @Autowired
    private AssureClient assureClient;

    @Transactional(rollbackFor = ApiException.class)
    public void add(ChannelForm channelForm) throws ApiException {
        normalize(channelForm);
        check(channelForm);
        assureClient.add(channelForm);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(String clientName,String channelName,ChannelListingForm channelListingForm) throws ApiException {
        normalize(channelListingForm);
        check(channelListingForm);
        assureClient.add(clientName,channelName,channelListingForm);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(String clientName,String channelName,List<ChannelListingForm> channelListingFormList) throws ApiException {
        for (ChannelListingForm channelListingForm:channelListingFormList)
        {
            normalize(channelListingForm);
            check(channelListingForm);
        }

        assureClient.add(clientName,channelName,channelListingFormList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<ChannelData> get() throws ApiException {
        return assureClient.get();
    }

    @Transactional(rollbackFor = ApiException.class)
    public ChannelData getByName(String name) throws ApiException {
        return assureClient.getByName(name);
    }
    @Transactional(rollbackFor = ApiException.class)
    public List<ChannelListingData> getAllChannelListing() throws ApiException {
        return assureClient.getAllChannelListing();
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<UserData> getAllUser() throws ApiException {
        return assureClient.getAllUser();
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
