package Chat_App_Server;
import java.util.*;

public class Update  extends Thread
{
    public int ord;
    public Server_GUI serv;
    Update(Server_GUI serv)    
    {   
        this.serv = serv;
        this.ord = 1;
    }
    @Override
    public void run()
    {
        while(ord == 1)
        {
            this.serv.Update_UI();
            this.serv.Update_Clients();
            try
            {
                this.sleep(1000);
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
        System.out.println("Update_UI Thread destroyed");
    }
}
