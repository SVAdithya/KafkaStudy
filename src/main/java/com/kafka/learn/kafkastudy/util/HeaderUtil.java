package com.kafka.learn.kafkastudy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Configuration
public class HeaderUtil {
	@Value("#{'${mask.headers.list}'.split(',')}")
	private Set<String> maskHeaders = new HashSet<>();

	public String headersMapToString(Headers headers) {
		ObjectMapper oj = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		headers.forEach(header -> {
			map.put(header.key(), (!maskHeaders.contains(header.key())) ? new String(header.value(), StandardCharsets.UTF_8) : "");
		});
		try {
			return oj.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			return null;
		}
	}
}
