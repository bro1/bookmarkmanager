package com.bro1.bookmarks;

public class NameAndURL {
	public NameAndURL(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}
	public String name;
	public String url;
	@Override
	public String toString() {
		
		if (name != null && !name.isBlank()) {
			return name + " - " + url;	
		}
		return url;
	}
	
	
	
}
