package assure.dao;

import assure.pojo.ProductPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(ProductPojo productPojo){
        entityManager.persist(productPojo);
    }

    //Retrieve a product pojo with id
    @Transactional
    public ProductPojo select(Long id){
        return entityManager.find(ProductPojo.class,id);
    }

    //Retrieve all orders
    @Transactional
    public List<ProductPojo> selectAll() {
        String select_all = "select p from ProductPojo p";
        TypedQuery<ProductPojo> query = getQuery(select_all,  ProductPojo.class);
        return query.getResultList();
    }

    //Update a product pojo
    @Transactional
    public void update(Long id,ProductPojo productPojo) {
        ProductPojo productPojo1=entityManager.find(ProductPojo.class, id);
        productPojo1.setBrandId(productPojo.getBrandId());
        productPojo1.setDescription(productPojo.getDescription());
        productPojo1.setMrp(productPojo.getMrp());
        productPojo1.setName(productPojo.getName());
        entityManager.merge(productPojo1);
    }

    @Transactional
    public List<ProductPojo> getFromClientIdClientSkuId(Long clientId, String clientSkuId){
        String selectAll="select p from ProductPojo p where clientId=:clientId and clientSkuId=:clientSkuId";
        TypedQuery<ProductPojo> query=getQuery(selectAll,ProductPojo.class);
        query.setParameter("clientId", clientId);
        query.setParameter("clientSkuId", clientSkuId);
        return query.getResultList();
    }

    @Transactional
    public ProductPojo getFromClientSkuId(String clientSkuId){
        String select =" select p from ProductPojo p where clientSkuId=:clientSkuId";
        TypedQuery<ProductPojo> query=getQuery(select,ProductPojo.class);
        query.setParameter("clientSkuId",clientSkuId);
        return query.getSingleResult();
    }
}
