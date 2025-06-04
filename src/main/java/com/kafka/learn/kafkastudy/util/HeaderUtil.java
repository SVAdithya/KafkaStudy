package com.kafka.learn.kafkastudy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Configuration
public class HeaderUtil {
	private static final Logger logger = LoggerFactory.getLogger(HeaderUtil.class);

	@Value("#{'${mask.headers.list}'.split(',')}")
	private Set<String> maskHeaders = new HashSet<>();

	public String headersMapToString(Headers headers) {
		ObjectMapper oj = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		headers.forEach(header -> {
			String headerValue = null;
			if (header.value() != null) {
				headerValue = new String(header.value(), StandardCharsets.UTF_8);
			}
			map.put(header.key(), (!maskHeaders.contains(header.key())) ? headerValue : "");
		});
		try {
			return oj.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			logger.error("Exception in HeaderUtil {}", e.toString());
			return null;
		}
	}
}
