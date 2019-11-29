package com.flexionmobile.codingchallenge.impl;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.flexionmobile.codingchallenge.integration.Integration;
import com.flexionmobile.codingchallenge.integration.Purchase;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class IntegrationImpl implements Integration{
	private WebResource resource = null;
	 private  Client client = null;
	 private WebResource.Builder builder;
	 private String restAPIPrefix="http://sandbox.flexionmobile.com/javachallenge/rest/developer/nitish" ;
	 Logger log = Logger.getLogger(IntegrationImpl.class);
	 private ObjectMapper objectMapper ;
	 
	@Override
	public Purchase buy(String itemID) {
		PurchaseImpl purchaseResp = null;
		try {
			
			if(itemID != null && !itemID.isEmpty()) {
				// Call Buy REST API
				  resource = client.resource(restAPIPrefix+"/buy/"+itemID);
				  builder = resource.accept(MediaType.APPLICATION_JSON);
				  ClientResponse clientResponse = builder.post(ClientResponse.class);
				  if(clientResponse != null && clientResponse.getStatus() == 200) {
					  String jsonResp = clientResponse.getEntity(String.class);
					  purchaseResp =  objectMapper.readValue(jsonResp, PurchaseImpl.class);
					  log.info("Item purchase successfull for " + itemID + " purchase id is " +  purchaseResp.getId());
				  } else {
					  log.info("Item purchase not successfull");
					  if(clientResponse != null) {
						  log.info("Error " + clientResponse.getEntity(String.class));
					  }
				  }
			 }else {
				 log.info("Empty itemID");
				 return null;
			 }
		}catch (Exception e) {
			log.error("Exception while buying " + e.getMessage());
			e.printStackTrace();
		}
		return purchaseResp;
	}

	@Override
	public void consume(Purchase purchase) {
		
		try {
			
			if(purchase != null	&& purchase.getId() != null && !purchase.getId().isEmpty()) {
				// Call Consume REST API
				  resource = client.resource(restAPIPrefix+"/consume/"+purchase.getId());
				  builder = resource.accept(MediaType.APPLICATION_JSON);
				  ClientResponse clientResponse = builder.post(ClientResponse.class);
				  if(clientResponse != null && clientResponse.getStatus() == 200) {
					  log.info("Consumed one purchase " + purchase.getId());
				  } else {
					  log.info("Not able to consume ");
					  if(clientResponse != null) {
						  log.info("Error " + clientResponse.getEntity(String.class));
					  }
				  }
			 }else {
				 log.info("Missing purchase");
			 }
		}catch (Exception e) {
			log.error("Exception while consuming purchase " + e.getMessage());
			e.printStackTrace();
		}

		
	}

	@Override
	@JsonProperty("purchase")
	public List<Purchase> getPurchases() {
		List<Purchase> purchases = null;
		try{
			//Call Purchases REST API
			 resource = client.resource(restAPIPrefix+"/all");
			  builder = resource.accept(MediaType.APPLICATION_JSON);
			  ClientResponse clientResponse = builder.get(ClientResponse.class);
			  if(clientResponse != null && clientResponse.getStatus() == 200) {
				  String jsonResp = clientResponse.getEntity(String.class);
				  final JsonNode jsonNode = objectMapper.readTree(jsonResp);
				  final ArrayNode responseArray = (ArrayNode) jsonNode.get("purchases");
				  purchases = objectMapper.convertValue(responseArray,  new TypeReference<List<PurchaseImpl>>(){});
				  log.info("Total no of purchases " + purchases.size());
			  } else {
				  log.info("No purchases response");
				  if(clientResponse != null) {
					  log.info("Error " + clientResponse.getEntity(String.class));
				  }
			  }
			  
		}catch (Exception e) {
			log.error("Exception while fetching purchases " + e.getMessage());
			e.printStackTrace();
		}
		return purchases;
	}

	public IntegrationImpl() {
		// Initialize REST API client 
		objectMapper = new ObjectMapper();
		 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		 ClientConfig clientConfig = new DefaultClientConfig();
		 clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		  client = Client.create(clientConfig);   	// Create REST webservice client
		  client.setConnectTimeout(60000); // 1 min timeout
		  
	}
}
