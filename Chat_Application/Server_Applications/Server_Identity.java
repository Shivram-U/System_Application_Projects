package Chat_App_Res;

import java.awt.Component;
import java.io.*;
import java.net.*;
import java.util.*; 

public class Server_Identity
{
    public String[] flds={"ID","Language","City","State","Country"};
    public HashMap<String,String> fv;
    public Server_Identity(String[] args)
    {
        fv = new HashMap<String,String>();
        for(int i=0;i<5;i++)
            fv.put(flds[i],args[i]);
    }
    public Server_Identity()
    {
        fv = new HashMap<String,String>();
    }
    public void Inp(Scanner Cons_inp)
    {
        System.out.print("Enter Server ID:");
        fv.put(flds[0],Cons_inp.next());
        System.out.print("Enter Language:");
        fv.put(flds[1],Cons_inp.next());
        System.out.print("Enter City:");
        fv.put(flds[2],Cons_inp.next());
        System.out.print("Enter State:");
        fv.put(flds[3],Cons_inp.next());
        System.out.print("Enter Country:");
        fv.put(flds[4],Cons_inp.next());
    }
    public void Inp(String[] args)
    {
        for(int i=0;i<5;i++)
            fv.put(flds[i],args[i]);
    }
    public void print()
    {
        System.out.println("Server "+this.fv.get("ID")+" Info:");
        for(int i=0;i<5;i++)
        {
            System.out.println(String.format("%"+-9+"s %s  %s",this.flds[i],":",this.fv.get(this.flds[i])));
        }
    }
}
