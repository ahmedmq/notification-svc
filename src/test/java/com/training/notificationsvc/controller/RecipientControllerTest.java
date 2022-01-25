package com.training.notificationsvc.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest
public class RecipientControllerTest {

	void shouldReturn201WhenNewRecipientRequestIsSent(){

		// POST /api/recipients -> CreateRecipientRequestDto
		// RESPONSE : HTTP status 201, Location: /api/recipient/{id}

		// Excercise - TODO

		// Refere Account Service - AccountControllerTest - shouldReturn201AndLocationHeaderWhenAccountCreatedSuccessfully()
	}
}
