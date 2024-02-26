package Chat_App_Server;

import Chat_App_Res.*;
import Customized_Screen_Dev.*;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.json.simple.JSONObject;

public class Server_CLI
{
    public Console cons;
    public Startup st;
    public Chat_Server cs = null;
    public Json_Handler jn = null;
    public JSONObject det = null,conf = null;
    public Update ut;
    public int Screen_Width,Screen_Height,cmps=0,prt;
    public String[] Tabs = {"Server","Server Details","Clients Details","Terminal"},
                    lbls = {"General Chat Server Interface","Server Status","Server IP Address","Application Port","Maximum Client Count","Number of Clients Connected",
                            "ID","Language","City","State","Country"},
                    bts = {"Start Server","Stop Server","Update Server Configuration","Update Server Details"},
                    flds={"ID","Language","City","State","Country"},
                    cl_flds={"Name","ID","Language","Location","Nationality"},
                    conflds={"Status","Server_IP","Port","Max_Client_Count","Clients_Count"};
    public String vals[] = {"","","","",""},rec[] = {"N","N","N","N","N"};
    //public String[][] Client_Data;
    public String Client_Data[][] = {{"N","N","N","N","N"}};
    public String t,path = "Resources\\",com="",pwd="";
    public static void main(String[] args)
    {
        Server_CLI serv = new Server_CLI();
    }
    public Server_CLI()
    {
        pwd = System.getProperty("user.dir");
        //System.out.println(pwd);
        vals = new String[5];
        jn = new Json_Handler();
        
        this.cons = System.console();
        try
        {
            this.det = jn.Read_json("Server_Data");
            this.conf= jn.Read_json("Server_Configuration");
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Server: "+e);
        }
        this.Start_Command_Line_Intrepreter();
    }
    public void Start_Command_Line_Intrepreter()
    {
        System.out.println("_".repeat(40)+" General Chat Server Interface"+"_".repeat(40));
        while(!com.equals("Exit"))
        {
            System.out.println(">");
            com = this.cons.readLine();
            if(com.equals("Start Server"))
            {
                this.Start_Server();
            }
        }
    }
    // Functions
    public void Start_Server()
    {
        if(cs == null)
        { 
            for(int i=0;i<5;i++)
                this.vals[i] = String.valueOf(this.det.get(this.flds[i]));
            try
            {
                t = String.valueOf(this.conf.get("Server_IP"));
                prt = Integer.parseInt((String)this.conf.get("Port"));
                cs = new Chat_Server(t, prt,this.vals);
                this.cs.Max_Clnt = Integer.parseInt(String.valueOf(this.conf.get("Max_Client_Count")));
                this.cs.Init();
                //this.oup = new Server_Output_Thread(this.cs);
                cs.start();
            }
            catch(Exception e1)
            {
                System.out.println("Server: "+e1);
            }
        }
    }
    public void Stop_Server()
    {
        try
        {
            this.cs.Stop_Server();
            this.cs = null;
        }
        catch(Exception e1)
        {
            System.out.println(e1);
        }
    }
}
