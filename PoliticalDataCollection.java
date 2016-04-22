package SocialNetworkAnalysis;

import twitter4j.TwitterException;

public class PoliticalDataCollection {
	public static void main(String[] args) throws BadIDException, BadUserException, TwitterException{
		Sample s = new Sample();
		s.name = "PoliticalDataCollection";
		String[] cNames = {"Bernie Sanders", "Hillary Clinton", "Donald Trump", "Ted Cruz"};
		
		try {
			System.out.println("Collecting candidates.");
			User bernie = TwitterRequestHandler.getUser(216776631, 0);
			User hillary = TwitterRequestHandler.getUser(1339835893, 0);
			User trump = TwitterRequestHandler.getUser(25073877, 0);
			User cruz = TwitterRequestHandler.getUser(23022687, 0);
			System.out.println("Candidates collected.");
			User[] bigs = {bernie, hillary, trump, cruz};
			System.out.println();
			
			for (int i = 0; i< bigs.length; i++){
				System.out.println("Getting followers for "+cNames[i]);
				bigs[i].getFollowers();
			}
			System.out.println("Saving output");
			s.outDir = "/Users/dominicburkart/Desktop/testOut";
			s.usersToCSV();
			System.out.println("Program complete.");
		} catch (RedundantEntryException e) {}
	}
	
}
