package com.aummn.sqs.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController()
@RequestMapping("/api")
class StaffService {

	@GetMapping("/staff/name")
	public String staffName() {
		log.info("in method staffName() - /api/staff/name");
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(2);
		if (randomInt < 2) {
//			throw new StaffException("Service is unavailable");
		}
		return "Michael";
	}
}

