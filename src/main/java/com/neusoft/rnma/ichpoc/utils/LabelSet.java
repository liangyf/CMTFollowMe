package com.neusoft.rnma.ichpoc.utils;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author liangyf
 *
 */

@Data
@Component
@ConfigurationProperties
public final class LabelSet {

	private Map<String, String> mrs;
}
