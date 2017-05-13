package SocialNetworkAnalysis.Applications;

import java.util.ArrayList;

import SocialNetworkAnalysis.TwitterAuth;
import SocialNetworkAnalysis.Utilities;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * 
 * Collects geotagged twitter data from the US and France.
 * 
 * @author Dominic Burkart
 * 
 * @since 13 May 2017
 *
 */
public class FrUSgeo{
	static int i = 0;
	static int last = 0;
	static int l;
	
	private static Configuration getFig(){
		ArrayList<ConfigurationBuilder> figs = TwitterAuth.getConfigurationBuilders();
		l = figs.size();
		return figs.get(i++ % l).build();
	}
	
	private static void geoStream(double[][] filters, String name){
		Runnable stream = new GeoStream(filters, getFig(), name, true);
		new Thread(stream, name).start();
		sleep();
	}
	
	private static void sleep(){
		if ( last >= l ){
			last = 0;
			Utilities.sleepFor(15 * 1000 * 60); //conservative pause length to avoid ratelimit. If ratelimit is reached, the tokens are compromised, multiple instances of the application have been run simulatenously/in quick succession, or the program has been modified to open more streams than twitter allows per token. 
		}
		last++;
	}
	
	public static void main(String[] args){
		
		//the streamargs array holds the geodata queries. Multiple locations can be grouped together into a single twitter stream.
		//when adding arguments, note that lat and longitude are flipped for twitter.
		//these bounding boxes include a lot of populated areas outside of the target state. Parse them in some way while analyzing collected data.
		double[][][] streamargs = { 
				new double[][]{ //france (including outer departments)
	                new double[]{-5.121045, 42.120278},  //metro
	                new double[]{8.850687, 51.492968},
	                new double[]{44.963021, -13.042925}, //mayotte
	                new double[]{45.346487, -12.597621}, 
	                new double[]{55.100158, -21.640858}, //reunion
	                new double[]{55.925759, -20.620760}, 
	                new double[]{-61.273137, 14.338969}, //martinique
	                new double[]{-60.710197, 14.936451},
	                new double[]{-61.866502, 15.784993}, //guadeloupe
	                new double[]{-60.913027, 16.590377},
	                new double[]{-54.583087, 2.098909},  //guyane
	                new double[]{-51.376433, 5.851318},
	                new double[]{8.454926, 41.270549},   //corse
	                new double[]{9.722735, 43.107318}
				},
				new double[][]{ //US (including inhabited territories)
					new double[]{-130.043550, 23.185154},   //continuous
					new double[]{-65.473555, 47.511249},
					new double[]{170.921485, 51.629305},    //alaskan islands in the eastern hemisphere
					new double[]{180, 55.449763},
					new double[]{-180, 51.311683},			//alaskan mainland
					new double[]{-128.938767, 71.769096},
					new double[]{-161.294273, 18.479234},   //hawaii
					new double[]{-153.354184, 22.917382},
					new double[]{-67.432406, 17.662763},    //puerto rico
					new double[]{-65.414789, 18.728939},
					new double[]{-65.132601, 17.562792},    //virgin islands
					new double[]{-64.620435, 18.468351},
					new double[]{144.596631, 13.182905},    //guam + northern mariana islands
					new double[]{146.685511, 19.416646},
					new double[]{-170.905258, -14.527961},  //american samoa
					new double[]{-169.366485, -14.046022}
				}
		};
		
		//The names array holds the names of the geodata queries.
		String[] names = new String[streamargs.length];
		//names[x] = "Whatever name of whatever group corresponds with the 2d list at index x of streamargs";
		names[0] = "France";
		names[1] = "USA";
		
		for (int i = 0 ; i < streamargs.length; i++){
			geoStream(streamargs[i], names[i]);
		}
	}
}
