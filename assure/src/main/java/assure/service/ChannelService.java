package assure.service;

import assure.dao.ChannelDao;
import assure.pojo.ChannelListingPojo;
import assure.pojo.ChannelPojo;
import assure.pojo.ProductPojo;
import common.model.InvoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private ProductService productService;

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
        return checkChannelExists(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<ChannelPojo> getAll(){
        return channelDao.getAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<ChannelListingPojo> getAllChannelListing(){
        return channelDao.getAllChannelListing();
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<ChannelListingPojo> getChannelListing(Long channelId) throws ApiException {
        checkChannelExists(channelId);
        return channelDao.getByChannelId(channelId);
    }

    @Transactional(rollbackFor = ApiException.class)
    public ChannelListingPojo getChannelListing(String channelSkuId) throws ApiException {
        ChannelListingPojo channelListingPojo=channelDao.getByChannelSkuId(channelSkuId);
        if (channelListingPojo==null)
            throw new ApiException("Channel listing with given channelSkuId does not exist: "+channelSkuId);
        return channelListingPojo;
    }

    public void checkDefault(){
        List<ChannelPojo> channelPojoList= channelDao.getAll();
        if(channelPojoList.isEmpty())
        {
            ChannelPojo channelPojo=new ChannelPojo();
            channelPojo.setInvoiceType(InvoiceType.SELF);
            channelPojo.setName("INTERNAL");
            channelDao.insert(channelPojo);
        }
    }

    public void checkAlreadyExist(ChannelPojo channelPojo) throws ApiException {
        List<ChannelPojo> channelPojoList= channelDao.getByName(channelPojo.getName());
        if(!channelPojoList.isEmpty())
            throw new ApiException("Channel with given name already exists: "+channelPojo.getName());
    }

    public void checkAlreadyExist(ChannelListingPojo channelListingPojo) throws ApiException {
        ChannelListingPojo channelListingPojoList=channelDao.getByChannelSkuId(channelListingPojo.getChannelSkuId());
        if(channelListingPojoList!=null)
            throw new ApiException("Channel with given channel sku id already exists: "+channelListingPojo.getChannelSkuId());
    }

    public ChannelPojo checkChannelExists(Long channelId) throws ApiException {
        ChannelPojo channelPojo=channelDao.get(channelId);
        if(channelPojo==null)
            throw new ApiException("Channel with given id does not exist: "+channelId);
        return channelPojo;
    }

    public Map<ChannelListingPojo, ProductPojo> getMap(List<ChannelListingPojo> channelListingPojoList) throws ApiException {
        Map<ChannelListingPojo, ProductPojo> channelListingPojoProductPojoMap=new HashMap<>();
        for (ChannelListingPojo channelListingPojo:channelListingPojoList){
            ProductPojo productPojo=productService.get(channelListingPojo.getGlobalSkuId());
            channelListingPojoProductPojoMap.put(channelListingPojo,productPojo);
        }
        return channelListingPojoProductPojoMap;
    }
}
