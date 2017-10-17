import com.e.dao.redis.RedisSaveSession;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;

public class RedisTest {
    @Test
    public void test() throws IOException {
        RedisSaveSession saveSession = new RedisSaveSession();
        assertTrue(saveSession.save("w","cacas",30));
    }
}
