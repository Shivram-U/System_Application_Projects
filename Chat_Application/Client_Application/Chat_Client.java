package Chat_App_Client;

import Chat_App_Res.*;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Component;

class Client_Input_Thread  extends Thread
{

    public Chat_Client ccl;
    public BufferedReader cons;
    public DataInputStream ip;
    public Socket soc;
    public JTextArea jta;
    public Alive alv;
    public int ord  = 0;
    Client_Input_Thread(Chat_Client ccl)
    {
        this.ccl = ccl;
        cons = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            //System.out.println(cl.soc);
            ip = new DataInputStream(ccl.soc.getInputStream());
            //System.out.println(ip);
        }
        catch(Exception e)
        {
            System.out.println("\nClient_Input_Thread: "+e);
        }
    }
    Client_Input_Thread(Socket soc,Chat_Client ccl)
    {
        this.soc = soc;
        this.ccl = ccl;
        cons = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            System.out.println(soc);
            ip = new DataInputStream(soc.getInputStream());
            //alv = new Alive(soc);
            //alv.start();
        }
        catch(Exception e)
        {
            if(ccl==null)
            {
                System.out.print("\nClient_Input_Thread: "+e);
            }
            else   
                System.out.println("\nClient_Input_Thread: "+"Client "+this.ccl.Client_ID.fv.get("Name")+" Disconnected");
        }
    }
    @Override
    public void run()
    {
        String t1,t2="",msg="",nam="",id="";
        int n=0,n1=0;
        try
        {

            /* 
            t1 = ip.readUTF();
            n =6;
            System.out.println(t1.substring(1,5));
            while(t1.substring(1,5) != "END" && t1.substring(1,5).equals("INFO"))
            {
                n = 6;
                System.out.println(t1);
                while(n<t1.length() && t1.charAt(n)!='<')
                {
                    //System.out.print("|"+t1.charAt(n));
                    t2+=t1.charAt(n++);
                }
                this.ccl.Server_ID.fv.put(this.ccl.Server_ID.flds[n1++],t2);
                t2 = "";
                t1 = ip.readUTF();
            }*/
            while(this.soc.isConnected())
            {
                t1 = ip.readUTF();
                // System.out.println("{"+t1+"}");
                // System.out.println("got"+t1.substring(1, 15));
                //<INFO Type='VALS:Server:TYPE'></INFO>
                if(t1.length()>4 && t1.substring(1,5).equals("INFO"))
                {
                    if(t1.substring(12,16).equals("VALS"))
                    {
                        if(t1.substring(17,23).equals("Server"))
                        {
                            n=24;
                            t2="";
                            while(n<t1.length() && t1.charAt(n)!='\'')
                            {
                                //System.out.print("|"+t1.charAt(n));
                                t2+=t1.charAt(n++);
                            }
                            n+=2;
                            msg="";
                            while(n<t1.length() && t1.charAt(n)!='<')
                            {
                                msg+=t1.charAt(n++);
                            }
                            System.out.println("Server:"+t2+" "+msg);
                            this.ccl.Server_ID.fv.put(t2,msg);
                            //this.ccl.Server_ID.print();
                        }
                    }
                    else if(t1.substring(12,19).equals("NETWORK"))
                    {
                        String[] vals = new String[3];
                        if(t1.substring(20,27).equals("Persons"))
                        {
                            n1=0;
                            while(n1<=2)
                            {
                                n=28;
                                t2="";
                                while(n<t1.length() && t1.charAt(n)!='\'')
                                {
                                    //System.out.print("|"+t1.charAt(n));
                                    t2+=t1.charAt(n++);
                                }
                                n+=2;
                                msg="";
                                while(n<t1.length() && t1.charAt(n)!='<')
                                {
                                    msg+=t1.charAt(n++);
                                }
                                System.out.println("NETWORK:"+t2+" "+msg);
                                vals[n1] = msg; 
                                if(n1<2)
                                    t1 = ip.readUTF();  
                                n1++;
                            }
                            this.ccl.cl.Add_Entry(0,vals);
                        }

                    }
                }
                else if(t1.length()>15 && t1.substring(1, 15).equals("CLIENT_MESSAGE"))
                {
                    // System.out.println("Client_Message");
                    // {<CLIENT_MESSAGE Name='Shivram_U' ID='CLIENT-2'>Hi im Client</CLIENT_MESSAGE>}
                    id="";msg="";nam="";
                    n = 22;
                    while(t1.charAt(n)!='\'')
                    {
                        nam+=t1.charAt(n++);
                    }
                    // System.out.println(t2);
                    n+=6;
                    while(t1.charAt(n)!='\'')
                        id+=t1.charAt(n++);
                    n+=2;
                    while(t1.charAt(n)!='<')
                        msg+=t1.charAt(n++);
                    this.ccl.cl.Add_Message_Entry(nam,msg,id);
                    //  System.out.println(msg);
                    //  t2 = String.format("%"+-this.ccl.pad+"s%s",t2," >  "+msg);
                    //  System.out.println("{"+t2+"}");
                    //  System.out.println("\n"+t2);
                    //  NOTE:
                    /*      
                            Normal Fonts, such as Helvetica, or Times New Roman, have variable Lengths for the font Letters, hence, when used to print a formatted String,
                            with Fixed Length, causes discrepancy, in the Length, even though the Number of Characters printed, still same, due to Difference in the Letter widths.

                            To solve this Problem, use fonts, which are MONOSPACE, These Fonts ensure that all the Letters are of Same Size.
                    */
                    //  REF :  JTextArea c = (JTextArea) (((JViewportView) (((JScrollPane) jTabbedPane1.getComponentAt(i)).getViewport()))).getView();
                }
                else if(t1.length()>5 && t1.substring(1,6).equals("CHECK"))
                {
                    //System.out.println(t1);
                    // <CHECK Type='Connection'>True</CHECK>
                    t2="";
                    msg="";
                    n = 13;
                    while(t1.charAt(n)!='\'')
                    {
                        t2+=t1.charAt(n++);
                    }
                    n+=2;
                    while(t1.charAt(n)!='<')
                    {
                        msg+=t1.charAt(n++);
                    }
                    //System.out.println(t2+" "+msg);
                    if(t2.equals("Connection"))
                    {
                        if(msg.equals("True"))
                        {
                            //System.out.println("Server Connection:Live");
                        }
                    }
                }
                else if(t1.length()>7 && t1.substring(1, 7).equals("UPDATE"))
                {
                    System.out.println(t1);
                    System.out.println("update");
                    t2="";
                    msg="";
                    n = 14;
                    while(t1.charAt(n)!='\'')
                    {
                        t2+=t1.charAt(n++);
                    }
                    //System.out.println(t2+"|");
                    n+=2;
                    while(t1.charAt(n)!='<')
                        msg+=t1.charAt(n++);
                    if(t2.equals("VALS:Client_ID"))
                    {
                        System.out.println(msg);
                        this.ccl.cl.jt[1][0].setText(msg);
                        this.ccl.Client_ID.fv.put("ID",msg);
                        this.ccl.Client_ID.print();
                    }
                    //System.out.println(msg);
                    //System.out.println("{"+t2+"}");
                    //System.out.println("\n"+t2);
                } 
            }
            ip.close();
        }
        catch(Exception e)
        {
                System.out.print("\nClient_Input_Thread: "+e);
                this.ccl.soc = null;
        }
    }
}

class Alive extends Thread      // Not Required, implemented to detect the Client Connection Termination.
{
    public Socket soc;
    public DataOutputStream ous;
    Alive(Socket soc)
    {
        try
        {
            this.soc = soc;
            ous = new DataOutputStream(this.soc.getOutputStream());
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Client: "+e);
        }
    }
    @Override
    public void run()
    {
        while(!soc.isClosed())
        {
            System.out.println("meow");
            try
            {
                this.ous.writeByte(1);
                this.sleep(1000);
            }
            catch(Exception e)
            {
                System.out.println("\nChat_Client: "+e);
            }
        }
    }
}

public class Chat_Client
{
    public Client cl;
    public identity Client_ID;   
    public Server_Identity Server_ID;
    public Socket soc;
    public Client_Input_Thread inp;
    public DataOutputStream ous;
    public int pad=1;
    public Chat_Client(String Server_DNS,int Port,Client cl)  throws IOException,InterruptedException
    {
        ///InetAddress address = InetAddress.getByName("0.tcp.in.ngrok.io");
        this.cl = cl;
        //for(int i=0;i<this.cmp;i++)
        //    System.out.println(this.comps[i]);
        Client_ID = new identity();
        this.Server_ID = new Server_Identity();
        //Scanner sc = new Scanner(System.in);
        Client_ID.Inp(cl.vals);
        //Client_ID.print();
        try
        {
            soc = new Socket(Server_DNS,Port);
            System.out.println(soc);
            System.out.println(soc.isConnected());
            System.out.println(soc.isClosed());
            if(this.soc.isConnected())
            {
                this.ous = new DataOutputStream(soc.getOutputStream());
                for(int j=1;j<5;j++)
                {
                    this.ous.writeUTF("<INFO>"+this.Client_ID.fv.get(this.Client_ID.flds[j])+"<INFO>");
                }
                this.ous.writeUTF("<END>");
                String[] t = new String[10];
                //this.id.Inp(Cons_inp);
                //id.print();
                //DataInputStream read = new DataInputStream(soc.getInputStream());
                //DataOutputStream wrt = new DataOutputStream(soc.getOutputStream());
                String in="",out="";
                System.out.println("Stream Started");
                inp = new Client_Input_Thread(soc,this);
                inp.start();
                System.out.println("Started:"+soc);
                //System.out.println("Server Disconnected");
                //read.close();
                //inp.close();
                //cons.close();
            }
        }
        catch(Exception e)
        {
            System.out.println("Chat_Client:"+e);
        }
    }  
    public Boolean Disconnect()
    {
        try
        {
            System.out.println(this.inp.soc);
            System.out.println(this.inp.soc.isClosed());
            this.inp.soc.close();
            return true;
        }
        catch(Exception e)
        {
            System.out.println("Chat Client:"+e);
            return false;
        }
    }
    public String Get_Client_Name()
    {
        return this.Client_ID.fv.get("Name");
    }
    public void Forward_Message(String Client_ID,String msg)
    {
        try
        {
            System.out.println(Client_ID+" "+msg);
            this.ous.writeUTF("<CLIENT_MESSAGE From='"+this.Client_ID.fv.get(this.Client_ID.flds[0])+"' To='"+Client_ID+"'>"+msg+"</CLIENT_MESSAGE>");
            //this.ous.writeUTF(txt);
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Client: "+e);
        }
    }
    public void Write_Text(String txt)
    {
        try
        {
            this.ous.writeUTF("<"+"CLIENT_MESSAGE"+">"+txt+"</CLIENT_MESSAGE>");
            //this.ous.writeUTF(txt);
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Client: "+e);
        }
    }  
}
