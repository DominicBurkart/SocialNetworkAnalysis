package SocialNetworkAnalysis.Applications;

import java.util.ArrayList;

import SocialNetworkAnalysis.TwitterAuth;
import SocialNetworkAnalysis.Utilities;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * 
 * Collects streams from locations specified by Mark.
 * 	
 * @author Dominic Burkart
 * 
 * @since March 2018
 *
 */
public class MarkGeo{
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
		double[][][] locargs = {
				new double[][]{ 
					new double[]{-10.390234375, 51.4737304688},
					new double[]{-6.02739257812, 55.3658203125},
					new double[]{172.705957031, -41.6106445312},
					new double[]{178.536230469, -34.4291015625},
					new double[]{166.477636719, -47.263671875},
					new double[]{174.370117188, -40.4900390625},
					new double[]{-5.65625, 50.0213867188},
					new double[]{1.74658203125, 55.8079589844},
					new double[]{-8.14482421875, 54.0512695312},
					new double[]{-5.47041015625, 55.241796875},
					new double[]{-7.54296875, 54.689453125},
					new double[]{-0.774267578125, 60.8318847656},
					new double[]{-5.2623046875, 51.3904296875},
					new double[]{-2.6623046875, 53.4192871094},
					new double[]{-78.3395019531, 17.7149414062},
					new double[]{-76.2107910156, 18.5222167969},
					new double[]{143.838574219, -43.6193359375},
					new double[]{148.47421875, -39.5801757813},
					new double[]{112.908203125, -39.1455078125},
					new double[]{153.616894531, -10.0517578125},
					new double[]{73.251171875, -53.1845703125},
					new double[]{73.8377929688, -52.9663085938},
					new double[]{167.906152344, -29.0962890625},
					new double[]{167.990429688, -29.0139648438},
					new double[]{-124.709960938, 24.5423339844},
					new double[]{-66.9870117187, 49.3696777344},
					new double[]{-160.243457031, 18.9639160156},
					new double[]{-154.804199219, 22.2231445312},
					new double[]{-178.19453125, 51.6036621094},
					new double[]{-130.0140625, 71.4076660156},
					new double[]{3.133, 50.75},
					new double[]{7.217, 53.683},
					new double[]{4.7990234375, 58.0209472656},
					new double[]{30.9606445313, 71.1420898438},
					new double[]{-61.9061035156, 10.0646484375},
					new double[]{-60.9176269531, 10.840234375},
					new double[]{-60.8106445312, 11.1686035156},
					new double[]{-60.5254882812, 11.325390625},
					new double[]{11.1471679688, 55.3463867188},
					new double[]{24.15546875, 69.0368652344},
					new double[]{14.6841796875, 55.0049316406},
					new double[]{15.137109375, 55.2967285156},
				},
				new double [][]{
					new double[]{8.121484375, 54.6288574219},
					new double[]{12.6657226563, 57.7369140625},
					new double[]{-72.8180664062, 59.8154785156},
					new double[]{-11.4255371094, 83.599609375},
					new double[]{-141.002148438, 41.6748535156},
					new double[]{-52.6536621094, 83.1161132813},
					new double[]{34.2453125, 29.47734375},
					new double[]{35.9134765625, 33.4317382812},
					new double[]{-13.2926757812, 6.90654296875},
					new double[]{-10.283203125, 9.99653320312},
					new double[]{103.650195312, 1.26538085938},
					new double[]{103.996386719, 1.4470703125},
					new double[]{-11.5075195312, 4.35131835938},
					new double[]{-7.39990234375, 8.5376953125},
					new double[]{9.5240234375, 46.3997070312},
					new double[]{17.1473632813, 49.0011230469},
					new double[]{5.85751953125, 47.2788085938},
					new double[]{15.0166015625, 55.0587402344},
					new double[]{20.6221679688, 59.816015625},
					new double[]{31.5365234375, 70.06484375},
					new double[]{-3.24389648438, 4.76245117188},
					new double[]{1.18720703125, 11.1668945312},
					new double[]{116.96953125, 5.06020507812},
					new double[]{126.593359375, 20.8412597656},
					new double[]{99.6462890625, 0.861962890625},
					new double[]{119.266308594, 7.35166015625},
					new double[]{5.97001953125, 45.8300292969},
					new double[]{10.4545898438, 47.7756347656},
					new double[]{4.2146484375, 50.7760253906},
					new double[]{4.44169921875, 50.900390625},
					new double[]{2.52490234375, 50.6976074219},
					new double[]{5.89248046875, 51.4911132813},
					new double[]{2.85546875, 49.5108886719},
					new double[]{6.364453125, 50.8059570312},
					new double[]{13.3782226563, 45.4283691406},
					new double[]{16.5162109375, 46.86328125},
					new double[]{60.843359375, 23.7533691406},
					new double[]{77.0486328125, 37.0366699219},
				},
				new double [][]{
					new double[]{2.68603515625, 4.27739257813},
					new double[]{14.6271484375, 13.8728515625},
					new double[]{19.646484375, 34.9344726563},
					new double[]{28.2318359375, 41.7437988281},
					new double[]{21.8544921875, 57.5254882812},
					new double[]{28.1510742188, 59.6390136719},
					new double[]{140.862304687, -11.6305664062},
					new double[]{154.280761719, -1.35322265625},
					new double[]{13.5171875, 42.4329101562},
					new double[]{19.4009765625, 46.5346191406},
					new double[]{79.7078125, 5.94936523438},
					new double[]{81.876953125, 9.8126953125},
					new double[]{80.0516601563, 26.3603027344},
					new double[]{88.1615234375, 30.3875},
					new double[]{21.0149414063, 55.6675292969},
					new double[]{28.2020507813, 58.0634277344},
					new double[]{34.95078125, 29.1904785156},
					new double[]{39.2927734375, 33.3722167969},
					new double[]{25.2240234375, -22.4020507813},
					new double[]{33.0067382813, -15.6430664062},
					new double[]{35.10859375, 33.0756835938},
					new double[]{36.5849609375, 34.6787109375},
					new double[]{-4.7625, 42.3404785156},
					new double[]{8.14033203125, 51.0971191406},
					new double[]{19.97734375, -26.8541992188},
					new double[]{29.36484375, -17.7875976562},
					new double[]{8.5328125, 1.67622070312},
					new double[]{16.1833984375, 13.078515625},
					new double[]{20.8998046875, 53.89296875},
					new double[]{26.7756835938, 56.4111816406},
					new double[]{14.1286132813, 49.0207519531},
					new double[]{24.1057617188, 54.8381835937},
					new double[]{24.7032226563, 21.9948730469},
					new double[]{36.8713867188, 31.6549804687},
					new double[]{38.7735351563, 29.063671875},
					new double[]{48.546484375, 37.371875},
					new double[]{8.180859375, 38.9096679688},
					new double[]{9.8052734375, 41.2570800781},
					new double[]{6.627734375, 37.9391113281},
					new double[]{18.4858398438, 47.0821289063},
					new double[]{12.435546875, 36.6878417969},
					new double[]{15.6346679688, 38.2958984375},
					new double[]{16.4475585938, -34.7857421875},
					new double[]{32.8861328125, -22.1462890625},
					new double[]{20.241796875, 43.6708007812},
					new double[]{29.705859375, 48.2634765625},
					new double[]{27.0517578125, -30.6422851563},
					new double[]{29.3907226563, -28.5817382813},
				},
				new double [][]{
					new double[]{97.3739257812, 5.63676757813},
					new double[]{105.641015625, 20.4244140625},
					new double[]{-9.47973632812, 37.0054199219},
					new double[]{-6.2125, 42.1374023437},
					new double[]{16.8626953125, 47.7634277344},
					new double[]{22.538671875, 49.5977050781},
					new double[]{22.3440429688, 41.2435546875},
					new double[]{28.5853515625, 44.2377929688},
					new double[]{-9.23564453125, 36.0259277344},
					new double[]{3.30673828125, 43.7645507812},
					new double[]{16.0930664063, 45.7530273437},
					new double[]{22.8766601563, 48.5534667969},
					new double[]{33.9, -4.6923828125},
					new double[]{41.883984375, 5.49228515625},
					new double[]{88.0234375, 20.7904296875},
					new double[]{92.631640625, 26.5715332031},
					new double[]{43.2571289063, -25.5705078125},
					new double[]{50.4827148438, -12.0795898437},
					new double[]{11.7216796875, -28.9387695313},
					new double[]{25.2587890625, -16.9676757813},
					new double[]{25.6689453125, 35.8314453125},
					new double[]{44.8171875, 42.0932617188},
					new double[]{21.97890625, -18.0415039062},
					new double[]{33.6615234375, -8.19365234375},
					new double[]{57.3176757813, -20.5131835938},
					new double[]{57.7919921875, -19.9899414063},
					new double[]{46.6091796875, 40.6086425781},
					new double[]{87.3228515625, 55.3895996094},
					new double[]{28.8576171875, -2.80859375},
					new double[]{30.8765625, -1.0630859375},
					new double[]{-17.0030761719, 21.420703125},
					new double[]{-1.06552734375, 35.9298828125},
					new double[]{-118.401367187, 14.5454101562},
					new double[]{-86.6962890625, 32.7153320313},
					new double[]{68.1650390625, 8.0783203125},
					new double[]{97.3435546875, 35.4958984375}
				}
		};

		//The names array holds the names of the location and keyword queries.
		String[] names = new String[] {"A","B", "C", "D", "E", "F", "G"};  // arbitrary, but can't include the number 2 or periods.

		for (int i=0; i < locargs.length; i++){
			geoStream(locargs[i], "markgeo"+names[i]);
		}

	}
}