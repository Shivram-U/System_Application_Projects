package Chat_App_Res;

import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.json.simple.JSONObject;

public class Json_Handler
{
    public String pwd,path = "\\Resources\\";
    public static void main(String[] args)
    {
        System.out.println("work");
    }
    public Json_Handler()
    {
        pwd = System.getProperty("user.dir");
        System.out.println(pwd);
        this.path = pwd+this.path;
    }
    public JSONObject Read_json(String filename)   throws Exception
    {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(this.path+filename+".json"));
        JSONObject jsonObject =  (JSONObject) obj;
        return jsonObject;
    }
    public void Write_Json(int file,String args[])  throws Exception
    {
        if(file == 1)
        {
            JSONObject jn = new JSONObject();
            jn.put("Server_IP",args[0]);
            jn.put("Port",Integer.parseInt(args[1]));
            PrintWriter pw = new PrintWriter("Server_Config.json");
            pw.write(jn.toJSONString());
            pw.flush();
            pw.close();
        }
    }
}