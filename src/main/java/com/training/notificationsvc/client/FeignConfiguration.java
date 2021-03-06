package com.training.notificationsvc.client;

import java.time.Duration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> customizer(){

		CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig
				.custom().slidingWindowType(SlidingWindowType.COUNT_BASED)
				.slidingWindowSize(5)
				.failureRateThreshold(20.0f)
				.waitDurationInOpenState(Duration.ofSeconds(5))
				.permittedNumberOfCallsInHalfOpenState(5)
				.slowCallDurationThreshold(Duration.ofSeconds(2))
				.slowCallRateThreshold(80.0f)
				.build();

		TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(5)).build();

		return resilience4JCircuitBreakerFactory -> resilience4JCircuitBreakerFactory.configure(resilience4JConfigBuilder ->
			resilience4JConfigBuilder.circuitBreakerConfig(circuitBreakerConfig).timeLimiterConfig(timeLimiterConfig), "AccountServiceClient#getTransaction(Long)");
		}
}
