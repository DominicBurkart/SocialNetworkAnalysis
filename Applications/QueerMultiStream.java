package SocialNetworkAnalysis.Applications;

import java.util.ArrayList;

import SocialNetworkAnalysis.TwitterAuth;
import SocialNetworkAnalysis.TwitterStreamerThread;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Simultaneously collects data from queer, lgbia, trans,
 * and queerphobic slur streams. 
 * 
 * @author dominicburkart
 */
public class QueerMultiStream {
	@SuppressWarnings("unused")
	private static TwitterAuth auth = new TwitterAuth(); //initializes authorizations
	private static ArrayList<ConfigurationBuilder> figs = TwitterAuth.getConfigurationBuilders();
	static int i = 0;
	
	
	public static void main(String[] args){
		String[] queerArgs = {"queer"};
		StreamThreader(queerArgs, "queer");
		
		String[] lgbiaArgs = {"gay", "lesbian", "bisexual", "pansexual", "intersex", "asexual"};
		StreamThreader(lgbiaArgs, "lgbia");
		
		String[] transArgs = {"trans", "transgender", "transsexual", "trans*"};
		StreamThreader(transArgs, "trans");
		
		String[] slurArgs = {"faggot", "faggots", "fag", "fags", "tranny"};
		StreamThreader(slurArgs, "slurs");
	}
	
	private static Configuration getFig(){
		return figs.get(i++ % figs.size()).build();
	}
	
	private static void StreamThreader(String[] filters, String name){
		Runnable stream = new TwitterStreamerThread(filters, getFig());
		new Thread(stream, name).start();
	}
}
