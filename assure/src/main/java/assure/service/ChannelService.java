package assure.service;

import assure.dao.ChannelDao;
import assure.pojo.ChannelListingPojo;
import assure.pojo.ChannelPojo;
import common.model.InvoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;

    @Transactional(rollbackFor = ApiException.class)
    public void add(ChannelPojo channelPojo) throws ApiException {
        checkDefault();
        checkAlreadyExist(channelPojo);
        channelDao.insert(channelPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(ChannelListingPojo channelListingPojo) throws ApiException {
        checkAlreadyExist(channelListingPojo);
        channelDao.insert(channelListingPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(List<ChannelListingPojo> channelListingPojoList) throws ApiException {
        for(ChannelListingPojo channelListingPojo:channelListingPojoList){
            checkAlreadyExist(channelListingPojo);
        }
        for(ChannelListingPojo channelListingPojo:channelListingPojoList){
            channelDao.insert(channelListingPojo);
        }
    }

    @Transactional(rollbackFor = ApiException.class)
    public ChannelPojo get(String name) throws ApiException {
        List<ChannelPojo> channelPojoList= channelDao.getByName(name);
        if(channelPojoList.isEmpty())
            throw new ApiException("Channel with given name does not exist: "+ name);
        return channelPojoList.get(0);
    }

    @Transactional(rollbackFor = ApiException.class)
    public ChannelPojo get(Long id) throws ApiException {
        ChannelPojo channelPojo=channelDao.get(id);
        if(channelPojo==null)
            throw new ApiException("Channel with given id does not exist: "+id);
        return channelPojo;
    }

    public void checkDefault(){
        List<ChannelPojo> channelPojoList= channelDao.getAll();
        if(channelPojoList.isEmpty())
        {
            ChannelPojo channelPojo=new ChannelPojo();
            channelPojo.setInvoiceType(InvoiceType.SELF);
            channelPojo.setName("INTERNAL");
            //channelPojo.setId(1L);
            channelDao.insert(channelPojo);
        }
    }
    public void checkAlreadyExist(ChannelPojo channelPojo) throws ApiException {
        List<ChannelPojo> channelPojoList= channelDao.getByName(channelPojo.getName());
        if(!channelPojoList.isEmpty())
            throw new ApiException("Channel with given name already exists: "+channelPojo.getName());
    }

    public void checkAlreadyExist(ChannelListingPojo channelListingPojo) throws ApiException {
        List<ChannelListingPojo> channelListingPojoList=channelDao.getByChannelSkuId(channelListingPojo.getChannelSkuId());
        if(!channelListingPojoList.isEmpty())
            throw new ApiException("Channel with given channel sku id does not exist: "+channelListingPojo.getChannelSkuId());
    }
}
