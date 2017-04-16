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
public class Inaug_Streams {
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
				{"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Conneticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermon", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"},
				{"election","election2016"},
				{"Hillary", "Clinton", "HillaryClinton", "Hillary2016", "HillaryClinton2016", "Clinton2016"},
				{"Donald","Trump", "DonaldTrump", "Trump2016", "Donald2016", "DonaldTrump2016"},
				{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"},
				{"NastyWoman", "NastyWomen"},
				{"ImWithHer"},
				{"president","presidential","POTUS"},
				{"vote", "voter", "voted", "voting"},
				{"womensmarch", "march", "WomensMarchonWashington", "millionwomenmarch"},
				{"protest"},
				{"inauguration", "trumpinauguration"},
				{"United States","USA", "America", "Americans"},
				{"Resistance"}
		};
		String[] names = new String[streamargs.length];
		for (String[] tags : streamargs){
			names[i++] = Utilities.strFromAr(tags);
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
