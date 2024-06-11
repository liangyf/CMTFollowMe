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
@ConfigurationProperties(prefix = "enum")
public final class EnumSet {

	private String normal;
	private String abnormal;
	private String abnormalLittle;
	private String abnormalSevere;
	private String high;
	private String low;
	private String sourceSp;
	private String sourceIvi;
	private String sourceHome;
	private String tired;
	private String tiredEasily;
	private String tiredNo;
	private String tiredLittle;
	private String tiredMedium;
	private String tiredSevere;
	private String bpTerminate;
	private String bpLittle;
	private String bpMedium;
	private String bpSevere;
	private String brHigh;
	private String brLow;
	private String hrLow;
	private String hrHigh;
}
