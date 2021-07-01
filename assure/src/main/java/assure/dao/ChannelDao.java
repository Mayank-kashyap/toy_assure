package assure.dao;

import assure.pojo.ChannelListingPojo;
import assure.pojo.ChannelPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ChannelDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(ChannelPojo channelPojo){
        entityManager.persist(channelPojo);
    }

    @Transactional
    public void insert(ChannelListingPojo channelListingPojo){entityManager.persist(channelListingPojo);}

    @Transactional
    public ChannelPojo get(Long id){
        return entityManager.find(ChannelPojo.class,id);
    }

    @Transactional
    public List<ChannelPojo> getAll(){
        String select="select p from ChannelPojo p";
        TypedQuery<ChannelPojo> query=getQuery(select,ChannelPojo.class);
        if(query == null){
            return new ArrayList<>();
        }
        return query.getResultList();
    }

    @Transactional
    public List<ChannelListingPojo> getAllChannelListing(){
        String select="select p from ChannelListingPojo p";
        TypedQuery<ChannelListingPojo> query=getQuery(select,ChannelListingPojo.class);
        if(query == null){
            return new ArrayList<>();
        }
        return query.getResultList();
    }

    @Transactional
    public List<ChannelPojo> getByName(String name){
        String select="select p from ChannelPojo p where name=:name";
        TypedQuery<ChannelPojo> query= getQuery(select,ChannelPojo.class);
        query.setParameter("name",name);
        return query.getResultList();
    }

    @Transactional
    public ChannelListingPojo getByChannelSkuId(String channelSkuId){
        String select="select p from ChannelListingPojo p where channelSkuId=:channelSkuId";
        TypedQuery<ChannelListingPojo> query=getQuery(select,ChannelListingPojo.class);
        query.setParameter("channelSkuId",channelSkuId);
        List<ChannelListingPojo> channelListingPojoList= query.getResultList();
        if(channelListingPojoList.size()>0){
            return channelListingPojoList.get(0);
        }
        else
            return null;
    }

    @Transactional
    public List<ChannelListingPojo> getByChannelId(Long channelId){
        String select="select p from ChannelListingPojo p where channelId=:channelId";
        TypedQuery<ChannelListingPojo> query=getQuery(select,ChannelListingPojo.class);
        query.setParameter("channelId",channelId);
        return query.getResultList();
    }
}
