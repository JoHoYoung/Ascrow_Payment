package com.sharescrow.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedHashMap;

import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.response.DataResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"dev"})
public class OrderControllerTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@Test
	public void userOrderListTest() throws Exception {
		mockMvc.perform(get("/api/v1/order/list/user?userId=10"))
			.andExpect(status().isOk());
	}

	@Test
	public void testpayTest() throws Exception{
		Order order = Order.builder().userId(100)
		.maxShare(5)
		.minShare(5)
		.productId(1)
		.productOptionId(1)
		.quantitiy(1).build();

		mockMvc.perform(post("/api/v1/order/execute/test")
		.contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(order)))
			.andExpect(status().isOk());
	}

	@Test
	public void confirmTest() throws Exception{
		Order order = Order.builder().userId(100)
			.maxShare(5)
			.minShare(5)
			.productId(1)
			.productOptionId(1)
			.quantitiy(1).build();

		MvcResult result = mockMvc.perform(post("/api/v1/order/execute/test")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(order))).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		DataResponse response =  objectMapper.readValue(result.getResponse().getContentAsString()
			, DataResponse.class);

		String orderId = ((LinkedHashMap) (response.getData())).get("id").toString();
		// get bad request beacause status of Order is no GET_GOODS, but TRANSACTION_DONE
		mockMvc.perform(get("/api/v1/order/confirm?orderId=".concat(orderId)))
			.andExpect(status().isBadRequest());
	}

}
