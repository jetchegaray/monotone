package ar.com.autominuto.monotone.utils;

public enum BucketsType {
	
	COMMERCES ("commerces"), USERS ("users"), PRODUCTS ("products"), COUPONS("coupons");

	
	private BucketsType(String name) {
		this.name = name;
	}
	
	private String name;

	public String getName() {
		return name;
	}
	
	public static BucketsType getByName(String name){
		for(BucketsType type : values()){
			if(type.getName().equals(name)){
				return type;
			}
		}
		return null;
	}
}

