/**
 * 
 */
package com.neusoft.rnma.ichpoc.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author liangyf
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "codes")
public final class CodeSet {
	private int ok;
	private int fail;
	private int unauthorized;
	private int tokenInvalid;
	private int tokenExpired;
	private int exist;
	private int noContent;
	private int paramInvalid;
}
