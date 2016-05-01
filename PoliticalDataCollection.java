package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.HashSet;

import twitter4j.TwitterException;

public class PoliticalDataCollection {
	static Sample s;

	public static void main(String[] args) throws BadIDException, BadUserException, TwitterException {
		s = new Sample();
		s.name = "PoliticalDataCollection";

		try {
			System.out.println("Collecting candidates.\n");
			User bernie = TwitterRequestHandler.getUser(216776631, 0);
			System.out.println(bernie);
			User hillary = TwitterRequestHandler.getUser(1339835893, 0);
			System.out.println(hillary);
			User trump = TwitterRequestHandler.getUser(25073877, 0);
			System.out.println(trump);
			User cruz = TwitterRequestHandler.getUser(23022687, 0);
			System.out.println(cruz);
			System.out.println("\nCandidates collected.\n");
			User[] bigs = { bernie, hillary, trump, cruz };
			HashSet<User> firstFollows = new HashSet<User>();

			for (User big : bigs) {
				System.out.println("\nGetting followers for " + big.username);
				ArrayList<User> fols = big.getSomeFollowers();
				firstFollows.addAll(fols);
				System.out.println(big.username + " followers:");
				for (User fol : fols) {
					System.out.println(fol);
				}
			}
			for (User smol : firstFollows) {
				System.out.println("Getting followers for " + smol.username);
				smol.getSomeFollowers();
			}
			System.out.println("Saving output");
			s.usersToCSV();
			System.out.println("Program complete.");
		} catch (RedundantEntryException e) {
		} catch (APIException e) {
			if (!e.type.equals("TwitterException")) {
				System.err.println("APIException incorrectly typed.");
			}
			TwitterException t = (TwitterException) e.thrower; // since this is
																// just twitter
																// data we can
																// do this cast.
			System.err.println("exception: twitter error #" + t.getErrorCode() + ": " + t.getErrorMessage()
					+ "\n Saving and quitting.");
			s.usersToCSV();
		}
	}
}
