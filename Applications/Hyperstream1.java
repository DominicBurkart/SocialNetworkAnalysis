package SocialNetworkAnalysis.Applications;

import java.util.ArrayList;

import SocialNetworkAnalysis.TwitterAuth;
import SocialNetworkAnalysis.TwitterStreamerThread;
import SocialNetworkAnalysis.Utilities;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * 
 * Collects:
 * 	geotagged twitter data from the US and France
 * 	LGBTQ+ keywords
 * 	homophobic language
 * 	islam keywords
 *  terrorism keywords
 *  2017-relevant social movement terms (blm, nobannowall)
 *  healthcare
 * 	
 * @author Dominic Burkart
 * 
 * @since 13 May 2017
 *
 */
public class Hyperstream1{
	static int i = 0;
	static int last = 0;
	static ArrayList<ConfigurationBuilder> figs = TwitterAuth.getConfigurationBuilders();
	static int l = figs.size();
	
	private static Configuration getFig(){
		return figs.get(i++ % l).build();
	}
	
	private static void geoStream(double[][] filters, String name){
		Runnable stream = new GeoStream(filters, getFig(), name);
		new Thread(stream, name).start();
		sleep();
	}
	
	private static void streamThreader(String[] filters, String name){
		Runnable stream = new TwitterStreamerThread(filters, getFig(), name);
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
		
		System.out.println("figs length: "+figs.size());
		
		//the locargs array holds the geodata queries. Multiple locations can be grouped together into a single twitter stream.
		//when adding arguments, note that lat and longitude are flipped for twitter.
		//these bounding boxes include a lot of populated areas outside of the target state. Parse them in some way while analyzing collected data.
		double[][][] locargs = { 
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
		
		String[][] topics = new String[][]{
			new String[] {"faggot", "faggots", "fag", "fags", "tranny", "trannies", "shemale", "shemales", "dyke", "dykes", "homosexual", "homosexuals", "homo", "homos"},
			new String[] {"queer", "lgbt","lgbt+","lgbtq","lgbtq+","lgbtqiap+","lesbian","gay","bisexual","pansexual","transgender","trans","asexual","intersex", "intersexual","queers","lesbians","gays", "bisexuals", "pansexuals","asexuals"},
			new String[] {"terrorist","terrorism"},
			new String[] {"islam","muslim", "islamic", "muslims"},
			new String[] {"trump", "POTUS", "donald trump"},
			new String[] {"resist","nobannowall", "no ban no wall", "muslimban", "muslim ban","blm","black lives matter","blacklivesmatter"},
			new String[] {"healthcare","health care", "health"}
		};
		
		//The names array holds the names of the location and keyword queries.
		String[] names = new String[locargs.length + topics.length];
		names[0] = "France";
		names[1] = "USA";
		names[2] = "homo_slurs";
		names[3] = "lgbtq";
		names[4] = "terrorist_terrorism";
		names[5] = "islam";
		names[6] = "presidency";
		names[7] = "social_movements";
		names[8] = "healthcare";
		
		if (l < names.length){
			System.out.println("As of early 2017 Twitter only supports one stream per account. You have too many streams / too few accounts.");
			System.exit(420); //420 is the ratelimiting code for twitter.
		}
		
		try{
			for (int i = 0 ; i < locargs.length; i++){
				geoStream(locargs[i], names[i]);
			}
			for (int i = 0 ; i < topics.length; i++){
				streamThreader(topics[i], names[locargs.length+i]);
			}
		} catch (IndexOutOfBoundsException e){
			System.err.print("Index error in Hyperstream. Are you sure that you named every thread?");
		}
		
	}
}
