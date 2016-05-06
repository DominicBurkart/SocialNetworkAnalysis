package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import twitter4j.TwitterException;

/**
 * For Electoral Network Analysis Project
 * 
 * @author dominicburkart
 */
public class PoliticalDataCollection {
	static Sample s;

	public static void main(String[] args) throws BadIDException, BadUserException, TwitterException {
		s = new TwitterSample();
		s.name = "PoliticalDataCollection";
		Scanner in = new Scanner(System.in);
		System.out.println("Number of followers to collect from each candidate: ");
		int firstnum = in.nextInt();
		System.out.println("Number of followers to collect from each non-candidate user: ");
		int secondnum = in.nextInt();
		System.out.println(firstnum + " followers will be collected from each user.");
		in.close();

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
				ArrayList<User> fols = big.getxFollowers(firstnum);
				firstFollows.addAll(fols);
				System.out.println("\n"+big.username + " followers:");
				for (User fol : fols) {
					fol.tensors.add(new Follow(fol, big));
					System.out.println(fol);
				}
			}
			for (User smol : firstFollows) { //people who follow main candidates
				System.out.println("Getting followers for " + smol.username);
				ArrayList<User> smollests = smol.getxFollowers(secondnum);
				System.out.println("\n"+smol.username + " followers:");
				for (User smollest : smollests){ //people who follow people who follow main candidates
					smollest.tensors.follows.add(new Follow(smollest, smol));
					System.out.println(smollest);
				}
			}
			System.out.println("Saving output");
			s.usersToTSV();
			System.out.println("Program complete.");
		} catch (RedundantEntryException e) {
		} catch (APIException e) {
			if (!e.type.equals("TwitterException")) {
				System.err.println("APIException incorrectly typed.");
			}
			TwitterException t = (TwitterException) e.thrower;
			System.err.println("exception: twitter error #" + t.getErrorCode() + ": " + t.getErrorMessage()
					+ "\n Saving and quitting.");
			s.usersToTSV();
		}
	}
}
