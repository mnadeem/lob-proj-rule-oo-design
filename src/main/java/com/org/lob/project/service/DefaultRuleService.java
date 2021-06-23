package com.org.lob.project.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
@CacheConfig(cacheNames = "rules")
public class DefaultRuleService implements RuleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleService.class);

	@Override
	@Cacheable
	public String load(String project) {
		return asString(new FileSystemResource(project));
	}

	public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
        	LOGGER.error("Error Proessing ", e);
            throw new UncheckedIOException(e);
        }
    }

	@Override
	@CachePut
	public String reLoad(String project) {
		return asString(new FileSystemResource(project));
	}
}
