package com.org.lob.project.repository;

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
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
@CacheConfig(cacheNames = "files")
public class SimpleFileRepository implements FileRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFileRepository.class);

	@Override
	@Cacheable
	public String load(String project) {
		return asString(new FileSystemResource(project));
	}

	@Override
	@CachePut
	public String reLoad(String project) {
		return asString(new FileSystemResource(project));
	}

	private String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
        	LOGGER.error("Error Proessing ", e);
            throw new UncheckedIOException(e);
        }
    }
}
