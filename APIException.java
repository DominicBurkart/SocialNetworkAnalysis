package SocialNetworkAnalysis;

import twitter4j.TwitterException;

/**
 * general class to hold more specific API exceptions.
 */
@SuppressWarnings("serial")
public class APIException extends Exception {
	Exception thrower;
	String type;
	TwitterException twitterException = new TwitterException("test");

	public APIException(Exception e) {
		thrower = e;
		if (e.getClass().equals(twitterException.getClass())) {
			type = "TwitterException";
		}
	}
}