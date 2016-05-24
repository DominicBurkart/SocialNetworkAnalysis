package SocialNetworkAnalysis;

/**
 * Denotes that a user could not be queried via
 * the given site's API.
 * 
 * @author dominicburkart
 */
@SuppressWarnings("serial")
public class BadUserException extends Exception {
	public BadUserException(String m) {
		super(m);
	}
}
