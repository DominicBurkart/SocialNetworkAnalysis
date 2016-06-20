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
	static int i = 0;
	
	private static void sleep(){
		try {
			Thread.sleep(1000 * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void main(String[] args){
		
		String[] queerArgs = {"queer", "queers"};
		StreamThreader(queerArgs, "queer");
		
		sleep();
		
		String[] gayArgs = {"gay", "gays"};
		StreamThreader(gayArgs, "gay");
		
		sleep();
		
		String[] lesbArgs = {"lesbian", "lesbians"};
		StreamThreader(lesbArgs, "lesbian");
		
		sleep();
		
		String[] biapArgs = {"bisexual", "bisexuals", "bi", "pan", "pansexual", "pansexuals", "intersex", "asexual", "asexuals"};
		StreamThreader(biapArgs, "biap");
		
		sleep();
		
		String[] transArgs = {"trans", "transgender", "transsexual", "trans*", "transman", "transmen", "transwoman", "transwomen", "nonbinary", "non binary", "non-binary"};
		StreamThreader(transArgs, "trans");
		
		sleep();
		
		String[] slurArgs = {"faggot", "faggots", "fag", "fags", "tranny", "trannies", "shemale", "shemales", "dyke", "dykes"};
		StreamThreader(slurArgs, "slurs");
		
		sleep();
		
		String[] orlArgs = {"orlando", "pulse", "orlandoshooting", "orlandounited", "orlando survivor", "pulse orlando"};
		StreamThreader(orlArgs, "orlando");
		
		sleep();
		
		String[] homargs = {"homosexual", "homosexuals", "homo", "homos"};
		StreamThreader(homargs, "homosexual");
	}
	
	private static Configuration getFig(){
		ArrayList<ConfigurationBuilder> figs = TwitterAuth.getConfigurationBuilders();
		return figs.get(i++ % figs.size()).build();
	}
	
	private static void StreamThreader(String[] filters, String name){
		Runnable stream = new TwitterStreamerThread(filters, getFig());
		new Thread(stream, name).start();
	}
}
