package SocialNetworkAnalysis;


import twitter4j.TwitterException;

/** 
 * For getting a set of Hillary's twitter followers.
 * @author dominicburkart
 */
public class HillaryFollowers {
	static Sample s;

	public static void main(String[] args) throws BadIDException, BadUserException, TwitterException {
		s = new TwitterSample();
		s.name = "HillaryFollowers";

		try {
			User hillary = TwitterRequestHandler.getUser(1339835893, 0);
			System.out.println(hillary);
			hillary.getxFollowers(50000);
			System.out.println("Saving output");
			s.usersToCSV();
			System.out.println("Program complete.");
		} catch (RedundantEntryException e) {
		} catch (APIException e) {
			if (!e.type.equals("TwitterException")) {
				System.err.println("APIException incorrectly typed.");
			}
			TwitterException t = (TwitterException) e.thrower;
			System.err.println("exception: twitter error #" + t.getErrorCode() + ": " + t.getErrorMessage()
					+ "\n Saving and quitting.");
			s.usersToCSV();
		}
	}
}
