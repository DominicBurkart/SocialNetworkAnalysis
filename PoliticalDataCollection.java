package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.HashSet;

import twitter4j.TwitterException;

public class PoliticalDataCollection {
	static Sample s;
	
	public static void main(String[] args) throws BadIDException, BadUserException, TwitterException{
		s = new Sample();
		s.name = "PoliticalDataCollection";
		
		try {
			System.out.println("Collecting candidates.");
			User bernie = TwitterRequestHandler.getUser(216776631, 0);
			System.out.println(bernie);
			User hillary = TwitterRequestHandler.getUser(1339835893, 0);
			System.out.println(hillary);
			User trump = TwitterRequestHandler.getUser(25073877, 0);
			System.out.println(trump);
			User cruz = TwitterRequestHandler.getUser(23022687, 0);
			System.out.println(cruz);
			System.out.println("Candidates collected.");
			User[] bigs = {bernie, hillary, trump, cruz};
			HashSet<User> firstFollows = new HashSet<User>();
			
			for (User big : bigs){
				System.out.println("\nGetting followers for "+big.username);
				ArrayList<User> fols = big.getSomeFollowers();
				firstFollows.addAll(fols);
				System.out.println(big.username+" followers:");
				for (User fol: fols){
					System.out.println(fol);
				}
			}
			for (User small : firstFollows){
				System.out.println("Getting followers for "+small.username);
				small.getSomeFollowers();
			}
			System.out.println("Saving output");
			s.usersToCSV();
			System.out.println("Program complete.");
		}
		catch (RedundantEntryException e) {}
		catch (TwitterException e){
			System.err.println("exception: twitter error #"+e.getErrorCode()+": "+e.getCause());
			System.out.println("Soft quitting.");
			s.usersToCSV();
		}
	}
}
