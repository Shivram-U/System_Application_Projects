package Chat_App_Server;

import Chat_App_Res.*;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;


public class Server_Input_Thread  extends Thread
{
 
    public int ord;
    public Socket soc;
    public Client_Connection cl;
    public BufferedReader cons;
    public DataInputStream ip;
    public Server_Input_Thread(Socket soc,Client_Connection cl)         // the Constructors have to be declared public, inorder to initialize the Class
    {
        System.out.println("Server input thread started");
        this.ord = 1;
        this.soc = soc;
        this.cl = cl;
        cons = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            ip = new DataInputStream(soc.getInputStream());
        }
        catch(Exception e)
        {
            if(cl==null)
            {
                System.out.print("\nServer_Input_Thread: "+e);
            }
            else   
            {
                System.out.println("\nServer_Input_Thread: "+"Client "+cl.id.fv.get("Name")+" Disconnected");
            }
        }
    }
    @Override
    public void run()
    {
        String t1,t2="",frm="",to="",msg="";
        int n=0,n1=1;
        try
        {
            t1 = ip.readUTF();
            //System.out.println(t1);
            n =6;
            //System.out.println(t1.substring(1,5));
            while(t1.substring(1, 5) != "END" && t1.substring(1,5).equals("INFO"))
            {
                n = 6;
                System.out.println("["+t1+"]");
                while(n<t1.length() && t1.charAt(n)!='<')
                {
                    //System.out.print("|"+t1.charAt(n));
                    t2+=t1.charAt(n++);
                }
                this.cl.id.fv.put(this.cl.id.flds[n1++],t2);
                t2 = "";
                t1 = ip.readUTF();
            }
            this.cl.cs.Add_Client();
            this.cl.id.print();
            while(soc.isConnected() && ord == 1)
            {
                t1 = ip.readUTF();
                //System.out.println(t1);
                //System.out.println("{"+t1+"}");
                //System.out.println("got"+t1.substring(1, 7));
                if(t1.length()>15 && t1.substring(1, 15).equals("CLIENT_MESSAGE"))
                {
                    frm=""; to="";msg="";
                    // <CLIENT_MESSAGE From='CLIENT-2' To='CLIENT-1'>Hi im Client</CLIENT_MESSAGE>
                    //System.out.println(t1);
                    System.out.println("Client_Message");
                    n = 22;
                    while(t1.charAt(n)!='\'')
                    {
                        frm+=t1.charAt(n++);
                    }
                    n+=6;
                    while(t1.charAt(n)!='\'')
                    {
                        to+=t1.charAt(n++);
                    }
                    n+=2;
                    while(t1.charAt(n)!='<')
                    {
                        msg+=t1.charAt(n++);
                    }
                    System.out.println(frm+" "+to+" "+msg);
                    this.cl.cs.Forward_Message(msg,to,this.cl.id.fv.get("Name"),this.cl.id.fv.get("ID"));  // The Alive Clients Count is also updated inside the Write_Text Function.
                }
                else if(t1.length()>7 && t1.substring(1, 7).equals("UPDATE"))
                {
                    /* 
                    //System.out.println("update");
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
                    if(t2.equals("Server_Configuration:ID"))
                    {
                        //System.out.println(msg);
                        this.cl.id.fv.put("ID",msg);
                        this.cl.id.print();
                    }
                    if(t2.equals("Data:pad"))
                    {
                        System.out.println(msg);
                        this.pad = Integer.parseInt(msg);
                    }
                    //System.out.println(msg);
                    //System.out.println("{"+t2+"}");
                    //System.out.println("\n"+t2);*/
                } 
                
                // NOTE:
                /*  
                        Normal Fonts, such as Helvetica, or Times New Roman, have variable Lengths for the font Letters, hence, when used to print a formatted String,
                        with Fixed Length, causes discrepancy, in the Length, even though the Number of Characters printed, still same, due to Difference in the Letter widths.

                        To solve this Problem, use fonts, which are MONOSPACE, These Fonts ensure that all the Letters are of Same Size.
                */
            }
            ip.close();
        }
        catch(Exception e)
        {
            System.out.print("\nServer_Input_Thread: "+e.getLocalizedMessage());
            if(cl!=null)
            {
                System.out.println("\nServer_Input_Thread: "+"Client "+cl.id.fv.get("Name")+" Disconnected");
                this.cl.Cons = false;       // to inform the Closing of the Socket Connection, thorugh Flag Variable.   
                this.cl.cs.Update();
            }
        }
    }
}
