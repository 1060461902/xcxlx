import com.e.support.util.Create3rdSessionID;
import org.junit.Test;

public class SessionIDTest {
    @Test
    public void test(){
        System.out.println(Create3rdSessionID.createByUnix());
    }
}
