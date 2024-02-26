package Chat_App_Server;

import Chat_App_Res.*;

import java.awt.Component;
import java.io.*;
import java.net.*;
import java.util.*; 
import org.json.simple.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;
import org.json.simple.JSONObject;

import javax.swing.*;


public class Chat_Server    extends Thread
{   
    public Json_Handler jn;
    public JSONObject clnt,clnth;
    public JSONArray l1,l2,l3,l4;
    public PrintWriter pw;
    public Boolean ord=true;
    public Server_Identity id;
    public DataInputStream read;
    public BufferedReader cons;
    public Scanner Cons_inp;
    public String t1,t2,t3,ip,op,Clid="CLIENT-",path = "Resources\\"; 
    public int Port,Max_Clnt=0,Clntnum=0,cmp=0,pad; 
    public InetAddress Server_IP;
    public ServerSocket servp;
    public Client_Connection[] Clnts;
    public Socket temp;
    public Chat_Server(String Server_DNS,int Port,String[] args)  
    {
        // Variables Intitialisation:
        jn = new Json_Handler();
        this.Port = Port;
        this.cons = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            this.clnth = jn.Read_json("Clients_History");
            this.clnt = jn.Read_json("Clients");
            this.id = new Server_Identity();
            //id.Inp(Cons_inp);
            id.Inp(args);
            id.print();
            //System.out.println(this.hi.Tabs[0]);
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Server: "+e);
        }
    }
    public void Init()
    {
        Clnts = new Client_Connection[this.Max_Clnt]; 
        System.out.println("Server Online");
    }

    
    public void Update()            // to update the Number of Clients, who are Connected. 
    {
        // Reference https://www.alpharithms.com/detecting-client-disconnections-java-sockets-091416/
        // NOTE:    No Additional Mechanism is required to detect the Termination of Client Connection, since it the Input-Stream Thread is Already runnning.
        //          if any termination occurs, the Input-Stream Thread, will automatically acknowledge, with an Exception, that Exception can be used to detect the Client Connection Termination, using a Flag Variable.
        int t = 0;
        try
        {
            for(int i=0;i<Clntnum;i++)
            {
                //if(this.Clnts[i].soc.isClosed())        // Do not use the .isClosed() method to detect Client Siode Disconnection, it will oly detect Server Side Disconnection
                //if(this.Clnts[i].soc.getInputStream().read() == -1)
                if(this.Clnts[i] != null && this.Clnts[i].Cons)
                {
                    //System.out.println("Client "+this.Clnts[i].id.fv.get("name")+" Status: Connected");
                    t++;
                }
                else
                {
                    System.out.println("Chat_Server: Client "+this.Clnts[i].id.fv.get("Name")+" Status: Disconnected");
                    this.Clnts[i].soc.close();
                    int t1 =i;
                    if(t1 == Clntnum-1)
                    {
                        this.Clnts[t1] = null;
                        Clntnum-=1;
                    }
                    else
                    {
                        while(t1<Clntnum-1)
                        {
                            this.Clnts[t1] = this.Clnts[t1+1];
                            t1++;
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Server - Update: "+e);
        }
        this.Clntnum = t;
        System.out.println("Updated Client Count:"+this.Clntnum);
    }
    public void Update_Clients(int ord)
    {
        //System.out.println("Update Clients");
        if(this.Clntnum>0)
        {
            if(ord == 1)
            {
                try
                {
                    for(int i=0;i<Clntnum;i++)
                    {
                        this.Clnts[i].ops.writeUTF("<CHECK Type='Connection'>True</UPDATE>");
                    }
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
            }
        }
    }
    public void Add_Client()  
    {
        int n = Clntnum-1;
        t1 = this.Clid+String.valueOf(Clntnum);
        this.Clnts[Clntnum-1].id.fv.put("ID",t1); 
        // System.out.println("num:"+this.Clntnum); 
        // System.out.println("num:"+this.Clnts[Clntnum-1].id.fv.get("name")); 
        try
        {
            this.l1 = (JSONArray)this.clnth.get("Clients_ID");
            this.l2 = (JSONArray)this.clnt.get("Clients_ID");
            this.l1.add(t1);
            this.l2.add(t1);
            this.l3 = new JSONArray();
            for(int i=1;i<5;i++)
            {
                l3.add(this.Clnts[n].id.fv.get(this.Clnts[n].id.flds[i]));
            }
            this.clnth.put(t1,l3);
            this.clnt.put(t1,l3);
            PrintWriter pw = new PrintWriter(this.path+"Clients_History.json");
            pw.write(this.clnth.toJSONString());
            pw.flush();
            pw.close();
            pw = new PrintWriter(this.path+"Clients.json");
            pw.write(this.clnt.toJSONString());
            pw.flush();
            pw.close();
            this.Clnts[n].ops.writeUTF("<UPDATE Type='VALS:Client_ID'>"+t1+"</UPDATE>");
            for(int i=0;i<Clntnum;i++)
            {
                if(i!=n)
                {
                    this.Clnts[n].ops.writeUTF("<INFO Type='NETWORK:Persons:ID'>"+this.Clnts[i].id.fv.get("ID")+"</INFO>");
                    this.Clnts[n].ops.writeUTF("<INFO Type='NETWORK:Persons:Name'>"+this.Clnts[i].id.fv.get("Name")+"</INFO>");
                    this.Clnts[n].ops.writeUTF("<INFO Type='NETWORK:Persons:Language'>"+this.Clnts[i].id.fv.get("Language")+"</INFO>");
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Chat_Server:"+e);
        }
    }

    public int Max_Length()
    {
        int t;
        pad = 0;
        for(int i=0;i<this.Clntnum;i++)
        {
            t = this.Clnts[i].id.fv.get("Name").length();
            if(t>pad)
            {
                pad = t;
            }
        }
        return pad;
    }
    public void Forward_Message(String txt,String to,String name,String id)  throws IOException
    {   
        String msg=("<"+"CLIENT_MESSAGE"+" Name='"+name+"' ID='"+id+"'>"+txt+"</CLIENT_MESSAGE>");
        this.Update();
        System.out.println("msg:"+msg);
        //System.out.println(msg);
        for(int i=0;i<this.Clntnum;i++)
        {
            //System.out.println(this.Clnts[i].id.fv.get("ID")+"||"+to);
            if(this.Clnts[i].id.fv.get("ID").equals(to))
            {
                System.out.println("Message from "+id+" forwarded to "+this.Clnts[i].id.fv.get("ID"));
                this.Clnts[i].ops.writeUTF(msg);
                break;
            }
        }
    }
    public void Client_Update()
    {

    }
    @Override
    public void run()
    {
        try
        {
            // Server_IP = InetAddress.getByName(Server_DNS);
            // Server_IP = "127.0.0.1";
            servp = new ServerSocket(this.Port);
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Server: "+e);
        }
        // cons = new BufferedReader(new InputStreamReader(System.in));
        Clnts = new Client_Connection[Max_Clnt];
        while(Clntnum<Max_Clnt && ord)
        {
            try
            {
                System.out.println("m");
                temp = servp.accept();
                System.out.println("m");
                Clnts[Clntnum++] = new Client_Connection(temp,this);
                //this.Clnts[Clntnum-1].id.print();
                //this.Add_Client();
            }
            catch(Exception e)
            {
                System.out.println("\nChat_Server : "+e);
            }
        }
    }
    public String Get_Host_Name()
    {
        return this.id.fv.get("Name");
    }
    public void Stop_Server()
    {
        try
        {
            this.ord = false;
            for(int i=0;i<Clntnum;i++)
            {
                this.stop();
                this.servp.close();
                this.Clnts[i].inp.ip.close();
                this.Clnts[i].inp.ord = 0;
                this.Clnts[i].ops.close();
            }
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Server: "+e);
        }
        System.out.println("Server Offline");
    }
}   
