package SocialNetworkAnalysis.Applications;

import java.util.ArrayList;

import SocialNetworkAnalysis.TwitterAuth;
import SocialNetworkAnalysis.Utilities;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class FranceStreams{
	static int i = 0;
	static int last = 0;
	static int l;
	
	private static Configuration getFig(){
		ArrayList<ConfigurationBuilder> figs = TwitterAuth.getConfigurationBuilders();
		l = figs.size();
		return figs.get(i++ % l).build();
	}
	
	private static void geoStream(double[][] filters, String name){
		Runnable stream = new GeoStream(filters, getFig(), name);
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
	                new double[]{-13.042925, 44.963021},
	                new double[]{-12.597621, 45.346487}
				},
				new double[][]{
	                new double[]{-21.640858, 55.100158},
	                new double[]{-20.620760, 55.925759}
				},
				new double[][]{
	                new double[]{14.338969, -61.273137},
	                new double[]{14.936451, -60.710197}
				},
				new double[][]{
	                new double[]{15.784993, -61.866502},
	                new double[]{16.590377, -60.913027}
				},
				new double[][]{
	                new double[]{2.098909, -54.583087},
	                new double[]{5.851318, -51.376433}
				},
				new double[][]{
	                new double[]{41.270549, 8.454926},
	                new double[]{43.107318, 9.722735}
				},
				new double[][]{
	                new double[]{-5.121045, 42.120278},
	                new double[]{8.850687, 51.492968}
				}
		};
		String[] names = new String[streamargs.length];
		names[0] = "Mayotte";
		names[1] = "Reunion";
		names[2] = "Martinique";
		names[3] = "Guadeloupe";
		names[4] = "Guyane";
		names[5] = "Corse";
		names[6] = "Metropolitain";
		
		for (int i = 0 ; i < streamargs.length; i++){
			geoStream(streamargs[i], names[i]);
		}
	}
}
