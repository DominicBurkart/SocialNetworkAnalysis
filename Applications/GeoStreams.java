package SocialNetworkAnalysis.Applications;

import java.util.ArrayList;

import SocialNetworkAnalysis.TwitterAuth;
import SocialNetworkAnalysis.Utilities;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class GeoStreams {
	static int i = 0;
	static int last = 0;
	static int l;
	
	private static Configuration getFig(){
		ArrayList<ConfigurationBuilder> figs = TwitterAuth.getConfigurationBuilders();
		l = figs.size();
		return figs.get(i++ % figs.size()).build();
	}
	
	private static void geoStream(double[][] filters, String name){
		Runnable stream = new GeoStream(filters, getFig());
		new Thread(stream, name).start();
		sleep();
	}
	
	private static void sleep(){
		if ( last >= l ){
			last = 0;
			Utilities.sleepFor(15 * 1000 * 60);
		}
		last++;
	}
	
	public static void main(String[] args){
		double[][][] streamargs = {
				new double[][]{
	                new double[]{-84.445026, 33.967426},
	                new double[]{-75.178897, 36.623241}
				},
				new double[][]{
	                new double[]{-85.061575, 38.470708},
	                new double[]{-80.296411, 42.368139}
				},
				new double[][]{
	                new double[]{-120.395612, 35.305075},
	                new double[]{-113.680371, 42.247648}
				}
		};
		String[] names = new String[streamargs.length];
		names[0] = "North_Carolina";
		names[1] = "Ohio";
		names[2] = "New Mexico";
		for (int i = 0 ; i < streamargs.length; i++){
			geoStream(streamargs[i], names[i]);
		}
	}
}
