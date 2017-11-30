import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by asus on 2017/11/30.
 */
public class MailTest {
    @Test
    public void sendMail(){
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet get = new HttpGet("http://localhost:8080/wx/order/mail.wx?order_id=akslud");
            httpClient.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
