package com.fm.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fm.bean.CommonResponseBean;
import com.fm.bean.UserFriendRequestBean;
import com.fm.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/friendrequest", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public CommonResponseBean createUserFriendship(@Valid @RequestBody UserFriendRequestBean requestBean) {
        Boolean status = userService.createUserFriendship((String[])requestBean.getFriends().stream().toArray(String[]::new));
        
        return new CommonResponseBean(status);
    }
	
	@RequestMapping(method=RequestMethod.DELETE)
	public CommonResponseBean deleteUserByEmail(@RequestParam("email1")String email1,@RequestParam("email2")String email2){
		userService.deleteUser(email1,email2);
		
		return new CommonResponseBean(true);
	}

}
