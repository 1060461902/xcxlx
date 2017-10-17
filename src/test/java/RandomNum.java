import com.e.support.util.Create3rdSessionID;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

/**
 * Created by asus on 2017/10/17.
 */
public class RandomNum {
    @Test
    public void test() throws NoSuchAlgorithmException {
        String os = System.getProperty("os.name");
        String thirdSessionId = "";
        if (os.contains("Windows")){
            thirdSessionId = Create3rdSessionID.createByOTH();
        }else {
            thirdSessionId = Create3rdSessionID.createByUnix();
        }
        System.out.println(thirdSessionId);
    }
}
