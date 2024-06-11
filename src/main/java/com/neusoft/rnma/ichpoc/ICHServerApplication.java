/**
 * 
 */
package com.neusoft.rnma.ichpoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author liangyf
 *
 */
@SpringBootApplication
@ServletComponentScan
@EnableCaching
public class ICHServerApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ICHServerApplication.class, args);
		System.out.println("   ___   ___      ___                ");
		System.out.println("      / /      / /   ) )   / /   / / ");
		System.out.println("     / /      / /         / /___/ /  ");
		System.out.println("    / /      / /         /  ___  /   ");
		System.out.println("   / /      / /         / /   / /    ");
		System.out.println("__/ /___   ( (____/ /  / /   / /    version 0.0.1-SNAPSHOT");
	}

}
