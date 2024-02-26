import java.io.FileNotFoundException;
import java.io.PrintWriter;
  
public class temp
{
    public static void main(String[] args) throws FileNotFoundException 
    {
        String t1,t2,t3;
        t1 = String.format("%"+20+"s %s","Shivram","meow");
        System.out.println(t1);
        t1 = String.format("%"+20+"s %s","Shivram_haildwqe","meow");
        System.out.println(t1);
        t1 = String.format("%"+20+"s %s","Sh","meow");
        System.out.println(t1);
        t1 = String.format("%"+20+"s %s","Shivram_hail","meow");
        System.out.println(t1);
        t1 = String.format("%"+-20+"s %s","Shivram","meow");
        System.out.println(t1);
        t1 = String.format("%"+-20+"s %s","Shivram_haildwqe","meow");
        System.out.println(t1);
        t1 = String.format("%"+-20+"s %s","Sh","meow");
        System.out.println(t1);
        t1 = String.format("%"+-20+"s %s","Shivram_hail","meow");
        System.out.println(t1);
    }
}