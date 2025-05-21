/*
package com.kafka.learn.kafkastudy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HeaderUtilTest {

    private HeaderUtil headerUtil;

    @Mock
    private Headers mockHeaders;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        headerUtil = new HeaderUtil();
    }

    private Header mockHeader(String key, String value) {
        Header header = mock(Header.class);
        when(header.key()).thenReturn(key);
        when(header.value()).thenReturn(value.getBytes(StandardCharsets.UTF_8));
        return header;
    }

    @Test
    void headersMapToString_emptyHeaders_shouldReturnEmptyJsonMap() {
        ReflectionTestUtils.setField(headerUtil, "maskHeaders", Collections.emptySet());
        when(mockHeaders.iterator()).thenReturn(Collections.emptyIterator());
        String result = headerUtil.headersMapToString(mockHeaders);
        assertEquals("{}", result);
    }

    @Test
    void headersMapToString_noMaskedHeaders_shouldReturnAllHeaders() {
        ReflectionTestUtils.setField(headerUtil, "maskHeaders", Collections.emptySet());
        Header h1 = mockHeader("key1", "value1");
        Header h2 = mockHeader("key2", "value2");
        when(mockHeaders.iterator()).thenReturn(Arrays.asList(h1, h2).iterator());

        String result = headerUtil.headersMapToString(mockHeaders);
        assertTrue(result.contains("\"key1\":\"value1\""));
        assertTrue(result.contains("\"key2\":\"value2\""));
    }

    @Test
    void headersMapToString_someMaskedHeaders_shouldMaskCorrectly() {
        Set<String> maskSet = new HashSet<>(Arrays.asList("key2"));
        ReflectionTestUtils.setField(headerUtil, "maskHeaders", maskSet);

        Header h1 = mockHeader("key1", "value1");
        Header h2 = mockHeader("key2", "sensitiveValue");
        Header h3 = mockHeader("key3", "value3");
        when(mockHeaders.iterator()).thenReturn(Arrays.asList(h1, h2, h3).iterator());

        String result = headerUtil.headersMapToString(mockHeaders);
        assertTrue(result.contains("\"key1\":\"value1\""));
        assertTrue(result.contains("\"key2\":\"\"")); // Masked
        assertTrue(result.contains("\"key3\":\"value3\""));
    }

    @Test
    void headersMapToString_allMaskedHeaders_shouldMaskAll() {
        Set<String> maskSet = new HashSet<>(Arrays.asList("key1", "key2"));
        ReflectionTestUtils.setField(headerUtil, "maskHeaders", maskSet);

        Header h1 = mockHeader("key1", "sensitiveValue1");
        Header h2 = mockHeader("key2", "sensitiveValue2");
        when(mockHeaders.iterator()).thenReturn(Arrays.asList(h1, h2).iterator());

        String result = headerUtil.headersMapToString(mockHeaders);
        assertTrue(result.contains("\"key1\":\"\""));
        assertTrue(result.contains("\"key2\":\"\""));
    }
    
    @Test
    void headersMapToString_headerKeyNotInMaskSetButValueIsEmpty_shouldReturnEmptyValue() {
        ReflectionTestUtils.setField(headerUtil, "maskHeaders", Collections.singleton("maskedKey"));
        Header h1 = mockHeader("key1", ""); // Not in mask set, but empty value
        when(mockHeaders.iterator()).thenReturn(Collections.singletonList(h1).iterator());

        String result = headerUtil.headersMapToString(mockHeaders);
        assertTrue(result.contains("\"key1\":\"\""));
    }


    @Test
    void headersMapToString_jsonProcessingException_shouldReturnNull() throws Exception {
        ReflectionTestUtils.setField(headerUtil, "maskHeaders", Collections.emptySet());
        Header h1 = mockHeader("key1", "value1");
        when(mockHeaders.iterator()).thenReturn(Collections.singletonList(h1).iterator());

        // Mock ObjectMapper to throw JsonProcessingException
        ObjectMapper failingObjectMapper = mock(ObjectMapper.class);
        when(failingObjectMapper.writeValueAsString(anyMap())).thenThrow(JsonProcessingException.class);
        ReflectionTestUtils.setField(headerUtil, "objectMapper", failingObjectMapper);

        String result = headerUtil.headersMapToString(mockHeaders);
        assertNull(result);
    }
}
*/
