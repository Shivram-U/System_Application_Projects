package Chat_App_Client;

import Chat_App_Res.*;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.json.simple.JSONObject;
import java.awt.geom.Path2D;

//import Network_Interface.Chat_Server;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

// Task Check Thread
class Task_Check extends Thread
{
    public Client cl = null;
    public Task_Check(Client cl)
    {
        this.cl = cl;
    }
    @Override
    public  void run()
    {
        while(true)
        {
            this.cl.Task_Check();
            this.cl.Update_UI();
            try
            {
                this.sleep(1000);
            }
            catch(Exception e)
            {
                System.out.println("Task_Check: "+e);
            }
        }
    }
}
class ScrollablePanel extends JPanel {
    private Dimension contentSize;

    public ScrollablePanel() {
        setLayout(null);
        contentSize = new Dimension(0, 0);
    }
    public ScrollablePanel(Dimension cs) {
        setLayout(null);
        contentSize = cs;
    }

    public void setContentSize(Dimension size) {
        contentSize = size;
    }

    @Override
    public Dimension getPreferredSize() {
        return contentSize;
    }
}

public class Client    implements ActionListener,WindowListener
{
    public Chat_Client cl = null;
    public Task_Check tc = null;
    public Container cont;
    public Component[] comps = null;
    public JFrame jf;
    public Json_Handler jn;
    public JLabel[][] jl;
    public Component[][] Person_Entries,Conversation_Entries;
    public JLabel tml1,tml2;
    public JLabel[] mjl,Chat_jl;
    public JTextField[][] jt;
    public JTextField[] Chat_jt;
    public JTextArea[] jta;
    public Cust_JTabPn[] jtp;
    public JButton[][] jb;
    JButton[] Chat_jb;
    public JScrollPane[] jsp,Chat_jsp;
    public JPanel[] jp,Chat_jp;
    public ScrollablePanel[] jsl,Chat_jsl;
    public Font fnt;
    public Boolean Server_Conn = true;
    public int Screen_Width,Screen_Height,cmps=0,Cons_X,Cons_Y,Console_Width,Console_Height,pe=0,ce=0,Chat_jps=0;
    public int[] Chat_Cons_Y,Chat_Console_Height;
    public String[] Tabs = {"Network","User Details","Console"},
                    hd = {"Identity","Name","Language","Host Location","Nationality"},
                    clnt={"Chat Server","Chat_Server_IP","Server_Port","Server OFFLINE","Server Info","ID","Language","City","State","Country","Network Info","Conversations","Persons"},
                    bts = {"Update Connection Configuration","Connect","Disconnect","Update User Details","Send"},
                    flds={"ID","Language","City","State","Country"},
                    cl_flds={"ID","Name","Language","Location","Nationality"},
                    Clients;
    public String vals[] = {"","","","",""};
    public Color concolor = new Color(255,0,0);
    public String t,t1,t2;
    public JSONObject usr,conn;
    public static void main(String[] args)
    {
        Client serv = new Client();
    }
    public Client()
    {
        //Customized_Screen_Dev.MW  wind = new  Customized_Screen_Dev.MW();
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

        jn = new Json_Handler();
        jf = new JFrame();
        this.cont = jf.getContentPane();
        jf.setLayout(null);             // The Layout has to be specified before the Bounds of the Components to be specified,
        jtp = new Cust_JTabPn[4];
        comps = new Component[10];
        this.jsp = new JScrollPane[5];
        mjl = new JLabel[6];
        jl = new JLabel[4][15];
        this.Person_Entries = new Component[20][4];
        this.Conversation_Entries = new Component[20][4];
        jt = new JTextField[4][15];
        jp = new JPanel[6];
        jsl = new ScrollablePanel[5];
        jb = new JButton[4][6];
        vals = new String[5];
        Clients = new String[20];
        this.Chat_jp =  new JPanel[20];
        this.Chat_jt =  new JTextField[20];
        this.Chat_jl =  new JLabel[20];
        this.Chat_jb =  new JButton[20];
        this.Chat_jsp =  new JScrollPane[20];
        this.Chat_jsl =  new ScrollablePanel[20];
        Chat_Cons_Y = new int[20];
        Chat_Console_Height = new int[20];
        
        this.Values_Init();
        this.Add_Components();
        // Threads
        this.tc = new Task_Check(this);
        this.tc.start();
    }
    public void Values_Init()
    {
        // Values:
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            this.Screen_Width = (int)size.getWidth();
            this.Screen_Height = (int)size.getHeight();
            this.Console_Height = this.Screen_Height-243;
        fnt = new Font("Helvetica",Font.PLAIN,20);
        try
        {
            conn = jn.Read_json("Connection");
            usr = jn.Read_json("User");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    // Application Data Processes
        public void Connect_Server()
        {
            if(cl == null && Server_Conn)
            {
                System.out.println("connect");
                try
                {
                    for(int i=0;i<4;i++)
                        this.comps[i] = this.jp[i];
                    cl = new Chat_Client("localhost",3333,this);
                    System.out.println(cl);
                    if(cl.soc!=null)
                    {
                        this.jl[0][3].setText("Server ONLINE");
                        this.jf.remove(mjl[1]);
                        mjl[1] = new JLabel()
                        {          
                            @Override
                            protected void paintComponent(Graphics g) {
                                Graphics2D g2d = (Graphics2D) g.create();
                                g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                                g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                                g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                                g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                                super.paintComponent(g2d);
                                g2d.setStroke(new BasicStroke(4.0f));
                                g2d.setColor(new Color(0,255,0));
                                g2d.drawArc(5,5, 40,30,30,120);
                                g2d.drawArc(12,12, 25,20,30,120);
                                g2d.drawLine(7, 14,25,30);
                                g2d.drawLine(44, 14,25,30);
                            }
                        };
                        jf.add(mjl[1]);
                        mjl[1].setBounds(this.Screen_Width-50,0,50,40);
                        mjl[1].setOpaque(true);
                        mjl[1].setBackground(new Color(0,0,0));
                        this.jl[0][3].setBackground(new Color(100,255,100));
                        this.jt[2][0].setText("Hi im Client");
                        this.jb[2][0].setEnabled(true);
                        this.jt[2][0].setEnabled(true);
                        for(int i=0;i<5;i++)
                        {
                            this.jt[0][i+2].setText(String.valueOf(this.cl.Server_ID.fv.get(this.clnt[i+5])));
                        }
                    }
                    else
                    {
                        this.cl = null;
                    }
                }
                catch(Exception e1)
                {
                    System.out.println("\nClient: "+e1);
                }
                this.Update_UI();
            }
        }
        public void Disconnect_Server()
        {
            if(cl.soc!=null)
            {
                try
                {
                    for(int i=0;i<4;i++)
                        this.comps[i] = null;
                    if(this.cl.Disconnect())
                    {
                        this.jl[0][3].setText("Server OFFLINE");
                        this.jl[0][3].setBackground(new Color(255,50,50));
                        this.jf.remove(mjl[1]);
                        mjl[1] = new JLabel()
                        {          
                            @Override
                            protected void paintComponent(Graphics g) {
                                Graphics2D g2d = (Graphics2D) g.create();
                                g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                                g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                                g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                                g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                                super.paintComponent(g2d);
                                g2d.setStroke(new BasicStroke(4.0f));
                                g2d.setColor(new Color(255,0,0));
                                g2d.drawArc(5,5, 40,30,30,120);
                                g2d.drawArc(12,12, 25,20,30,120);
                                g2d.drawLine(7, 14,25,30);
                                g2d.drawLine(44, 14,25,30);
                            }
                        };
                        jf.add(mjl[1]);
                        mjl[1].setBounds(this.Screen_Width-50,0,50,40);
                        mjl[1].setOpaque(true);
                        mjl[1].setBackground(new Color(0,0,0));
                        this.jt[2][0].setText(" ");
                        this.jb[2][0].setEnabled(false);
                        this.jt[2][0].setEnabled(false);
                        this.cl = null;
                        for(int i=0;i<5;i++)
                        {
                            this.jt[0][i+2].setText("_______________");
                        }
                        this.jt[1][0].setText("<Server OFFLINE>");
                    }
                }
                catch(Exception e1)
                {
                    System.out.println("\nClient: "+e1);
                }
                this.Update_UI();
            }
        }
        public void Task_Check()
        {
            // Check Server Availability
                this.Connect_Server();
            // Check Server Connection
            if(cl !=null)
            {
                if(cl.soc==null)
                {
                    try
                    {
                        for(int i=0;i<4;i++)
                            this.comps[i] = null;
                        this.jl[0][3].setText("Server OFFLINE");
                        this.jl[0][3].setBackground(new Color(255,50,50));
                        this.jf.remove(mjl[1]);
                        mjl[1] = new JLabel()
                        {          
                            @Override
                            protected void paintComponent(Graphics g) {
                                Graphics2D g2d = (Graphics2D) g.create();
                                g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                                g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                                g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                                g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                                super.paintComponent(g2d);
                                g2d.setStroke(new BasicStroke(4.0f));
                                g2d.setColor(new Color(255,0,0));
                                g2d.drawArc(5,5, 40,30,30,120);
                                g2d.drawArc(12,12, 25,20,30,120);
                                g2d.drawLine(7, 14,25,30);
                                g2d.drawLine(44, 14,25,30);
                            }
                        };
                        jf.add(mjl[1]);
                        mjl[1].setBounds(this.Screen_Width-50,0,50,40);
                        mjl[1].setOpaque(true);
                        mjl[1].setBackground(new Color(0,0,0));
                        this.jt[2][0].setText(" ");
                        this.jb[2][0].setEnabled(false);
                        this.jt[2][0].setEnabled(false);
                        this.cl = null;
                        for(int i=0;i<5;i++)
                        {
                            this.jt[0][i+2].setText("_______________");
                        }
                        this.jt[1][0].setText("<Server OFFLINE>");
                    }
                    catch(Exception e1)
                    {
                        System.out.println("\nClient: "+e1);
                    }
                    this.Update_UI();
                }
            }
        }
        public void Update_UI()
        {
            //System.out.println("Update");
            try
            {
                conn = jn.Read_json("Connection");
            }
            catch(Exception e)
            {
                System.out.println("Client:"+e);
            }
            jt[0][0].setText(String.valueOf(conn.get(this.clnt[1])));
            jt[0][1].setText(String.valueOf(conn.get(this.clnt[2])));
            if(cl!=null)
            {
                for(int i=0;i<5;i++)
                {
                    this.jt[0][i+2].setText(String.valueOf(this.cl.Server_ID.fv.get(this.clnt[i+5])));
                }
            }
        }
        public void Add_Entry(int ord,String[] vals)
        {
            if(ord == 0)
            {
                System.out.println("PErson Entry");
                String t1="",t2="";
                int l;
                //tml = new JLabel(this.cl.Get_Client_Name());
                for(int i=0;i<3;i++)
                {
                    this.Person_Entries[pe][i] = new JLabel(vals[i]);
                    ((JLabel)this.Person_Entries[pe][i]).setHorizontalAlignment(SwingConstants.CENTER);
                    ((JLabel)this.Person_Entries[pe][i]).setOpaque(true);
                    ((JLabel)this.Person_Entries[pe][i]).setFont(new Font("Helvetica",Font.PLAIN,20));
                    ((JLabel)this.Person_Entries[pe][i]).setBackground(new Color(40,40,40));
                    ((JLabel)this.Person_Entries[pe][i]).setForeground(new Color(0,255,255));
                    this.jsl[3].add((JLabel)this.Person_Entries[pe][i]);
                    ((JLabel)this.Person_Entries[pe][i]).setBounds(i*370+((i+1)*4),(pe*50)+((pe+1)*10),370,50); 
                }
                this.Person_Entries[pe][3] = new JButton("Chat:"+vals[0]);
                ((JButton)this.Person_Entries[pe][3]).setHorizontalAlignment(SwingConstants.CENTER);
                ((JButton)this.Person_Entries[pe][3]).setOpaque(true);
                ((JButton)this.Person_Entries[pe][3]).setFont(new Font("Helvetica",Font.PLAIN,20));
                ((JButton)this.Person_Entries[pe][3]).setBackground(new Color(70,70,70));
                ((JButton)this.Person_Entries[pe][3]).setForeground(new Color(0,255,255));
                ((JButton)this.Person_Entries[pe][3]).addActionListener(this);
                this.jsl[3].add(this.Person_Entries[pe][3]);
                this.Person_Entries[pe][3].setBounds(3*370+((4)*6),(pe*50)+((pe+1)*10),370,50); 
                pe++;
                jsl[2].revalidate();
            }
        }
        public void Add_Message_Entry(String Name,String msg,String Console_ID)
        {
            int n;
            n = this.Create_Chat_Console(Console_ID);
            if(n == -1)
                n = this.Chat_jps-1;
            tml1 = new JLabel(Name);
            tml1.setHorizontalAlignment(SwingConstants.CENTER);
            tml1.setOpaque(true);
            tml1.setFont(new Font("Helvetica",Font.PLAIN,20));
            tml1.setBackground(new Color(70,70,70));
            tml1.setForeground(new Color(0,255,255));
            this.Chat_jsl[n].add(tml1);
            tml1.setBounds(0,this.Chat_Cons_Y[n],300,40); 
            tml2 = new JLabel(msg);
            tml2.setHorizontalAlignment(SwingConstants.CENTER);
            tml2.setOpaque(true);
            tml2.setFont(new Font("Helvetica",Font.PLAIN,20));
            tml2.setBackground(new Color(40,40,40));
            tml2.setForeground(new Color(0,255,255));
            this.Chat_jsl[n].add(tml2);
            //jsl[1].setBounds(0, 0, this.Screen_Width,this.Screen_Height-400);
            Chat_jsl[n].revalidate();
            tml2.setBounds(300,this.Chat_Cons_Y[n],this.Screen_Width-320,40); 
            this.Chat_Cons_Y[n]+=50;
            //System.out.println(String.valueOf(this.Cons_Y)+" "+String.valueOf(this.Console_Height)+" "+String.valueOf(this.Screen_Width-243));
            if(this.Chat_Cons_Y[n]>this.Chat_Console_Height[n])
            {
                //System.out.println("meow");
                this.Chat_Console_Height[n]+=50;
                this.Chat_jsl[n].setContentSize(new Dimension(this.Screen_Width,this.Chat_Console_Height[n]));
                JScrollBar vertical = Chat_jsp[n].getVerticalScrollBar();
                System.out.println(String.valueOf( vertical.getValue()));
                vertical.setValue( vertical.getValue()+50);
            }
        }
        public int Create_Chat_Console(String Client_ID)
        {
            for(int i=0;i<Chat_jps;i++)
            {
                if(this.Clients[i].equals(Client_ID))
                {
                    return i;
                }
            }
            this.Chat_jp[Chat_jps] = new JPanel();
            this.Chat_jp[Chat_jps].setLayout(null);
            this.Chat_jl[Chat_jps] = new JLabel(Client_ID+" Chat Console");
            this.Clients[Chat_jps] = Client_ID;
            this.Chat_jt[Chat_jps] = new JTextField("Hi im Client");
            this.Chat_jb[Chat_jps] = new JButton("Send>"+Client_ID);
            this.Chat_jb[Chat_jps].addActionListener(this);
            this.Chat_jsl[Chat_jps] = new ScrollablePanel(new Dimension(this.Screen_Width,this.Console_Height));
            this.Chat_jsl[Chat_jps].setBorder(null);
            this.Chat_jsl[Chat_jps].setBackground(new Color(10,10,10));
            this.Chat_jsl[Chat_jps].setLayout(null);
            this.Chat_jsp[Chat_jps] = new JScrollPane(this.Chat_jsl[Chat_jps],JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            this.Chat_jsp[Chat_jps].setBorder(null);
            // Component Inclusions
            this.Chat_jp[Chat_jps].add(this.Chat_jsp[Chat_jps]);
            this.Chat_jp[Chat_jps].add(this.Chat_jl[Chat_jps]);
            this.Chat_jp[Chat_jps].add(this.Chat_jt[Chat_jps]);
            this.Chat_jp[Chat_jps].add(this.Chat_jb[Chat_jps]);
            // Component Configuration
            this.Chat_jsp[Chat_jps].setBounds(0,50,this.Screen_Width+2,this.Screen_Height-240);
            this.Chat_jl[Chat_jps].setBounds(0,0,this.Screen_Width+2,50);
            this.Chat_jb[Chat_jps].setBounds(this.Screen_Width-200,this.Screen_Height-190,202,50);
            this.Chat_jt[Chat_jps].setBounds(0,this.Screen_Height-190,this.Screen_Width-200,50);

            this.Chat_jsp[Chat_jps].setViewportView(this.Chat_jsl[Chat_jps]);

            this.Chat_jl[Chat_jps].setOpaque( true);
            this.Chat_jl[Chat_jps].setHorizontalAlignment(SwingConstants.CENTER);
            this.Chat_jl[Chat_jps].setBorder(null);
            this.Chat_jl[Chat_jps].setBackground(new Color(30,30,30));
            this.Chat_jl[Chat_jps].setForeground(new Color(0,255,255));
            this.Chat_jl[Chat_jps].setFont(new Font("Helvetica",Font.PLAIN,25));

            this.Chat_jt[Chat_jps].setBackground(new Color(40,40,40));
            this.Chat_jt[Chat_jps].setFont(fnt);
            this.Chat_jt[Chat_jps].setForeground(new Color(0,255,255));
            this.Chat_jt[Chat_jps].setBorder(null);
            this.Chat_jt[Chat_jps].setHorizontalAlignment(SwingConstants.CENTER);

            this.Chat_jb[Chat_jps].setFont(fnt);
            this.Chat_jb[Chat_jps].setBorder(null);
            this.Chat_jb[Chat_jps].setHorizontalAlignment(SwingConstants.CENTER);
            this.Chat_jb[Chat_jps].setBackground(new Color(60,60,60));
            this.Chat_jb[Chat_jps].setForeground(new Color(0,255,255));

            this.Chat_jsp[Chat_jps].setViewportView(this.Chat_jsl[Chat_jps]);

            this.Chat_Cons_Y[Chat_jps] = 0;
            this.Chat_Console_Height[Chat_jps] = this.Console_Height;
            this.jtp[1].add(Client_ID,this.Chat_jp[Chat_jps]);
            System.out.println("Test");
            Chat_jps+=1;
            return -1;
        }
    // Application Graphical Processes
    public void Add_Components()
    {
        // Components Initialisation:
            jtp[0] = new Cust_JTabPn();
            this.jf.add(jtp[0]);
            for(int i=0;i<3;i++)
            {
                jp[i] = new JPanel();
                jp[i].setBorder(null);
                jp[i].setLayout(null);
                jtp[0].add(Tabs[i],jp[i]);
            }

            mjl[0] = new JLabel("General Chat Client Interface");
            mjl[1] = new JLabel()
            {          
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                    g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                    super.paintComponent(g2d);
                    g2d.setStroke(new BasicStroke(4.0f));
                    g2d.setColor(new Color(255,0,0));
                    g2d.drawArc(5,5, 40,30,30,120);
                    g2d.drawArc(12,12, 25,20,30,120);
                    g2d.drawLine(7, 14,25,30);
                    g2d.drawLine(44, 14,25,30);
                }
            };
            jf.add(mjl[0]);
            jf.add(mjl[1]);
    
            jsl[0] = new ScrollablePanel(new Dimension(this.Screen_Width,this.Screen_Height+3*this.Screen_Height/4-100));
            jsl[0].setLayout(null);
            jsl[0].setBorder(null);
            jsp[0] = new JScrollPane(jsl[0],JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jsp[0].setBorder(null);
            this.jp[0].add(jsp[0]);
            for(int i=0;i<13;i++)
            {
                jl[0][i] = new JLabel(this.clnt[i].replace('_', ' '));
            }
            // jl[0][3] = new JLabel("Server OFFLINE");

            for(int i=0;i<7;i++)
            {
                jt[0][i] = new JTextField();
                this.jsl[0].add(jt[0][i]);
            }

            for(int i=2;i<7;i++)
                jt[0][i].setEditable(false);
            jb[0][0] = new JButton(this.bts[0]);
            jb[0][1] = new JButton(this.bts[1]);
            jb[0][2] = new JButton(this.bts[2]);
            jsl[0].add(jb[0][0]);
            jsl[0].add(jb[0][1]);
            jsl[0].add(jb[0][2]);
            for(int i=0;i<13;i++)
            {
                this.jsl[0].add(jl[0][i]);
            }
            jsl[2] = new ScrollablePanel(new Dimension(this.Screen_Width,200));
            jsl[3] = new ScrollablePanel(new Dimension(this.Screen_Width,200));
            jsl[2].setLayout(null);
            jsl[3].setLayout(null);
            jsp[1] = new JScrollPane(jsl[2],JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jsp[2] = new JScrollPane(jsl[3],JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            this.jsl[0].add(jsp[1]);
            this.jsl[0].add(jsp[2]);

            jb[1][0] = new JButton(this.bts[3]);
            
            for(int i=0;i<5;i++)
            {
                jt[1][i] = new JTextField();
            }
            
            // Components Inclusion
            for(int i=0;i<5;i++)
            {
                this.jp[1].add(jt[1][i]);
            }
            jl[1][0] = new JLabel("User Profile");
            jp[1].add(jl[1][0]);
            for(int i=1;i<6;i++)
            {
                jl[1][i] = new JLabel(this.hd[i-1]);
                jp[1].add(jl[1][i]);
            }
            jp[1].add(jb[1][0]);
            
            
            jb[2][0] = new JButton(this.bts[4]);
            jp[2].add(jb[2][0]);
         
            jsl[1] = new ScrollablePanel(new Dimension(this.Screen_Width,this.Console_Height));
            jsl[1].setLayout(null);
            jsp[1] = new JScrollPane(jsl[1],JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jl[2][0] = new JLabel("Chat Console");
            jt[2][0] = new JTextField();
            jtp[1] = new Cust_JTabPn();
            this.jp[2].add(jtp[1]);
            //jtp[1].add(Tabs[0],jp[0]);
            //this.jp[2].add(jl[2][0]);
            //this.jp[2].add(jsp[1]);
            //this.jp[2].add(jt[2][0]);
            //jt[2][0].setEnabled(false);
            //this.jp[2].add(jb[2][0]);
            //jb[2][0].setEnabled(false);
            //System.out.println(jsl[0].getComponent(0));
            this.Position_Components();
    }
   
    public void Position_Components()
    {
        /*
        // Arbitrary Message Inclusion for Console UI
        for(int i=0;i<30;i++)
        {
            tml1 = new JLabel("mw");
                tml1.setHorizontalAlignment(SwingConstants.CENTER);
                tml1.setOpaque(true);
                tml1.setFont(new Font("Helvetica",Font.PLAIN,20));
                tml1.setBackground(new Color(50,50,50));
                tml1.setForeground(new Color(0,255,255));
                this.jsl[1].add(tml1);
                tml1.setBounds(0,Cons_Y,300,40); 
                tml2 = new JLabel("m");
                tml2.setHorizontalAlignment(SwingConstants.CENTER);
                tml2.setOpaque(true);
                tml2.setFont(new Font("Helvetica",Font.PLAIN,20));
                tml2.setBackground(new Color(20,20,20));
                tml2.setForeground(new Color(0,255,255));
                this.jsl[1].add(tml2);
                tml2.setBounds(300,Cons_Y,this.Screen_Width-500,40); 
                Cons_Y+=44;
        }
        */
        jtp[0].setBounds(0,10,this.Screen_Width, this.Screen_Height-10);
        jtp[1].setBounds(0,0,this.Screen_Width, this.Screen_Height-110);
        mjl[0].setBounds(0,0,this.Screen_Width-50,40);
        mjl[1].setBounds(this.Screen_Width-50,0,50,40);
        jl[0][0].setBounds(0,0,this.Screen_Width-210,50);
        jl[0][1].setBounds(0,52,this.Screen_Width/4-5,50);
        jl[0][2].setBounds(0,104,this.Screen_Width/4-5,50);
        jl[0][3].setBounds(this.Screen_Width-210,0,190,50);
        jl[0][4].setBounds(0,156,this.Screen_Width-20,50);
        jl[0][5].setBounds(0,208,this.Screen_Width/2-10,50);
        jl[0][6].setBounds(0,260,this.Screen_Width/4-5,50);
        jl[0][7].setBounds(0,312,this.Screen_Width/4-5,50);
        jl[0][8].setBounds(this.Screen_Width/2-10,260,this.Screen_Width/4-5,50);
        jl[0][9].setBounds(this.Screen_Width/2-10,312,this.Screen_Width/4-5,50);
        jl[0][10].setBounds(0,364,this.Screen_Width-20,50);
        jl[0][11].setBounds(0,416,this.Screen_Width-20,30);
        jl[0][12].setBounds(0,450+3*this.Screen_Height/8,this.Screen_Width-20,30);
        jt[0][0].setBounds(this.Screen_Width/4-5,52,this.Screen_Width/4-5,50); 
        jt[0][1].setBounds(this.Screen_Width/4-5,104,this.Screen_Width/4-5,50); 
        jt[0][2].setBounds(this.Screen_Width/2-10,208,this.Screen_Width/2-10,50); 
        jt[0][3].setBounds(this.Screen_Width/4-5,260,this.Screen_Width/4-5,50); 
        jt[0][4].setBounds(this.Screen_Width/4-5,312,this.Screen_Width/4-5,50); 
        jt[0][5].setBounds(3*this.Screen_Width/4-15,260,this.Screen_Width/4-5,50); 
        jt[0][6].setBounds(3*this.Screen_Width/4-15,312,this.Screen_Width/4-5,50); 
        jb[0][0].setBounds(this.Screen_Width/2-10,52,this.Screen_Width/2-10,50);
        jb[0][1].setBounds(this.Screen_Width/2-10,104,this.Screen_Width/4-5,50);
        jb[0][2].setBounds(3*this.Screen_Width/4-15,104,this.Screen_Width/4-5,50);
        jsp[0].setBounds(0,0,this.Screen_Width,this.Screen_Height);
        //jsp[1].setBounds(0,51,this.Screen_Width,this.Screen_Height-243);
        jsp[1].setBounds(5,448,this.Screen_Width-30,3*this.Screen_Height/8);
        jsp[2].setBounds(5,482+3*this.Screen_Height/8,this.Screen_Width-30,3*this.Screen_Height/8);
        jl[1][0].setBounds(0,0,this.Screen_Width,40);
        for(int i=1,y = 50;i<6;i++,y+=70)
        {
            jl[1][i].setBounds(0,y,this.Screen_Width/2,60);
            jt[1][i-1].setBounds(this.Screen_Width/2,y,this.Screen_Width/2,60);
        }
        jb[1][0].setBounds(520,450,500,50);
        //jl[2][0].setBounds(0,0,this.Screen_Width,40);
        //jb[2][0].setBounds(this.Screen_Width-100,this.Screen_Height-190,100,50);
        //jt[2][0].setBounds(0,this.Screen_Height-190,this.Screen_Width-100,50);
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
            this.jtp[0].setBorder(null);
            this.jtp[0].setFont(new Font("Helvetica",Font.PLAIN,20));
            this.jtp[0].setBackground(new Color(40,40,40));
            this.jtp[0].setForeground(new Color(0,255,255));
            this.jtp[1].setBorder(null);
            this.jtp[1].setFont(new Font("Helvetica",Font.PLAIN,20));
            this.jtp[1].setBackground(new Color(40,40,40));
            this.jtp[1].setForeground(new Color(0,255,255));
        for(int i=0;i<3;i++)
        {
            this.jp[i].setBackground(new Color(0,0,0));
        } 
        jsl[0].setBackground(new Color(0,0,0));
        jsl[0].setBorder(null);
        jsl[1].setBackground(new Color(0,0,0));
        jsl[2].setBackground(new Color(20,20,20));
        jsl[3].setBackground(new Color(20,20,20));
        //this.jsl[1].setContentSize(new Dimension(this.Screen_Width,1000));
        try
        {
            //System.out.println(jn.Read_json("Server_Config").get("Port"));
            //jt[0].setText("0");
            //jt[1].setText("1");
            //jsn1 = jn.Read_json("Resources\\Server_Config");
            //jt[0].setText((String)(jsn1.get("Server_IP")));
            //jt[1].setText((String)(jn.Read_json("Server_Config").get("Server_IP")));
            //jt[1].setText(String.valueOf((jsn1.get("Port"))));
            //jt[2].setText(String.valueOf((jsn1.get("Max_Client_Count"))));
            
            for(int i=0;i<2;i++)
            {
                System.out.println(String.valueOf(conn.get(this.clnt[i+1])));
                jt[0][i].setText(String.valueOf(conn.get(this.clnt[i+1])));
            }
            for(int i=0;i<5;i++)
            {
                t = String.valueOf(usr.get(this.cl_flds[i]));
                jt[1][i].setText(String.valueOf(t));
                this.vals[i] = String.valueOf(t);
            }
            jt[1][0].setText("<Server OFFLINE>");
        }
        catch(Exception e)
        {
            System.out.println("\nClient:"+e);
        }

        mjl[0].setHorizontalAlignment(SwingConstants.CENTER);
        mjl[0].setOpaque(true);
        mjl[0].setBackground(new Color(20,20,20));
        mjl[0].setForeground(new Color(0,255,255));
        mjl[0].setFont(new Font("Helvetica",Font.PLAIN,30));

        mjl[1].setOpaque(true);
        mjl[1].setBackground(new Color(0,0,0));

        jl[0][0].setOpaque( true);
        jl[0][0].setHorizontalTextPosition(JLabel.CENTER);
        jl[0][0].setHorizontalAlignment(SwingConstants.CENTER);
        jl[0][0].setBorder(null);
        jl[0][0].setForeground(new Color(0,255,255));
        jl[0][0].setBackground(new Color(50,50,50));
        for(int i=1;i<11;i++)
        {
            jl[0][i].setOpaque( true);
            jl[0][0].setFont(new Font("Helvetica",Font.PLAIN,30));
            jl[0][i].setHorizontalAlignment(SwingConstants.CENTER);
            jl[0][i].setBorder(null);
            jl[0][i].setBackground(new Color(30,30,30));
            jl[0][i].setForeground(new Color(0,255,255));
            jl[0][i].setFont(fnt);
        }
        for(int i=11;i<13;i++)
        {
            jl[0][i].setOpaque( true);
            jl[0][0].setFont(new Font("Helvetica",Font.PLAIN,30));
            jl[0][i].setHorizontalAlignment(SwingConstants.CENTER);
            jl[0][i].setBorder(null);
            jl[0][i].setBackground(new Color(40,40,40));
            jl[0][i].setForeground(new Color(0,255,255));
            jl[0][i].setFont(fnt);
        }
        jl[0][3].setHorizontalAlignment(SwingConstants.CENTER);
        jl[0][3].setFont(fnt);
        jl[0][3].setOpaque( true);
        jl[0][3].setBackground(new Color(255,0,0));
        jl[0][3].setForeground(new Color(0,0,0));

        jl[0][4].setOpaque( true);
        jl[0][4].setHorizontalAlignment(SwingConstants.CENTER);
        jl[0][4].setBorder(null);
        jl[0][4].setBackground(new Color(50,50,50));
        jl[0][4].setForeground(new Color(0,255,255));
        jl[0][4].setFont(new Font("Helvetica",Font.PLAIN,30));

        jl[0][10].setOpaque( true);
        jl[0][10].setHorizontalAlignment(SwingConstants.CENTER);
        jl[0][10].setBorder(null);
        jl[0][10].setBackground(new Color(50,50,50));
        jl[0][10].setForeground(new Color(0,255,255));
        jl[0][10].setFont(new Font("Helvetica",Font.PLAIN,30));

        jl[1][0].setOpaque( true);
        jl[1][0].setHorizontalAlignment(SwingConstants.CENTER);
        jl[1][0].setBorder(null);
        jl[1][0].setBackground(new Color(50,50,50));
        jl[1][0].setForeground(new Color(0,255,255));
        jl[1][0].setFont(new Font("Helvetica",Font.PLAIN,23));
        for(int i=1;i<6;i++)
        {
            jl[1][i].setOpaque( true);
            jl[1][i].setHorizontalAlignment(SwingConstants.CENTER);
            jl[1][i].setBorder(null);
            jl[1][i].setBackground(new Color(30,30,30));
            jl[1][i].setForeground(new Color(0,255,255));
            jl[1][i].setFont(new Font("Helvetica",Font.PLAIN,27));
        }
        jl[2][0].setOpaque( true);
        jl[2][0].setHorizontalAlignment(SwingConstants.CENTER);
        jl[2][0].setBorder(null);
        jl[2][0].setBackground(new Color(30,30,30));
        jl[2][0].setForeground(new Color(0,255,255));
        jl[2][0].setFont(new Font("Helvetica",Font.PLAIN,25));

        for(int i=0;i<7;i++)
        {
            jt[0][i].setBackground(new Color(40,40,40));
            jt[0][i].setFont(fnt);
            jt[0][i].setForeground(new Color(0,255,255));
            jt[0][i].setBorder(null);
            jt[0][i].setHorizontalAlignment(SwingConstants.CENTER);
        }
        for(int i=0;i<5;i++)
        {
            this.jt[0][i+2].setText("_______________");
        }
        for(int i=0;i<5;i++)
        {
            jt[1][i].setBackground(new Color(40,40,40));
            jt[1][i].setFont(new Font("Helvetica",Font.PLAIN,27));
            jt[1][i].setForeground(new Color(0,255,255));
            jt[1][i].setBorder(null);
            jt[1][i].setHorizontalAlignment(SwingConstants.CENTER);
        }
        jt[2][0].setBackground(new Color(40,40,40));
        jt[2][0].setFont(fnt);
        jt[2][0].setForeground(new Color(0,255,255));
        jt[2][0].setBorder(null);
        jt[2][0].setHorizontalAlignment(SwingConstants.CENTER);

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

        jb[2][0].setFont(fnt);
        jb[2][0].setBorder(null);
        jb[2][0].setHorizontalAlignment(SwingConstants.CENTER);
        jb[2][0].setBackground(new Color(60,60,60));
        jb[2][0].setForeground(new Color(0,255,255));


        //this.jsl[1].setAutoscrolls(true);
        //jsp[1].getViewport().setBackground(new Color(40,40,40));
        jsp[1].setBorder(null);
        jsp[1].setBorder(null);
        jsp[2].setBorder(null);
        jsp[1].setViewportView(jsl[1]);
        jsp[1].setViewportView(jsl[2]);
        jsp[2].setViewportView(jsl[3]);
        //jta[0].setFont(new Font("Courier New",Font.PLAIN,20));
        //jta[0].setBackground(new Color(0,10,10));
        //jta[0].setForeground(new Color(0,255,255));
        this.Add_Events();
    }
    public void Add_Events()
    {
        jf.addWindowListener(this);
        for(int i=0;i<3;i++)
            jb[0][i].addActionListener(this);
        jb[1][0].addActionListener(this);
        jb[2][0].addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        String t1="",t2="";
        t1 = e.getActionCommand();
        System.out.println(e.getActionCommand());
        if(t1.substring(0,4).equals("Send"))
        {
                System.out.println("C1");
                int l=-1;
                t1 = t1.substring(5,t1.length());
                System.out.println(t1);
                for(int i=0;i<this.Chat_jps;i++)
                {
                    if(t1.equals(Clients[i]))
                    {
                        l = i;
                    }
                }
                //l = this.cl.cs.Max_Length();
                t2 = (Chat_jt[l]).getText();
                //this.oup = new Server_Output_Thread(this.cs,t1);
                //if(t1.length()>0)
                //{
                //    t2 = String.format("%"+-this.cl.pad+"s%s",this.cl.Get_Client_Name()," >  "+t1);
                //    (this.jta[0]).setText((this.jta[0]).getText()+t2+"\n");
                //    this.cl.Write_Text(t1);
                //}
                ////System.out.println("C2");
                //tml = new JLabel(this.cl.Get_Client_Name());
                tml1 = new JLabel(this.cl.Client_ID.fv.get(this.cl.Client_ID.flds[1]));
                tml1.setHorizontalAlignment(SwingConstants.CENTER);
                tml1.setOpaque(true);
                tml1.setFont(new Font("Helvetica",Font.PLAIN,20));
                tml1.setBackground(new Color(70,70,70));
                tml1.setForeground(new Color(0,255,255));
                this.Chat_jsl[l].add(tml1);
                tml1.setBounds(0,this.Chat_Cons_Y[l],300,40); 
                tml2 = new JLabel(t2);
                tml2.setHorizontalAlignment(SwingConstants.CENTER);
                tml2.setOpaque(true);
                tml2.setFont(new Font("Helvetica",Font.PLAIN,20));
                tml2.setBackground(new Color(40,40,40));
                tml2.setForeground(new Color(0,255,255));
                this.Chat_jsl[l].add(tml2);
                //jsl[1].setBounds(0, 0, this.Screen_Width,this.Screen_Height-400);
                Chat_jsl[l].revalidate();
                tml2.setBounds(300,this.Chat_Cons_Y[l],this.Screen_Width-320,40); 
                this.Chat_Cons_Y[l]+=50;
                //System.out.println(String.valueOf(this.Cons_Y)+" "+String.valueOf(this.Console_Height)+" "+String.valueOf(this.Screen_Width-243));
                if(this.Chat_Cons_Y[l]>this.Chat_Console_Height[l])
                {
                    //System.out.println("meow");
                    this.Chat_Console_Height[l]+=50;
                    this.Chat_jsl[l].setContentSize(new Dimension(this.Screen_Width,this.Chat_Console_Height[l]));
                    JScrollBar vertical = Chat_jsp[l].getVerticalScrollBar();
                    System.out.println(String.valueOf( vertical.getValue()));
                    vertical.setValue( vertical.getValue()+50);
                }
                this.cl.Forward_Message(t1,t2);
                //this.jsp[1].repaint();
        }
        else if((t1.substring(0,4)).equals("Chat"))
        {
            if(this.Create_Chat_Console(t1.substring(5,t1.length()))==-1)
            {
                System.out.println("Chat Console with "+t1.substring(5,t1.length())+" Created");
            }
            else
                System.out.println("Chat Console with "+t1.substring(5,t1.length())+" already exists");
        }
        else if(t1 == "Connect")
        {
            if(!this.Server_Conn)
                this.Server_Conn = true;
        }
        else if(t1 == "Disconnect")
        {
            if(this.Server_Conn)
            {
                this.Server_Conn = false;
                if(this.cl!=null)
                {
                    this.Disconnect_Server();
                }
            }
        }
        else if(t1 == "Update User Details")
        {
            for(int i=0;i<5;i++)
            {
                this.vals[i] = this.jt[1][i].getText();
                //conn = jn.Read_json("Connection");
                //this.conn.put();
            }
            
            if(this.cl!=null)
            {
                for(int i=0;i<5;i++)
                    this.cl.Client_ID.fv.put(this.flds[i],this.jt[1][i].getText());
            }// System.out.println(this.vals[0]);
            // System.out.println(this.vals[1]);
            // System.out.println(this.vals[2]);
        }
        
    }
    @Override
    public void windowOpened(WindowEvent e) {
        
    }
    @Override
    public void windowClosing(WindowEvent e) {

    }
    @Override
    public void windowClosed(WindowEvent e) {
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
