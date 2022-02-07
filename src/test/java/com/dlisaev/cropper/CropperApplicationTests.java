package com.dlisaev.cropper;

import com.dlisaev.cropper.controllers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CropperApplicationTests {
	private AuthController authController;
	private CropController cropController;
	private NotificationController notificationController;
	private OfferController offerController;
	private UserController userController;

	@Autowired
	public CropperApplicationTests(AuthController authController, CropController cropController, NotificationController notificationController, OfferController offerController, UserController userController) {
		this.authController = authController;
		this.cropController = cropController;
		this.notificationController = notificationController;
		this.offerController = offerController;
		this.userController = userController;
	}

	@Test
	void contextLoads() {
		assertThat(authController).isNotNull();
		assertThat(cropController).isNotNull();
		assertThat(notificationController).isNotNull();
		assertThat(offerController).isNotNull();
		assertThat(userController).isNotNull();
	}

}
