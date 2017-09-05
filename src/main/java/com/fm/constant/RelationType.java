package com.fm.constant;

public enum RelationType {
	
	FRIEND(1),
	SUBSCRIBE(2);
	
	private int value;
	
	RelationType(int value){
		this.value = value;
	}
	
	public int value(){
		return this.value;
	}

}
