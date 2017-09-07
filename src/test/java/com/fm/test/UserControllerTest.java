package com.fm.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fm.Application;
import com.fm.bean.CommonResponseBean;
import com.fm.bean.UserFriendRequestBean;
import com.fm.controller.UserController;
import com.fm.util.JsonUtil;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class UserControllerTest {

	@InjectMocks
	UserController controller;

	@Autowired
	WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void initTests() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void createUserFriendship() throws Exception {
		List<String> emailList = new ArrayList<>();

		UserFriendRequestBean bean = new UserFriendRequestBean();
		bean.setFriends(emailList);

		byte[] r1Json = JsonUtil.toJson(bean);

		// Validate empty email
		MvcResult result = mvc.perform(post("/user/friendrequest").content(r1Json)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();

		CommonResponseBean response = JsonUtil.toObject(result.getResponse().getContentAsString(),
				CommonResponseBean.class);

		assertTrue(!response.getStatus());

		// validate invalid email format
		emailList.add("notEmail");
		emailList.add("notEmail2");

		r1Json = JsonUtil.toJson(bean);

		result = mvc.perform(post("/user/friendrequest").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

		response = JsonUtil.toObject(result.getResponse().getContentAsString(), CommonResponseBean.class);
		assertTrue(!response.getStatus());
		assertTrue(response.getErrors().size() == 2);

		// Success create friendship
		emailList.clear();
		emailList.add("email@email.com");
		emailList.add("email2@email.com");

		r1Json = JsonUtil.toJson(bean);

		result = mvc.perform(post("/user/friendrequest").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		response = JsonUtil.toObject(result.getResponse().getContentAsString(), CommonResponseBean.class);
		assertTrue(response.getStatus());

		// Validate Existing friendship
		result = mvc.perform(post("/user/friendrequest").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

		response = JsonUtil.toObject(result.getResponse().getContentAsString(), CommonResponseBean.class);
		assertTrue(!response.getStatus());

		// delete test data
		mvc.perform(delete("/user").param("email1", "email@email.com").param("email2", "email2@email.com")).andReturn();


	}
}
