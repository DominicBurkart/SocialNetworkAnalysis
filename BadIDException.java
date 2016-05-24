package SocialNetworkAnalysis;

/**
 * Denotes that an ID could not be queried via
 * the given site's API.
 * 
 * @author dominicburkart
 */
@SuppressWarnings("serial")
public class BadIDException extends Exception {
	public BadIDException(String m) {
		super(m);
	}
}
