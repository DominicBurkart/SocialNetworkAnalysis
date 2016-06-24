package SocialNetworkAnalysis.Applications;

import java.util.ArrayList;

import SocialNetworkAnalysis.TwitterAuth;
import SocialNetworkAnalysis.TwitterStreamerThread;
import SocialNetworkAnalysis.Utilities;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Simultaneously collects data from queer, lgbia, trans,
 * and queerphobic slur streams. It looks like Twitter 
 * limits us to two logins using the same app/user
 * credentials.
 * 
 * @author dominicburkart
 */
public class QueerMultiStream {
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
		String[] queerArgs = {"queer", "queers"};
		StreamThreader(queerArgs, "queer");
		
		String[] gayArgs = {"gay", "gays"};
		StreamThreader(gayArgs, "gay");
		
		String[] lesbArgs = {"lesbian", "lesbians"};
		StreamThreader(lesbArgs, "lesbian");
		
		String[] biapArgs = {"bisexual", "bisexuals", "pansexual", "pansexuals", "intersex", "asexual", "asexuals"};
		StreamThreader(biapArgs, "biap");
		
		String[] transArgs = {"trans", "transgender", "transsexual", "transsexuals", "trans*", "transman", "transmen", "transwoman", "transwomen", "nonbinary", "non binary", "non-binary"};
		StreamThreader(transArgs, "trans");
		
		String[] slurHomArgs = {"faggot", "faggots", "fag", "fags", "tranny", "trannies", "shemale", "shemales", "dyke", "dykes", "homosexual", "homosexuals", "homo", "homos"};
		StreamThreader(slurHomArgs, "slurs + \"homosexual\"");
		
		String[] orlArgs = {"orlando", "pulse", "orlandoshooting", "orlandounited", "orlando survivor", "pulse orlando"};
		StreamThreader(orlArgs, "orlando");
		
		String[] prideArgs = {"pride", "pride2016"};
		StreamThreader(prideArgs, "pride");
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
