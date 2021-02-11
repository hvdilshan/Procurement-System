package com.procument.model;

import java.util.ArrayList;

public class PageManufactorer<T> {

	public double itemCount(ArrayList<T> list) {
		int count = 0;
		
		for(T item : list) {
			count++;
		}
		
		double pageCount = count / 10.0;
		pageCount = Math.ceil(pageCount);
		
		return pageCount;
	}
	
	
	
}
