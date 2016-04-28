package SocialNetworkAnalysis;

@SuppressWarnings("serial")
public class APIException extends Exception {
	Exception thrower;
	public APIException(Exception e){
		e = thrower;
	}
}