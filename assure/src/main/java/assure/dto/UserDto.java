package assure.dto;

import assure.pojo.UserPojo;
import assure.service.ApiException;
import assure.service.UserService;
import assure.util.ConversionUtil;
import assure.util.StringUtil;
import common.model.UserData;
import common.model.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDto {
    @Autowired
    private UserService userService;

    @Transactional(rollbackFor = ApiException.class)
    public void add(UserForm userForm) throws ApiException {
        normalize(userForm);
        check(userForm);
        userService.add(ConversionUtil.convert(userForm));
    }
    @Transactional(rollbackFor = ApiException.class)
    public UserData get(Long id) throws ApiException {
        UserPojo userPojo= userService.get(id);
        return ConversionUtil.convert(userPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<UserData> getAll() {
        List<UserPojo> userPojoList = userService.getAll();
        return ConversionUtil.convert(userPojoList);
    }
    public void check(UserForm userForm) throws ApiException {
        if(StringUtil.isEmpty(userForm.getName())){
            throw new ApiException("Name cannot be empty");
        }
        if(userForm.getType()==null){
            throw new ApiException("Type cannot be empty");
        }
    }

    public void normalize(UserForm userForm){
        userForm.setName(StringUtil.trim(userForm.getName()));
    }
}
