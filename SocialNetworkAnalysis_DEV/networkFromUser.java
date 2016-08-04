package SocialNetworkAnalysis.SocialNetworkAnalysis_DEV;

import java.util.Date;

import SocialNetworkAnalysis.*;

public class networkFromUser extends TwitterSample {
		// goal and depth must be > 0.
		static final int DEPTH = 2;
		static int goal = 500; // max collectable followers per user with this algorithm is 5k
		static User root;
		static String rootID;
		
		public networkFromUser(String rootID) {
			super(rootID+"_twitterNetwork", "/Volumes/Burkart/files/current_twitter_collection");
		}
		
		public static void main(String[] args) {
			System.out.println("networkFromUser main is running!");
			Date d = new Date();
			try{
				Double.parseDouble(args[0]); //throws numberFormatException when given bad input
				if (args.length > 1){
					try{
						goal = Integer.parseInt(args[1]);
					} catch (NumberFormatException e){
						System.err.println("Invalid second argument passed to networkFromUser. Expected an integer for the maximum number of followers to collect from the root.");
						System.exit(1);
					}
				}
				rootID = args[0];
				networkFromUser n = new networkFromUser(rootID);
				n.go();
				d =  new Date();
				System.out.println("Program complete. Current time: "+d.toString());
			} catch (NumberFormatException e){
				System.err.println("Invalid first argument passed to networkFromUser. Expected a twitter ID (a large integer).");
				System.exit(1);
			} catch (IndexOutOfBoundsException e){
				System.err.println("No arguments passed to networkFromUser. Expected a twitterID (a large integer).");
			}

		}

		/**
		 * Tells the iterative collector to get Friends instead of Followers!
		 */
		@Override
		public ToUser getFol(ToFollow u) {
			return getFriends(u);
		}

		/**
		 * conditions under which we will apply userAction.
		 * 
		 * Returns true when conditions have been met.
		 * 
		 * This userConditions additionally adds every user to
		 * the getPostsQ, so we have a sample of tweets from each
		 * user.
		 */
		@Override
		public boolean userConditions(User u) {
			if (!u.fromFromPost) this.getPostsQ.add((TwitterUser) u);
			if (u != null && u.firstDepth < DEPTH && collected < goal && u != root && !u.fromPost){
				if (verbose) System.out.println("User included for userAction: "+u);
				collected++;
				return true;
			}
			else{
				try{
					if (verbose) System.out.println("User excluded from userAction: "+u);
				} catch (NullPointerException e){
					if (verbose) System.out.println("Null value passed to userConditions and exempted from userActions.");
				}
				return false;
			}
		}

		/**
		 * what to do with a user who has been collected.
		 * 
		 * In this case, tell the program to collect their followers and some posts
		 * if they pass userConditions.
		 */
		@Override
		public void userAction(User user) {
			ToFollow toFol = new ToFollow(user);
			this.getFollowingQ.add(toFol);
		}

		/**
		 * decides whether or not we apply followAction to these ids.
		 */
		@Override
		public boolean followingConditions(ToUser ids) {
			if (ids != null)
				return true;
			else
				return false;
		}

		/**
		 * followAction defines what to do with the ids we collected. In this case,
		 * we turn them into full user objects by adding them to the getUser queue.
		 */
		@Override
		public void followAction(ToUser ids) {
			if (verbose){
				System.out.print("networkFromUser followAction input: ");
				if (ids.single){
					System.out.print("ToUser is single. ");
					System.out.print("id: "+ids.id);
				}
				else{
					System.out.print("ToUser holds multiple values. ");
					for (String id : ids.ids){
						System.out.print(id+" ");
					}
				}
				System.out.println("\n");
			}
			if (ids.ids.length < 100) {
				getUserQ.add(ids);
			} else { 
				Utilities.toUserChunker(ids);
			}
		}

		@Override
		public void start() {
			if (verbose) System.out.println("Starting data collection in networkFromUser start()!\n");
			ToUser rootToUser = new ToUser(rootID, 0);
			root = getUser(rootToUser);
			ToFollow f = new ToFollow(root);
			getUserQ.add(super.getSomeFol(f, goal)); //actual getSomeFollowers, where the getFollowers in this class actually gets friends.
			getPostsQ.add((TwitterUser) root);
			if (verbose) System.out.println("start() completed.");
		}
}
