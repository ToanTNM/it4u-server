package vn.tpsc.it4u.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ModelMapperConfig
 */
@Configuration
public class ModelMapperConfig {

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}