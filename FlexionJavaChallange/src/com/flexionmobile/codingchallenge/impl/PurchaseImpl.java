package com.flexionmobile.codingchallenge.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexionmobile.codingchallenge.integration.Purchase;

public class PurchaseImpl implements Purchase{
	@JsonProperty("id")
	String id;
	@JsonProperty("consumed")
	boolean consumed;
	@JsonProperty("itemId")
	String itemId;
	@Override
	public boolean getConsumed() {
		// TODO Auto-generated method stub
		return consumed;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String getItemId() {
		// TODO Auto-generated method stub
		return itemId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	

}
