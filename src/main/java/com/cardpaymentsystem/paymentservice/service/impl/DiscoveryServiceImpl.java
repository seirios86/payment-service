package com.cardpaymentsystem.paymentservice.service.impl;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import com.cardpaymentsystem.paymentservice.service.DiscoveryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscoveryServiceImpl implements DiscoveryService {

	private final DiscoveryClient discoveryClient;
	private static final String API_GATEWAY_SERVICE = "API-GATEWAY-SERVICE";

	public String getApiGatewayUrl() {

		List<ServiceInstance> instances = discoveryClient.getInstances(API_GATEWAY_SERVICE);
		if (instances == null || instances.isEmpty()) {
			throw new RuntimeException("Could not find API Gateway service.");
		}

		return instances.get(0).getUri().toString();
	}
}
