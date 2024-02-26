package Chat_App_Server;

import Chat_App_Res.*;

import java.awt.Component; 
import java.io.*;
import java.net.*;
import java.util.*; 

public class Client_Connection
{
    public DataOutputStream ops;
    public Chat_Server cs;
    public identity id;   
    public Socket soc;
    public Server_Input_Thread inp;
    public Boolean Cons;
    // The 'Cons' is a Flag Variable, to ensure that the Connection is Terminated, 
    // which is required to update the Live, Clients in the Server, inorder to Forware the Received Messages.
    public Client_Connection(Socket soc,Chat_Server cs)  
    {
        // Variables Intitialisation:
        this.cs = cs;
        this.soc = soc;
        this.Cons = true;
        try
        {
            this.ops = new DataOutputStream(soc.getOutputStream());
            for(int j=0;j<5;j++)
            {
                this.ops.writeUTF("<INFO Type='VALS:Server:"+this.cs.id.flds[j]+"'>"+this.cs.id.fv.get(this.cs.id.flds[j])+"<INFO>");
            }
            this.ops.writeUTF("<UPDATE VALS='Client_ID'>"+this.cs.id.fv.get(this.cs.id.flds[0])+"<INFO>");
            String[] t = new String[10];
            this.id = new identity();
            //this.id.Inp(Cons_inp);
            //id.print();
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Server: "+e);
        }
        Start_Stream();
    }
    public void Start_Stream()
    {
        System.out.println("Stream Started");
        inp = new Server_Input_Thread(soc,this);
        inp.start();
    }
}
