import org.junit.Test;

/**
 * Created by asus on 2017/10/17.
 */
public class JUOS {
    @Test
    public void test(){
        String os = System.getProperty("os.name");
        if (os.contains("Windows")){
            System.out.println(os);
        }else {
            System.out.println("no win");
        }
    }
}
