package SocialNetworkAnalysis;

@SuppressWarnings("serial")
public class RedundantEntryException extends BadUserException {
	User user;

	public RedundantEntryException(String m) {
		super(m);
	};

	public RedundantEntryException(String m, User u) {
		super(m);
		user = u;
	}
}
