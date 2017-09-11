package com.fm.test;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.fm.bean.FriendListResponseBean;
import com.fm.bean.RequestorTargetRequestBean;
import com.fm.bean.SingleEmailRequestBean;
import com.fm.bean.TwoEmailRequestBean;
import com.fm.bean.UpdateRecipientRequestBean;
import com.fm.bean.UpdateRecipientResponseBean;
import com.fm.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class UserControllerTest {

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

		TwoEmailRequestBean bean = new TwoEmailRequestBean();
		bean.setFriends(emailList);

		byte[] r1Json = JsonUtil.toJson(bean);

		// Validate empty email
		MvcResult result = mvc.perform(post("/user/friendrequest").content(r1Json)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();

		CommonResponseBean response = JsonUtil.toObject(result.getResponse().getContentAsString(),
				CommonResponseBean.class);

		assertTrue(!response.getSuccess());

		// validate invalid email format
		emailList.add("notEmail");
		emailList.add("notEmail2");

		r1Json = JsonUtil.toJson(bean);

		result = mvc.perform(post("/user/friendrequest").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

		response = JsonUtil.toObject(result.getResponse().getContentAsString(), CommonResponseBean.class);
		assertTrue(!response.getSuccess());
		assertTrue(response.getErrors().size() == 2);

		// Success create friendship
		emailList.clear();
		emailList.add("email@email.com");
		emailList.add("email2@email.com");

		r1Json = JsonUtil.toJson(bean);

		result = mvc.perform(post("/user/friendrequest").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		response = JsonUtil.toObject(result.getResponse().getContentAsString(), CommonResponseBean.class);
		assertTrue(response.getSuccess());

		// Validate Existing friendship
		result = mvc.perform(post("/user/friendrequest").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

		response = JsonUtil.toObject(result.getResponse().getContentAsString(), CommonResponseBean.class);
		assertTrue(!response.getSuccess());

		// delete test data
		mvc.perform(delete("/user").param("email1", "email@email.com").param("email2", "email2@email.com")).andReturn();

	}

	@Test
	public void retrieveFriendListTest() throws Exception {
		// Prepare Test Data
		List<String> emailList = new ArrayList<>();
		emailList.add("email@email.com");
		emailList.add("email2@email.com");

		TwoEmailRequestBean bean = new TwoEmailRequestBean();
		bean.setFriends(emailList);

		byte[] r1Json = JsonUtil.toJson(bean);

		MvcResult result = mvc.perform(post("/user/friendrequest").content(r1Json)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();

		// Email not Exists
		SingleEmailRequestBean requestBean = new SingleEmailRequestBean("notExistsEmail");
		r1Json = JsonUtil.toJson(requestBean);

		result = mvc.perform(post("/user/friendlist").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

		// Retrieve success
		requestBean.setEmail("email@email.com");
		r1Json = JsonUtil.toJson(requestBean);

		result = mvc.perform(post("/user/friendlist").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		FriendListResponseBean response = JsonUtil.toObject(result.getResponse().getContentAsString(),
				FriendListResponseBean.class);
		assertTrue(response.getCount() == 1);

		// delete test data
		mvc.perform(delete("/user").param("email1", "email@email.com").param("email2", "email2@email.com")).andReturn();
	}

	@Test
	public void retrieveCommonFriendListTest() throws Exception {
		// Prepare Test Data
		List<String> emailList = new ArrayList<>();
		emailList.add("email@email.com");
		emailList.add("email2@email.com");

		TwoEmailRequestBean bean = new TwoEmailRequestBean();
		bean.setFriends(emailList);

		byte[] r1Json = JsonUtil.toJson(bean);

		MvcResult result = mvc.perform(post("/user/friendrequest").content(r1Json)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();

		emailList.clear();
		emailList.add("email@email.com");
		emailList.add("email3@email.com");

		bean = new TwoEmailRequestBean();
		bean.setFriends(emailList);

		r1Json = JsonUtil.toJson(bean);

		mvc.perform(post("/user/friendrequest").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		// Email not Exists
		emailList.clear();
		emailList.add("notexistsemail@email.com");
		emailList.add("email3@email.com");

		bean = new TwoEmailRequestBean();
		bean.setFriends(emailList);

		r1Json = JsonUtil.toJson(bean);

		result = mvc.perform(post("/user/commonfriendlist").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

		// Retrieve common friend success
		emailList.clear();
		emailList.add("email2@email.com");
		emailList.add("email3@email.com");

		bean = new TwoEmailRequestBean();
		bean.setFriends(emailList);

		r1Json = JsonUtil.toJson(bean);

		result = mvc.perform(post("/user/commonfriendlist").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		FriendListResponseBean response = JsonUtil.toObject(result.getResponse().getContentAsString(),
				FriendListResponseBean.class);
		assertTrue(response.getCount() == 1);

		// delete test data
		mvc.perform(delete("/user").param("email1", "email2@email.com").param("email2", "email@email.com")
				.param("delete", "false")).andReturn();
		mvc.perform(delete("/user").param("email1", "email@email.com").param("email2", "email3@email.com")).andReturn();
	}

	@Test
	public void createSubscribership() throws Exception {

		RequestorTargetRequestBean bean = new RequestorTargetRequestBean();
		byte[] r1Json = JsonUtil.toJson(bean);

		// Validate empty email
		MvcResult result = mvc.perform(post("/user/subscribe").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

		CommonResponseBean response = JsonUtil.toObject(result.getResponse().getContentAsString(),
				CommonResponseBean.class);

		assertTrue(!response.getSuccess());

		// validate invalid email format
		bean.setRequestor("notvalidemail");
		bean.setTarget("notvalidemail");

		r1Json = JsonUtil.toJson(bean);

		result = mvc.perform(post("/user/subscribe").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

		response = JsonUtil.toObject(result.getResponse().getContentAsString(), CommonResponseBean.class);
		assertTrue(!response.getSuccess());
		assertTrue(response.getErrors().size() == 2);

		// Success subscribe
		bean.setRequestor("requestor@example.com");
		bean.setTarget("target@example.com");

		r1Json = JsonUtil.toJson(bean);

		result = mvc.perform(post("/user/subscribe").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		response = JsonUtil.toObject(result.getResponse().getContentAsString(), CommonResponseBean.class);
		assertTrue(response.getSuccess());

		// Validate already subscribed
		result = mvc.perform(post("/user/subscribe").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

		response = JsonUtil.toObject(result.getResponse().getContentAsString(), CommonResponseBean.class);
		assertTrue(!response.getSuccess());

		// delete test data
		mvc.perform(delete("/user").param("email1", "requestor@example.com").param("email2", "target@example.com"))
				.andReturn();

	}

	@Test
	public void testBlockUser() throws Exception {
		RequestorTargetRequestBean bean = new RequestorTargetRequestBean();
		byte[] r1Json = JsonUtil.toJson(bean);

		// Validate empty email
		MvcResult result = mvc.perform(put("/user/block").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

		CommonResponseBean response = JsonUtil.toObject(result.getResponse().getContentAsString(),
				CommonResponseBean.class);

		assertTrue(!response.getSuccess());

		// validate invalid email format
		bean.setRequestor("notvalidemail");
		bean.setTarget("notvalidemail");

		r1Json = JsonUtil.toJson(bean);

		result = mvc.perform(put("/user/block").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

		response = JsonUtil.toObject(result.getResponse().getContentAsString(), CommonResponseBean.class);
		assertTrue(!response.getSuccess());
		assertTrue(response.getErrors().size() == 2);

		// validate success blocking
		bean.setRequestor("requestor@example.com");
		bean.setTarget("target@example.com");

		r1Json = JsonUtil.toJson(bean);

		result = mvc.perform(put("/user/block").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		response = JsonUtil.toObject(result.getResponse().getContentAsString(), CommonResponseBean.class);
		assertTrue(response.getSuccess());

		// Target cannot add friendship with requestor because of blocking
		List<String> emailList = new ArrayList<>();

		emailList.add("target@example.com");
		emailList.add("requestor@example.com");

		TwoEmailRequestBean requestBean = new TwoEmailRequestBean();
		requestBean.setFriends(emailList);

		r1Json = JsonUtil.toJson(requestBean);

		result = mvc.perform(post("/user/friendrequest").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

		response = JsonUtil.toObject(result.getResponse().getContentAsString(), CommonResponseBean.class);
		assertTrue(!response.getSuccess());

		// delete test data
		mvc.perform(delete("/user").param("email1", "requestor@example.com").param("email2", "target@example.com"))
				.andReturn();

	}
	
	@Test
	public void retrieveUpdateRecipientTest() throws Exception {
		// Prepare Test Data
		// Create Friendship
		List<String> emailList = new ArrayList<>();
		emailList.add("email@email.com");
		emailList.add("email2@email.com");

		TwoEmailRequestBean bean = new TwoEmailRequestBean();
		bean.setFriends(emailList);

		byte[] r1Json = JsonUtil.toJson(bean);

		MvcResult result = mvc.perform(post("/user/friendrequest").content(r1Json)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();

		emailList.clear();
		emailList.add("email@email.com");
		emailList.add("email3@email.com");

		bean.setFriends(emailList);

		r1Json = JsonUtil.toJson(bean);

		result = mvc.perform(post("/user/friendrequest").content(r1Json)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();
		
		//one person block target
		RequestorTargetRequestBean bean2 = new RequestorTargetRequestBean();
		bean2.setRequestor("email3@email.com");
		bean2.setTarget("email@email.com");

		r1Json = JsonUtil.toJson(bean2);

		result = mvc.perform(put("/user/block").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		//Create subscriber
		bean2 = new RequestorTargetRequestBean();
		bean2.setRequestor("subscriber@email.com");
		bean2.setTarget("email@email.com");

		r1Json = JsonUtil.toJson(bean2);

		mvc.perform(post("/user/subscribe").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		// Email not Exists
		UpdateRecipientRequestBean bean3 = new UpdateRecipientRequestBean();
		bean3.setSender("notExistsEmail@email.com");

		r1Json = JsonUtil.toJson(bean3);

		result = mvc.perform(post("/user/updaterecipient").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

		// Retrieve update recipient success
		bean3.setSender("email@email.com");
		bean3.setText("add another recipient another@email.com");

		r1Json = JsonUtil.toJson(bean3);

		result = mvc.perform(post("/user/updaterecipient").content(r1Json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		UpdateRecipientResponseBean response = JsonUtil.toObject(result.getResponse().getContentAsString(),
				UpdateRecipientResponseBean.class);
		assertTrue(response.getRecipients().size() == 3);

		// delete test data
		mvc.perform(delete("/user").param("email1", "email3@email.com").param("email2", "email@email.com")
				.param("delete", "false")).andReturn();
		mvc.perform(delete("/user").param("email1", "email2@email.com").param("email2", "email@email.com")
				.param("delete", "false")).andReturn();
		mvc.perform(delete("/user").param("email1", "subscriber@email.com").param("email2", "email@email.com")).andReturn();
	}
}
