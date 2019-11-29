package com.flexionmobile.codingchallenge.main;

import org.apache.log4j.Logger;

import com.flexionmobile.codingchallenge.impl.IntegrationImpl;
import com.flexionmobile.codingchallenge.integration.IntegrationTestRunner;

public class IntegrationMain {
	
	static Logger log = Logger.getLogger(IntegrationMain.class);
	
	public static void main(String args[]) {
		try{
			
			IntegrationImpl integrationImpl = new IntegrationImpl();
			IntegrationTestRunner integrationTestRunner = new IntegrationTestRunner();
			integrationTestRunner.runTests(integrationImpl);
			
		}catch (Exception e) {
			log.info("Exception " + e.getMessage());
			e.printStackTrace();
		}
	}
}
