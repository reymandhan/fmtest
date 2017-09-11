package com.fm.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fm.bean.CommonResponseBean;
import com.fm.bean.FriendListResponseBean;
import com.fm.bean.RequestorTargetRequestBean;
import com.fm.bean.SingleEmailRequestBean;
import com.fm.bean.TwoEmailRequestBean;
import com.fm.bean.UpdateRecipientRequestBean;
import com.fm.bean.UpdateRecipientResponseBean;
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
	
	@RequestMapping(value="/commonfriendlist", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public FriendListResponseBean retrieveCommonFriendList(@Valid @RequestBody TwoEmailRequestBean requestBean){
		List<String> result = userService.retrieveCommonFriendList(requestBean.getFriends().get(0), requestBean.getFriends().get(1));
		
		FriendListResponseBean response = new FriendListResponseBean();
		response.setSuccess(true);
		response.setFriends(result);
		response.setCount(result.size());
		
		return response;
		
	}	
	
	@RequestMapping(value="/subscribe", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public CommonResponseBean createSubscribership(@Valid @RequestBody RequestorTargetRequestBean requestBean) {
        userService.createSubscribership(requestBean.getRequestor(), requestBean.getTarget());
        
        return new CommonResponseBean(true);
    }
	
	@RequestMapping(value="/block", method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CommonResponseBean blockUser(@Valid @RequestBody RequestorTargetRequestBean requestBean){
		userService.blockUser(requestBean.getRequestor(), requestBean.getTarget());
		
		return new CommonResponseBean(true);
	}
	
	@RequestMapping(value="/updaterecipient", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public UpdateRecipientResponseBean retrieveUpdateRecipients(@Valid @RequestBody UpdateRecipientRequestBean requestBean){
		List<String> result = userService.retrieveUpdateRecipient(requestBean.getSender(), requestBean.getText());
		
		UpdateRecipientResponseBean response = new UpdateRecipientResponseBean();
		response.setSuccess(true);
		response.setRecipients(result);
		
		return response;
	}
	
	@RequestMapping(method=RequestMethod.DELETE)
	public CommonResponseBean deleteUserByEmail(@RequestParam("email1")String email1,@RequestParam("email2")String email2, @RequestParam(value="delete",required=false)String delete){
		userService.deleteUser(email1,email2,StringUtils.isEmpty(delete)?true:Boolean.getBoolean(delete));
		
		return new CommonResponseBean(true);
	}

}
