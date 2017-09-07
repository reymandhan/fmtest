package com.fm.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fm.bean.CommonResponseBean;
import com.fm.bean.FriendListResponseBean;
import com.fm.bean.SingleEmailRequestBean;
import com.fm.bean.TwoEmailRequestBean;
import com.fm.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/friendrequest", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public CommonResponseBean createUserFriendship(@Valid @RequestBody TwoEmailRequestBean requestBean) {
        Boolean status = userService.createUserFriendship((String[])requestBean.getFriends().stream().toArray(String[]::new));
        
        return new CommonResponseBean(status);
    }
	
	@RequestMapping(value="/friendlist", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public FriendListResponseBean retrieveFriendList(@Valid @RequestBody SingleEmailRequestBean requestBean){
		List<String> result = userService.retrieveFriendList(requestBean.getEmail());
		
		FriendListResponseBean response = new FriendListResponseBean();
		response.setSuccess(true);
		response.setFriends(result);
		response.setCount(result.size());
		
		return response;
		
	}	
	
	@RequestMapping(method=RequestMethod.DELETE)
	public CommonResponseBean deleteUserByEmail(@RequestParam("email1")String email1,@RequestParam("email2")String email2){
		userService.deleteUser(email1,email2);
		
		return new CommonResponseBean(true);
	}

}
