package lingxi.shop.search.client;

import lingxi.shop.item.pojo.SpuDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryClientTest {
    @Autowired
    private GoodsClient goodsClent;
    @Test
    public void queryClientTest() {
        SpuDetail spuDetail = goodsClent.queryDetailById(10l);
        System.out.println(spuDetail);

    }
}