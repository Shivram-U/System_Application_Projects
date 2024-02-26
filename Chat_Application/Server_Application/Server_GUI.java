package Chat_App_Server;

import Chat_App_Res.*;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.json.simple.JSONObject;


//import Network_Interface.Chat_Server;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;


public class Server_GUI    implements ActionListener,WindowListener
{
    public Startup st;
    public Chat_Server cs = null;
    public Json_Handler jn = null;
    public JSONObject det = null,conf = null;
    public Update ut;
    public Container cont;
    public Component[] comps;
    public JFrame jf;
    public Cust_JTabPn jtp;
    public JPanel[] jp;
    public JScrollPane[] jsp;
    public JTextArea[] jta;
    public JLabel[][] jl;
    public JLabel[] mjl;
    public JTextField[][] jt;
    public JButton[][] jb;
    public JTable jtb;
    public Font fnt;
    public int Screen_Width,Screen_Height,cmps=0,prt,clnts=0;
    public String[] Tabs = {"Server","Server Details","Clients Details","Terminal"},
                    lbls = {"General Chat Server Interface","Server Status","Server IP Address","Application Port","Maximum Client Count","Number of Clients Connected",
                            "ID","Language","City","State","Country"},
                    bts = {"Start Server","Stop Server","Update Server Configuration","Update Server Details"},
                    flds={"ID","Language","City","State","Country"},
                    cl_flds={"Name","ID","Language","Location","Nationality"},
                    conflds={"Status","Server_IP","Port","Max_Client_Count","Clients_Count"};
    public String vals[] = {"","","","",""};
    //public String[][] Client_Data;
    public String[][] Client_Data;
    public String t,path = "Resources\\",com="";
    public static void main(String args[])
    {
        Server_GUI serv = new Server_GUI();
    }
    public  Server_GUI()
    {
        Insets insets = UIManager.getInsets("TabbedPane.contentBorderInsets");
        // JTabbepane Border Configuration
            insets.top = -1;
            insets.left = -1;
            insets.right = -1;
            insets.bottom = -1;
        //
        UIManager.put("TabbedPane.focus", new Color(0,0,0));
        UIManager.put("TabbedPane.selected", new Color(0,0,0));
        UIManager.put("TabbedPane.borderHightlightColor", new Color(0,0,0)); 
        UIManager.put("TabbedPane.darkShadow", new Color(0,0,0)); 
        UIManager.put("TabbedPane.light", new Color(0,0,0));
        //UIManager.put("TabbedPane.lightHighlight", new Color(255,0,0));
        UIManager.put("TabbedPane.selectHighlight", new Color(0,255,255));
        UIManager.put("TabbedPane.Shadow",new Color(255,0,0));
        UIManager.put("TabbedPane.contentBorderInsets", insets);

        //Customized_Screen_Dev.MW  wind = new  Customized_Screen_Dev.MW();
        Client_Data = new String[20][5];
        comps = new Component[10];
        jf = new JFrame();
        jf.setLayout(null);             // The Layout has to be specified before the Bounds of the Components to be specified,
        this.cont = jf.getContentPane();
        jp = new JPanel[5];
        jsp = new JScrollPane[3];
        jta = new JTextArea[3];
        mjl = new JLabel[4];
        jl = new JLabel[4][6];
        jt = new JTextField[4][6];
        jb = new JButton[4][6];
        vals = new String[5];
        jtp = new Cust_JTabPn();
        jn = new Json_Handler();
        jtb = new JTable(Client_Data,this.cl_flds){
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
               return false;
            }
        };
        jsp[0] = new JScrollPane(jtb);
        /* 
        Client_Data = new String[110][10];
        for(int i=0;i<100;i++)
        {
            this.Client_Data[i] = rec;
        }
        */
        this.Values_Init();
        this.Add_Components();
    }
    public void Add_Client(String[] vals)
    {
        this.Client_Data[this.cs.Clntnum-1] = vals;
    }
    public void Update_UI()     // This function is called periodically, by a thread for every 1 Second, inorder to Update/Refresh the User Interface.
    {
        //System.out.print("|>");
        if(cs!=null)
        {
            jt[0][4].setText(String.valueOf(this.cs.Clntnum)); 
            /*System.out.println("{"+this.clnts+","+this.cs.Clntnum+"}");
            System.out.println("Length: "+this.cs.Clntnum);
            System.out.println("Clients: ");
            for(int i=0;i<this.cs.Clntnum;i++)
                this.cs.Clnts[i].id.print();
            System.out.print("{");
            for(int i=0;i<10;i++)
            {
                System.out.print(this.Client_Data[i][0]+"|");
            }
            System.out.println("}");
            System.out.print("{");
            for(int i=0;i<5;i++)
                System.out.print("("+this.cs.Clnts[i].id.fv.get("Name")+")");
            System.out.println("}");
            */
            for(int i=0;i<this.cs.Clntnum;i++)
            {
                for(int j=0;j<5;j++)
                {
                    Client_Data[i][j] = this.cs.Clnts[i].id.fv.get(this.cl_flds[j]);
                }
            }   
            if(this.clnts>this.cs.Clntnum)
            {   
                for(int i=this.cs.Clntnum;i<this.clnts;i++)
                {
                    //System.out.println("[["+this.Client_Data[i][0]+"]]");
                    for(int j=0;j<5;j++)
                    {
                        Client_Data[i][j] = null;
                    }
                }
            }
            this.clnts = this.cs.Clntnum;
            this.jtb.repaint(); 
        }
        //System.out.print("<|");
    }
    public void Update_Clients()
    {
        if(this.cs!=null)
            this.cs.Update_Clients(1);
    }
    public void Values_Init()
    {
        // Values:
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            this.Screen_Width = (int)size.getWidth();
            this.Screen_Height = (int)size.getHeight();
        fnt = new Font("Helvetica",Font.PLAIN,20);
        try
        {
            this.det = jn.Read_json("Server_Data");
            this.conf= jn.Read_json("Server_Configuration");
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Server: "+e);
        }
    }
    public void Add_Components()
    {
        for(int i=0;i<4;i++)
        {
            jp[i] = new JPanel();
            jp[i].setLayout(null);
            jtp.add(Tabs[i],jp[i]);
        }
        mjl[0] = new JLabel(this.lbls[0]);
        for(int i=0;i<5;i++)
        {
            jl[0][i] = new JLabel(this.lbls[i+1]);
            jt[0][i] = new JTextField();
            this.jp[0].add(jl[0][i]);
            this.jp[0].add(jt[0][i]);
        }
        for(int i=0;i<5;i++)
        {
            jl[1][i] = new JLabel(this.lbls[i+6]);
            jt[1][i] = new JTextField();
            this.jp[1].add(jl[1][i]);
            this.jp[1].add(jt[1][i]);
        }
        for(int i=0;i<3;i++)
        {
            jb[0][i] = new JButton(this.bts[i]);
            this.jp[0].add(jb[0][i]);
        }
        jb[1][0] = new JButton(this.bts[3]);
        this.jp[1].add(jb[1][0]);
        jta[0] = new JTextArea();
        this.jf.add(mjl[0]);
        
        this.jf.add(jtp);
        
        jp[2].add(jsp[0]);
        this.Position_Components();
        //System.out.println(jp[0].getComponent(0));
    }
    public void Position_Components()
    {
        mjl[0].setBounds(0,0,this.Screen_Width,35);
        for(int i=0,y=100;i<5;i++,y+=70)
        {
            jl[0][i].setBounds(this.Screen_Width/4,y,400,50);
            jt[0][i].setBounds(this.Screen_Width/4+400,y,400,50);
            jl[1][i].setBounds(this.Screen_Width/4,y,400,50);
            jt[1][i].setBounds(this.Screen_Width/4+400,y,400,50);
        }
        jb[0][0].setBounds(600,500,300,40);
        jb[0][1].setBounds(600,550,300,40);
        jb[0][2].setBounds(600,600,300,40);
        jb[1][0].setBounds(600,600,300,40);
        jtp.setBounds(0,36,this.Screen_Width, this.Screen_Height);
        jsp[0].setBounds(0,0,this.Screen_Width,this.Screen_Height-135);
        this.Configure_Components();
    }
    public void Configure_Components()  
    {
        // jta[0].setText("mwowqwqw");
        // Main Window Configuration
            jf.setTitle("   General Chat  ");
            jf.setLocation(0,0);
            //jf.setSize(1000,700);
            jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
            jf.setResizable(true);                         // Will Not Work, Since the Resizing Provision, provided by the OS, is removed, we have to Define Our Own Algorithm for Resizing.
            jf.setVisible(true);
            
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            cont.setBackground(new Color(0,0,0));
            cont.setLayout(null);
        
        this.jtp.setBorder(null);
        this.jtp.setFont(new Font("Helvetica",Font.PLAIN,20));
        this.jtp.setBackground(new Color(40,40,40));
        this.jtp.setForeground(new Color(0,255,255));
        for(int i=0;i<4;i++)
        {
            this.jp[i].setBackground(new Color(16,16,16));
            this.jp[i].setBorder(null);
        }
        try
        {
            // System.out.println(jn.Read_json("Server_Config").get("Port"));
            // jt[0].setText("0");
            // jt[1].setText("1");
            // jt[1].setText((String)(jn.Read_json("Server_Config").get("Server_IP")));
            jt[0][0].setText((String)(this.conf.get("Status")));
            jt[0][1].setText((String)(this.conf.get("Server_IP")));
            jt[0][2].setText(String.valueOf((this.conf.get("Port"))));
            jt[0][3].setText(String.valueOf((this.conf.get("Max_Client_Count"))));
            jt[0][4].setText(String.valueOf((this.conf.get("Clients_Count"))));
        
            for(int i=0;i<5;i++)
            {
                t = String.valueOf(this.det.get(this.flds[i]));
                jt[1][i].setText(String.valueOf(t));
                this.vals[i] = String.valueOf(t);
            }
        }
        catch(Exception e)
        {
            System.out.println("\nHost_Interface: "+e);
        }

        mjl[0].setOpaque( true);
        mjl[0].setHorizontalAlignment(SwingConstants.CENTER);
        mjl[0].setBorder(null);
        mjl[0].setBackground(new Color(30,30,30));
        mjl[0].setForeground(new Color(0,255,255));
        mjl[0].setFont(fnt);

        for(int i=0;i<5;i++)
        {
            jl[0][i].setOpaque( true);
            jl[0][i].setHorizontalAlignment(SwingConstants.CENTER);
            jl[0][i].setBorder(null);
            jl[0][i].setBackground(new Color(30,30,30));
            jl[0][i].setForeground(new Color(0,255,255));
            jl[0][i].setFont(fnt);
            jl[1][i].setOpaque( true);
            jl[1][i].setHorizontalAlignment(SwingConstants.CENTER);
            jl[1][i].setBorder(null);
            jl[1][i].setBackground(new Color(30,30,30));
            jl[1][i].setForeground(new Color(0,255,255));
            jl[1][i].setFont(fnt);
        }
        for(int i=0;i<5;i++)
        {
            jt[0][i].setBackground(new Color(40,40,40));
            jt[0][i].setFont(fnt);
            jt[0][i].setForeground(new Color(0,255,255));
            jt[0][i].setBorder(null);
            jt[0][i].setHorizontalAlignment(SwingConstants.CENTER);
            jt[1][i].setBackground(new Color(40,40,40));
            jt[1][i].setFont(fnt);
            jt[1][i].setForeground(new Color(0,255,255));
            jt[1][i].setBorder(null);
            jt[1][i].setHorizontalAlignment(SwingConstants.CENTER);
        }

        for(int i=0;i<3;i++)
        {
            jb[0][i].setFont(fnt);
            jb[0][i].setHorizontalAlignment(SwingConstants.CENTER);
            jb[0][i].setBackground(new Color(40,40,40));
            jb[0][i].setForeground(new Color(0,255,255));
        }
        jb[1][0].setFont(fnt);
        jb[1][0].setHorizontalAlignment(SwingConstants.CENTER);
        jb[1][0].setBackground(new Color(40,40,40));
        jb[1][0].setForeground(new Color(0,255,255));

        jsp[0].getViewport().setBackground(new Color(0,0,0));
        jsp[0].setBorder(null);
        jtb.setRowSelectionAllowed(false);
        jtb.setRowHeight(50);
        jtb.setBackground(new Color(40,40,40));
        jtb.setForeground(new Color(0,255,255));
        jtb.setFont(new Font("Helvetica",Font.PLAIN,25));
        jtb.setOpaque(true);
        jtb.setBorder(null);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        TableModel tableModel = jtb.getModel();

        for (int columnIndex = 0; columnIndex < 5; columnIndex++)
        {
            jtb.getColumnModel().getColumn(columnIndex).setCellRenderer(rightRenderer);
        }
        
        JTableHeader jth = jtb.getTableHeader();
        jth.setReorderingAllowed(false);
        jth.setBackground(new Color(60,60,60));
        jth.setForeground(new Color(0,255,255));
        jth.setFont(new Font("Helvetica",Font.PLAIN,25));
        this.Add_Events();
        this.ut = new Update(this);
        ut.start();
    }
    public void Add_Events()
    {
        jf.addWindowListener(this);
        for(int i=0;i<3;i++)
            jb[0][i].addActionListener(this);
        jb[1][0].addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getActionCommand() == "Start Server")
        {
            if(cs == null && vals[1]!=null && vals[2]!=null && this.jt[0][3].getText()!=null)
            { 
                try
                {
                    t = String.valueOf(this.conf.get("Server_IP"));
                    prt = Integer.parseInt((String)this.conf.get("Port"));
                    cs = new Chat_Server(t, prt,this.vals);
                    this.cs.Max_Clnt = Integer.parseInt(this.jt[0][3].getText());
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
        else if(e.getActionCommand() == "Stop Server")
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
        else if(e.getActionCommand() == "Update Server Details")
        {
            for(int i=0;i<5;i++)
            {
                this.vals[i] = this.jt[0][i].getText();
                this.det.put(flds[i],this.jt[1][i].getText());
            }
            try
            {
                PrintWriter pw = new PrintWriter(this.path+"Server_Data.json");
                pw.write(this.det.toJSONString());
                pw.flush();
                pw.close();
                this.det = jn.Read_json("Server_Data");
            }
            catch(Exception e1)
            {
                System.out.println(e1);
            }
        }
        else if(e.getActionCommand() == "Update Server Configuration")
        {
            for(int i=0;i<5;i++)
            {
                this.conf.put(conflds[i],this.jt[0][i].getText());
            }
            try
            {
                PrintWriter pw = new PrintWriter(this.path+"Server_Configuration.json");
                pw.write(this.conf.toJSONString());
                pw.flush();
                pw.close();
                this.conf = jn.Read_json("Server_Configuration");
            }
            catch(Exception e1)
            {
                System.out.println(e1);
            }
        }
        
    }
    @Override
    public void windowOpened(WindowEvent e) {
        
    }
    @Override
    public void windowClosing(WindowEvent e) {
        this.ut.ord = 0;
        System.out.println("Done");
    }
    @Override
    public void windowClosed(WindowEvent e) 
    {
        
    }
    @Override
    public void windowIconified(WindowEvent e) {
    }
    @Override
    public void windowDeiconified(WindowEvent e) {
    }
    @Override
    public void windowActivated(WindowEvent e) {
    }
    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}