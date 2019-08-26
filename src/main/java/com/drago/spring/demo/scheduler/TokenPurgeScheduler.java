package com.drago.spring.demo.scheduler;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.drago.spring.demo.repositories.VerificatinTokenRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenPurgeScheduler implements Scheduler {

	@Autowired
	private VerificatinTokenRepository passwordTokenRepository;
	
	@Override
	@Transactional
	@Scheduled(cron = "${spring.purge.token.cron.expression}")
	public void run() {
		log.info("Starting token purge scheduler...");
		LocalDateTime now = LocalDateTime.now();
		passwordTokenRepository.deleteAllExpiredTokens(java.sql.Timestamp.valueOf(now));

	}

}
