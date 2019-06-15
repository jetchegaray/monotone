package ar.com.autominuto.monotone.utils;

public enum AutominutoS3BucketEnv {

	dev("testing"), prod("production");
	
	private String environment;
	
	private AutominutoS3BucketEnv(String environment) {
		this.environment = environment;
	}
	
	public static AutominutoS3BucketEnv getByName(String environment){
		for(AutominutoS3BucketEnv env : values()){
			if(env.getEnvironment().equals(environment)){
				return env;
			}
		}
		return null;
	}


	public String getEnvironment() {
		return environment;
	}


	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	
}
