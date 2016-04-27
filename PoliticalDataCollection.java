package SocialNetworkAnalysis;

import java.util.HashSet;

import twitter4j.TwitterException;

public class PoliticalDataCollection {
	static Sample s;
	
	public static void main(String[] args) throws BadIDException, BadUserException, TwitterException{
		s = new Sample();
		s.name = "PoliticalDataCollection";
		String[] cNames = {"Bernie Sanders", "Hillary Clinton", "Donald Trump", "Ted Cruz"};
		
		try {
			System.out.println("Collecting candidates.");
			User bernie = TwitterRequestHandler.getUser(216776631, 0);
			System.out.println(bernie.toString());
			User hillary = TwitterRequestHandler.getUser(1339835893, 0);
			System.out.println(hillary.toString());
			User trump = TwitterRequestHandler.getUser(25073877, 0);
			System.out.println(trump.toString());
			User cruz = TwitterRequestHandler.getUser(23022687, 0);
			System.out.println(cruz.toString());
			System.out.println("Candidates collected.");
			User[] bigs = {bernie, hillary, trump, cruz};
			System.out.println();
			HashSet<User> firstFollows = new HashSet<User>();
			
			for (int i = 0; i< bigs.length; i++){
				System.out.println("Getting followers for "+cNames[i]);
				firstFollows.addAll(bigs[i].getSomeFollowers());
			}
			for (User small : firstFollows){
				System.out.println("Getting followers for "+small.username);
				small.getFollowers();
			}
			System.out.println("Saving output");
			s.outDir = "/Users/dominicburkart/Desktop/testOut";
			s.usersToCSV();
			System.out.println("Program complete.");
		} catch (RedundantEntryException e) {}
	}
}
