
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by asus on 2017/10/28.
 */
public class SLFTest {
    @Test
    public void test(){
        Logger logger = LoggerFactory.getLogger(SLFTest.class);
        logger.warn("debug");
    }
}
