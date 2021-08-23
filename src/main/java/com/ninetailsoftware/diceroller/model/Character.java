package com.ninetailsoftware.diceroller.model;

import java.util.HashSet;

public class Character {
	private String name;
	private String type;
	private HashSet<String> status = new HashSet<String>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public HashSet<String> getStatus() {
		return status;
	}
}
