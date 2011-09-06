//DOS - Project 4 - Valampuri Lakshminarayanan
import java.io.*;
import java.net.*;
import java.util.*;

public class skb
{
	static int port;
	static String group;
	static int nofclients;
	static int nofrequests;
	static int id,inq=0;
	static boolean x = true;
	public static void main(String args[]) throws IOException
	{
		//Getting inputs from start through cmd line
		
        group=args[0];		
        port=Integer.parseInt(args[1]);
        id=Integer.parseInt(args[2]);
        nofclients=Integer.parseInt(args[3]);
        nofrequests=Integer.parseInt(args[4]);

        skb s = new skb();
		
	}
	
	Vector<Integer> seqvec = new Vector<Integer>();
	public boolean wait = true;
	public boolean youwait=true;
	public volatile boolean have_token=false;
	token dtoken = null;
	String mainmatter ="valampur".concat(Integer.toString(id));
	String matter="";
	int mynofreq=0;
	
	public skb()
	{

			
			try 
			{
				if(id==1)
				{
					have_token=true;
	        		dtoken = new token(nofclients);
				}
				for(int i=0;i<nofclients;i++)
					seqvec.add(0);
				listen l = new listen();
				Thread.sleep(2000);
				request r = new request();
				tokenhandler t = new tokenhandler();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
	}


	class listen extends Thread
	{
		public int myport;
		public listen()
		{
			this.start();
		}
		public void run()
		{
				try 
				{
					MulticastSocket s = new MulticastSocket(port);
					s.joinGroup(InetAddress.getByName(group));
					byte buf[] = new byte[1024];
					DatagramPacket pack = new DatagramPacket(buf, buf.length);
					while(true)
					//for(int h=0;h<(nofrequests);h++)
					{
						//for(int j=0;j<(nofclients-1);j++)
						//{
							s.receive(pack);
							//byte[] bmatter = pack.getData();
							matter= new String(pack.getData());
							//System.out.println(id+" is Listening.. ");
							//System.out.print(matter);
							
							for(int l=0;l<nofclients;l++)
							{
								int seq = Integer.parseInt(Character.toString(matter.charAt(l+9)));
								if(seq>seqvec.elementAt(l))
									seqvec.set(l, seq);
								
							}
							//System.out.println();
							//System.out.println("deiiiiiiiii" + dtoken.id);

							//System.out.println();
						//}
						
					}
					//s.leaveGroup(InetAddress.getByName(group));
					//s.close();

				} 
				catch (UnknownHostException e) 
				{
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
	
			}
		
	}
	
	class request extends Thread
	{

		String sseqvec="";
		
		public request()
		{
			this.start();
		}
		public void run()
		{
			try 
			{
				for(int h=0;h<nofrequests;h++)
				{
						if((id==1) && (mynofreq==0) &&(have_token==true))
						{
							//System.out.println("I skipped because I am first..................................."+h);
							h--;
							//System.out.println("I skipped because I am first(2)..................................."+h);
							while(have_token==true){Thread.sleep(100);}
						}
						else if((have_token==true))
						{
							//System.out.println("I skipped because I have token...................................."+h);
							h--;
							//System.out.println("I skipped because I have token(2)...................................."+h);
							while(have_token==true){Thread.sleep(100);}
						}
						else
						{
							//System.out.println("I am not skipping............................."+h);
							seqvec.set((id-1),seqvec.elementAt(id-1)+1);
						
							//converting seq vector to string
							for(int ss=0;ss<nofclients;ss++)
							{
								sseqvec=sseqvec.concat(Integer.toString(seqvec.elementAt(ss)));
							}
	
							mainmatter = mainmatter.concat(sseqvec);
							//System.out.println("Broadcasting request.. : "+mainmatter);			
							byte buf[] = mainmatter.getBytes();
							MulticastSocket s = new MulticastSocket();
							DatagramPacket pack = new DatagramPacket(buf, buf.length,InetAddress.getByName(group), port);
							s.send(pack);
							s.send(pack);
							Random rand = new Random();
							int random=rand.nextInt(999);
							s.close();
						
							//System.out.println("Waiting for token");
							mynofreq++;
							
							while(have_token==false){Thread.sleep(100);}
							
							//System.out.println("Requesting again......");
							Thread.sleep(random+1);
						}
						mainmatter ="valampur".concat(Integer.toString(id));
						sseqvec="";
				}
				
			}
			catch (IOException e) 
			{
					e.printStackTrace();
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	class tokenhandler extends Thread
	{
		public tokenhandler()
		{

			this.start();
		}
		
		public void run()
		{
			int exit=0,exit2=0;
			while(true)
			{
				try 
				{
					
					if(have_token==true)
					{
						for(int t=0;t<nofclients;t++)
						{
							if((seqvec.elementAt(t)>dtoken.tokvec.elementAt(t))&& (dtoken.tokq.contains(t+1)==false))
							{
								dtoken.tokq.add(t+1);
								//System.out.println("Enqueuing in token Q :"+(t+1));

							}
						}
						
						//System.out.println(dtoken.tokq.add(Integer.parseInt(Character.toString(matter.charAt(8)))));
						//System.out.println("Enqueuing in token vector :"+matter.charAt(8));
						//System.out.println("Inside q:" +dtoken.tokq.peek());
	
						if((dtoken.tokq.peek()!=null) && (dtoken.tokq.peek()!=id))
						{
							//Thread.sleep(200);
							Properties prop2 = new Properties();
							prop2.load(new FileInputStream("system.properties"));

							//output
							//System.out.println(dtoken.output);
							dtoken.output = dtoken.output.concat(Integer.toString(id)+"\t\t");
							//System.out.println(Integer.toString(id)+"\t");
							dtoken.output = dtoken.output.concat((seqvec.toString()+"\t\t"));
							//System.out.print((seqvec.toString()+"\t"));
							dtoken.output = dtoken.output.concat((dtoken.tokvec.toString()+"\t\t"));
							//System.out.print((dtoken.tokvec.toString()+"\t"));
							dtoken.output = dtoken.output.concat((dtoken.tokq.toString()+"\n"));
							//System.out.print((dtoken.tokq.toString()+"\n"));
							
							//Getting first element of token queue
							int he=dtoken.tokq.poll();
							//System.out.println("Adada"+he);
							
							//Reading from system.properties					
							String nowc = "Client".concat(Integer.toString(he));
							String nowc2 = "Client".concat(Integer.toString(id));
							String nowcports = nowc.concat(".port");
							String nowcports2 = nowc2.concat(".port");
							int hisport = Integer.parseInt(prop2.getProperty(nowcports));
							int myport = Integer.parseInt(prop2.getProperty(nowcports2));
							InetAddress hisaddr = InetAddress.getByName(prop2.getProperty(nowc));
							
							
							//Creating socket for unicast communication
							Thread.sleep(400);
							//System.out.println("Token exchange from "+id +" at "+ myport +" to " + he + " on " + hisaddr +" at "+ hisport);
							//System.out.println("Token Q : " + dtoken.tokq.toString());
							//System.out.println("Token Vector : " +dtoken.tokvec.toString());
							if(dtoken.tokvec.elementAt(id-1)==nofrequests)
								exit=1;
							
							Socket csocket = new Socket(hisaddr,hisport);
							ObjectOutputStream os = new ObjectOutputStream(csocket.getOutputStream());
							os.writeObject(dtoken);
							dtoken = null;
							have_token=false;
							//System.out.println("Sent the packet !");
							os.close();
							csocket.close();
						}
						if(exit==1)
							System.exit(1);
						
					}
					else
					{
						
						Properties prop2 = new Properties();
						prop2.load(new FileInputStream("system.properties"));
						String nowc2 = "Client".concat(Integer.toString(id));
						String nowcports2 = nowc2.concat(".port");
						int myport = Integer.parseInt(prop2.getProperty(nowcports2));
						
						ServerSocket ssocket = new ServerSocket(myport);
						Socket cs = ssocket.accept();
						ObjectInputStream is = new ObjectInputStream(cs.getInputStream());
						dtoken = (token) is.readObject();
						is.close();
						cs.close();
						ssocket.close();
						
						have_token=true;
					    //System.out.println("Receieved the packet ! " +"Inside the recvd packet's q:" +dtoken.tokq.peek());
					    //System.out.println("Inside the recvd packet's q:" +dtoken.tokq.peek());
					    
					    //incrementing token vector
					    dtoken.tokvec.set((id-1),dtoken.tokvec.elementAt(id-1)+1);
					    //System.out.println("Token Vector : " +dtoken.tokvec.toString());
					    for(int t=0;t<nofclients;t++)
						{	
				    		if(dtoken.tokvec.elementAt(t)==nofrequests)
				    		{
								exit2=1;
							}
							else
							{
								exit2=0;
								break;
							}
						}
						if(exit2==1)
						{
			    			dtoken.output = dtoken.output.concat(Integer.toString(id)+"\t\t");
			    			dtoken.output = dtoken.output.concat((seqvec.toString()+"\t\t"));
			    			dtoken.output = dtoken.output.concat((dtoken.tokvec.toString()+"\t\t"));
			    			if(dtoken.tokq.peek()==null)
			    			{
			    				dtoken.output = dtoken.output.concat(("NULL"));
			    			}
			    			else
			    			{
			    				dtoken.output = dtoken.output.concat((dtoken.tokq.toString()+"\n"));
			    			}
							//System.out.println(dtoken.output);
							
							PrintWriter sout = new PrintWriter("request.log");
							sout.println("GROUP MEMBER : "+nofclients);
							sout.println("# of each Member's Request : "+nofclients);
							sout.println(dtoken.output);
							sout.close();
							System.exit(1);
						}
					    
					    //critical section
					    Random rand = new Random();
						int random=rand.nextInt(999);
					    Thread.sleep(random+1);
					}
					
					
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				catch (ClassNotFoundException e) 
				{
					e.printStackTrace();
				}
	
				/*		for(int h=0;h<(Global.nofrequests*Global.nofclients);h++) 
				{					
					System.out.println("Token handler..\nSeq vector is :");
					for(int d=0;d<Global.nofclients;d++)
						System.out.print(seqvec[d]+"\t");
					System.out.println();
					System.out.println("Token handler..\nToken vector is :");
					for(int d=0;d<Global.nofclients;d++)
						System.out.print(tokvec[d]+"\t");
					System.out.println();
					System.out.println("Token handler..\nToken Queue is :");
					for(int g=0;g<Global.nofclients;g++)
						System.out.print(tokq[g]+"\t");
				}*/
			}			
		}
	}
}	
