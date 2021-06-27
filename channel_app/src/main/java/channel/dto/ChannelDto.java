package channel.dto;

import channel.assure.AssureClient;
import channel.util.ApiException;
import common.model.ChannelForm;
import common.model.ChannelListingForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChannelDto {
    @Autowired
    private AssureClient assureClient;

    @Transactional(rollbackFor = ApiException.class)
    public void add(ChannelForm channelForm){

    }

    private void check(ChannelForm channelForm) throws ApiException {
        if(channelForm.getName()==null)
            throw new ApiException("Name cannot be empty");
        if(channelForm.getInvoiceType()==null)
            throw new ApiException("Invoice type cannot be empty");
    }

    private void check(ChannelListingForm channelListingForm) throws ApiException {
        if(channelListingForm.getChannelSkuId()==null)
            throw new ApiException("Channel sku id cannot be empty");
        if(channelListingForm.getClientSkuId()==null)
            throw new ApiException("Client sku id cannot be empty");
    }
}
