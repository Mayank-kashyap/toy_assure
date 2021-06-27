package assure.controller;

import assure.dto.UserDto;
import common.model.UserData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import common.model.UserForm;
import assure.service.ApiException;

import java.util.List;

@RestController
@Api
public class UserController extends ExceptionHandler {
    @Autowired
    private UserDto userDto;

    @ApiOperation(value = "Adds a user")
    @RequestMapping(path = "/api/user", method = RequestMethod.POST)
    public void add(@RequestBody UserForm userForm) throws ApiException {
        userDto.add(userForm);
    }

    @ApiOperation(value = "Get a user")
    @RequestMapping(path = "/api/user/{id}", method = RequestMethod.GET)
    public UserData get(@PathVariable Long id) throws ApiException {
        return userDto.get(id);
    }
    @ApiOperation(value = "Gets list of users")
    @RequestMapping(path = "/api/user", method = RequestMethod.GET)
    public List<UserData> getAll(){
        return userDto.getAll();
    }
}
