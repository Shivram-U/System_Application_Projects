package Chat_App_Server;

import Chat_App_Res.*;
import Chat_App_Server.Server_CLI;
import Customized_Screen_Dev.*;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Startup   implements  ActionListener
{
    public Startup st;
    public JFrame jr;
    public String com,pwd;
    public static void main(String[] args)
    {
        Startup st = new Startup();
    }
    public Startup()
    {
        com="";
        pwd = System.getProperty("user.dir");
        System.out.println(pwd);
        jr = new JFrame("Startup");
        jr.setUndecorated(true);
        jr.setTitle("   General Chat  ");
        jr.setLocation(320,250);
        jr.setSize(900,150);
        //jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jr.setResizable(false);                         // Will Not Work, Since the Resizing Provision, provided by the OS, is removed, we have to Define Our Own Algorithm for Resizing.
        jr.setVisible(true);
        jr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jr.getContentPane().setBackground(new Color(20,20,20));
        jr.getContentPane().setLayout(null);

        jr.getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0,255,255)));
        JLabel jl1 = new JLabel("Chat App");
        jr.getContentPane().add(jl1);
        jl1.setOpaque(true);
        jl1.setSize(300,50);
        jl1.setBounds(0,0,900,30);
        jl1.setBackground(new Color(40,40,40));
        jl1.setHorizontalAlignment(JLabel.CENTER);
        jl1.setForeground(new Color(0,255,255));
        jl1.setFont(new Font("Helvetica",Font.PLAIN,25));
        JLabel jl2 = new JLabel("Choose the Server Interface");
        jr.getContentPane().add(jl2);
        jl2.setSize(300,50);
        jl2.setBounds(70,40,400,30);
        jl2.setForeground(new Color(0,255,255));
        jl2.setFont(new Font("Helvetica",Font.PLAIN,20));
        JButton jb1 = new JButton("Graphical_User_Interface");
        JButton jb2 = new JButton("Command_Line_Interface");
        jb1.addActionListener(this);
        jb2.addActionListener(this);
        jr.getContentPane().add(jb1);
        jr.getContentPane().add(jb2);
        jb1.setFont(new Font("Helvetica",Font.PLAIN,20));
        jb2.setFont(new Font("Helvetica",Font.PLAIN,20));
        jb1.setBounds(120,80,300,40);
        jb2.setBounds(480,80,300,40);
        jb1.setBackground(new Color(40,40,40));
        jb2.setBackground(new Color(40,40,40));
        jb1.setForeground(new Color(0,255,255));
        jb2.setForeground(new Color(0,255,255));
        jb1.setBorder(null);
        jb2.setBorder(null);
    }
    public void actionPerformed(ActionEvent ae)
    {
        if(ae.getActionCommand().equals("Graphical_User_Interface"))
        {
            Server_GUI serv = new Server_GUI();
            this.jr.dispose();
        }
        else if(ae.getActionCommand().equals("Command_Line_Interface"))
        {
            try
            {
                //com = "cmd /c start cmd.exe /k cd "+pwd+" ; java -cp '"+pwd+"\\Java_Libraries\\json-simple-1.1.1.jar;.' Chat_App_Server.Server_CLI";
                //com = "cmd /c start cmd.exe /k \"java -cp Chat_App_Server.jar Chat_App_Server.Server_CLI && exit\"";
                com = "cmd /c start cmd.exe /k \"java -cp  \"D:\\Shivram_U\\Sources\\Software\\Project_Sys_WorkSpace\\Android_App_Projects\\General_Chat\\Server_Applications\\Java_Libraries\\json-simple-1.1.1.jar;.\" Chat_App_Server.Server_CLI  && exit\"";
                System.out.println(com);
                Process p = Runtime.getRuntime().exec(com);
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
            this.jr.dispose();
        }
    }
}