package lingxi.lingxi.sms;

import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private SmsListener smsListener;
    @org.junit.Test
    public void testsend() throws InterruptedException {
        Map<String, String> map = new HashMap<>();
        map.put("phone", "17734671027");
        map.put("code","548795");
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",map);
       // smsListener.listenVerifyCode();
Thread.sleep(10000L);
    }
}

