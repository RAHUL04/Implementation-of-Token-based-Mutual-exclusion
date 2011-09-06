//DOS - Project 4 - Valampuri Lakshminarayanan
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class Start
{

	public static String address;
	public static int port,nofclient,nofrequest;
	public static void main(String[] args)
	{


		try 
		{

			Properties prop = new Properties();
			prop.load(new FileInputStream("system.properties"));

			//Multicast address
			address = prop.getProperty("Multicast.address");

			//Multicast Port
			port = Integer.parseInt(prop.getProperty("Multicast.port"));

			//Number of clients and details
			nofclient = Integer.parseInt(prop.getProperty("ClientNum"));
			String[] clientname = new String[nofclient];
			int[] cport=new int[nofclient];
			
			nofrequest = Integer.parseInt(prop.getProperty("numberOfRequests"));	

			//Reading properties and Starting Remote Client Process
			for(int i=0;i<nofclient;i++)
			{
				String c = "Client".concat(Integer.toString(i+1));
				String cports = c.concat(".port");
				clientname[i] = prop.getProperty(c);
				cport[i] = Integer.parseInt(prop.getProperty(cports));

				int mycid=i+1;
				String path = System.getProperty("user.dir");
				Runtime.getRuntime().exec("ssh " + clientname[i] + " ; cd " + path + " ; java skb" + " " + address + " " + port + " " + mycid+" "+ nofclient +" "+ nofrequest);

			}

			//Reading output from server and printing

		}
			
		catch(Exception e)
		{
			e.printStackTrace();
		}
	

	}

}

