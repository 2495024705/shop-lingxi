package lingxi.shop.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 文件名称: ItemServiceApplication.java
 * 编写人: yh.zeng
 * 编写时间: 2019/3/30 16:27
 * 文件描述: todo
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient

@MapperScan("lingxi.shop.item.mapper")
public class ItemServiceApplication {

    public static void main(String args[]) {
        SpringApplication.run(ItemServiceApplication.class);

    }

}
