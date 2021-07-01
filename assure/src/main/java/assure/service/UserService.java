package assure.service;

import assure.dao.UserDao;
import assure.pojo.UserPojo;
import common.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Transactional(rollbackFor = ApiException.class)
    public void add(UserPojo userPojo) throws ApiException {
        checkAlreadyExist(userPojo);
        userDao.insert(userPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public UserPojo get(Long id) throws ApiException {
        return check(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public UserPojo get(String name,Type type) throws ApiException {
        List<UserPojo> userPojoList = userDao.getByName(name, type);
        if(userPojoList.isEmpty())
            throw new ApiException("User with name: "+name+" and type: "+type+" does not exist");
        return userPojoList.get(0);
    }

    @Transactional
    public List<UserPojo> getAll(){
        return userDao.getAll();
    }

    @Transactional
    public UserPojo check(Long id) throws ApiException {
        UserPojo userPojo = userDao.select(id);
        if(userPojo ==null)
            throw new ApiException("User with given id does not exist");
        return userPojo;
    }
    public void checkAlreadyExist(UserPojo userPojo) throws ApiException {
        List<UserPojo> userPojoList = userDao.getByName(userPojo.getName(),userPojo.getType());
        if(!userPojoList.isEmpty()){
            throw new ApiException("User with name: "+userPojo.getName()+" and type: "+userPojo.getType()+" already exists");
        }
    }


}
