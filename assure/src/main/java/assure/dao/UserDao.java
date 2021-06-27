package assure.dao;

import common.model.Type;
import assure.pojo.UserPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insert(UserPojo userPojo){
        entityManager.persist(userPojo);
    }

    @Transactional
    public UserPojo select(Long id){
        return entityManager.find(UserPojo.class,id);
    }

    @Transactional
    public List<UserPojo> getAll(){
        String selectAll="select p from UserPojo p";
        TypedQuery<UserPojo> query=getQuery(selectAll, UserPojo.class);
        if(query == null){
            return new ArrayList<>();
        }
        return query.getResultList();
    }

    @Transactional
    public List<UserPojo> getByName(String name, Type type){
        String selectAll="select p from UserPojo p where name=:name and type=:type";
        TypedQuery<UserPojo> query= getQuery(selectAll, UserPojo.class);
        query.setParameter("name",name);
        query.setParameter("type",type);
        return query.getResultList();
    }
}
