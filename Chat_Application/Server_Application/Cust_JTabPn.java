package Chat_App_Res;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Cust_JTabPn extends JTabbedPane 
{
	//public Insets insets = UIManager.getInsets("TabbedPane.contentBorderInsets");
	
	public Cust_JTabPn(Component c)
	{
		super();
		removeAll();
		setBackground(c.getBackground());
		setForeground(c.getForeground());
		setBorder(null);
	}
	public Cust_JTabPn()
	{
		super();
		removeAll();
		setBorder(null);
	}
	public Cust_JTabPn(String[] nams,JPanel[] jps,int num,Color bck,Color fre,Font fnt)
	{
		super();
		removeAll();
		for(int i=0;i<num;i++)
        {
            jps[i].setLayout(null);
            add(nams[i],jps[i]);
        }
		setBackground(bck);
		setForeground(fre);
		setFont(fnt);
		setBorder(null);
	}
}
