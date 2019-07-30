package lingxi.shop.search.mq.Listen;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Listen {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.insert.queue", durable = "true"),
            exchange = @Exchange(
                    value = "spring.test.exchange",
                    type = ExchangeTypes.TOPIC

            ),
            key = {"item.update"}
    ))
    public void listen(String msg) {
        System.out.println(msg);
    }
}
