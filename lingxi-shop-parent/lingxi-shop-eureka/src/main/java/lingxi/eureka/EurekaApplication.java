package lingxi.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by 宝宝 on 2019/3/30.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
 public static void main(String[] args){
SpringApplication.run(EurekaApplication.class);
 }
}
