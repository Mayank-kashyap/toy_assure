package assure.dao;

import assure.pojo.BinPojo;
import assure.pojo.BinSkuPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BinDao extends AbstractDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Long insert(BinPojo binPojo){
        entityManager.persist(binPojo);
        entityManager.flush();
        return binPojo.getId();
    }

    @Transactional
    public void insert(BinSkuPojo binSkuPojo){
        entityManager.persist(binSkuPojo);
    }

    @Transactional
    public BinSkuPojo get(Long id){
        return entityManager.find(BinSkuPojo.class,id);
    }

    @Transactional
    public List<BinSkuPojo> getALl(){
        String select="select p from BinSkuPojo p";
        TypedQuery<BinSkuPojo> query=getQuery(select,BinSkuPojo.class);
        return query.getResultList();
    }

    @Transactional
    public BinPojo getBin(Long id){
        return  entityManager.find(BinPojo.class,id);
    }

    @Transactional
    public void update(Long id,BinSkuPojo binSkuPojo){
        BinSkuPojo binSkuPojo1=entityManager.find(BinSkuPojo.class,id);
        binSkuPojo1.setQuantity(binSkuPojo.getQuantity());
        entityManager.merge(binSkuPojo1);
    }
    @Transactional
    public BinSkuPojo getFromBinIdGlobalSkuId(Long binId,Long globalSkuId){
        String select="select p from BinSkuPojo p where binId=:binId and globalSkuId=:globalSkuId";
        TypedQuery<BinSkuPojo> query=getQuery(select,BinSkuPojo.class);
        query.setParameter("binId",binId);
        query.setParameter("globalSkuId",globalSkuId);
        List<BinSkuPojo> binSkuPojoList= query.getResultList();
        if(binSkuPojoList.size()>0)
            return binSkuPojoList.get(0);
        else
            return null;
    }

    @Transactional
    public List<BinSkuPojo> getFromGlobalSkuId (Long globalSkuId){
        String select="select p from BinSkuPojo p where globalSkuId=:globalSkuId";
        TypedQuery<BinSkuPojo> query=getQuery(select,BinSkuPojo.class);
        query.setParameter("globalSkuId",globalSkuId);
        return query.getResultList();
    }
}
