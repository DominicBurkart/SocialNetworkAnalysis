package SocialNetworkAnalysis.Applications;

import java.util.ArrayList;

import SocialNetworkAnalysis.TwitterAuth;
import SocialNetworkAnalysis.TwitterStreamerThread;
import SocialNetworkAnalysis.Utilities;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * 
 * 
 * @author dominicburkart
 */
public class ElectionStreams {
	@SuppressWarnings("unused")
	private static TwitterAuth auth = new TwitterAuth(); //initializes authorizations
	static int i = 0;
	static int last = 0;
	
	private static void sleep(){
		if ( i / TwitterAuth.size() > last / TwitterAuth.size()){
			Utilities.sleepFor(15 * 1000 * 60);
		}
		last++;
	}
	
	public static void main(String[] args){
		String[][] streamargs = {
				{"election","election2016"},
				{"Hillary", "Clinton", "HillaryClinton", "Hillary2016", "HillaryClinton2016", "Clinton2016"},
				{"Donald","Trump", "DonaldTrump", "Trump2016", "Donald2016", "DonaldTrump2016"},
				{"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Conneticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermon", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"},
				{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"},
				{"Centipedes","Centipede"},
				{"Trumpers"},
				{"NastyWoman", "NastyWomen"},
				{"ImWithHer"},
				{"president","presidential","POTUS"},
				{"vote", "voter", "voted", "voting"},
				{"Hillbots"},
				{"JillStein", "Jill", "Stein"},
				{"GaryJohnson", "Gary", "Johnson"},
				{"Killary"},
				{"MAGA", "Make America Great Again"},
				{"rigged"},
				{"GOP", "Republican", "Republicans"},
				{"Dems", "Dem", "Democrat", "Democrats", "Democratic Party", "Democratic Nominee"},
				{"Evan McMullin", "LP", "Libertarian Party", "third party"}
		};
		String[] names = new String[streamargs.length];
		for (String[] tags : streamargs){
			String n = Utilities.cleanstring(String.valueOf(tags)).replace(" ", "_");
			if (n.length() > 30){
				n = n.substring(0, 20)+"_truncated";
			}
			names[i++] = n;
		}
		for (int i = 0 ; i < streamargs.length; i++){
			StreamThreader(streamargs[i], names[i]);
		}
	}
	
	private static Configuration getFig(){
		ArrayList<ConfigurationBuilder> figs = TwitterAuth.getConfigurationBuilders();
		return figs.get(i++ % figs.size()).build();
	}
	
	private static void StreamThreader(String[] filters, String name){
		Runnable stream = new TwitterStreamerThread(filters, getFig());
		new Thread(stream, name).start();
		sleep();
	}
}
