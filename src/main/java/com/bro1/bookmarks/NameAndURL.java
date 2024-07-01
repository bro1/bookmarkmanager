package com.bro1.bookmarks;

import java.util.Objects;

public class NameAndURL {
	public NameAndURL(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(url);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NameAndURL other = (NameAndURL) obj;
		return Objects.equals(url, other.url);
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
