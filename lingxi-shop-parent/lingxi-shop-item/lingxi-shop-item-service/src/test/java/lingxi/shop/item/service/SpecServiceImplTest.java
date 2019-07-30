package lingxi.shop.item.service;

import lingxi.shop.item.pojo.CartDto;
import lingxi.shop.item.pojo.SpecGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author bystander
 * @date 2018/9/26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpecServiceImplTest {

    @Autowired
    private SpeccificationService speccificationService;
    @Autowired
    private GoodsService goodsService;
    @Test
    public void querySpecsByCid() {
        List<SpecGroup> groups = speccificationService.getListGroupList(76l);
        for (SpecGroup group : groups) {
            System.out.println(group);
        }
    }
    @Test
    public void pecsByCid() {
        speccificationService.DELETEGroup(76l);
    }
    @Test
    public void pecssByCid() {
        List<CartDto> cartDtos = Arrays.asList(new CartDto(2600242L, 2), new CartDto(2600248L, 2));

        goodsService.decreaseStock(cartDtos);
    }
}