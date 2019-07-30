package lingxi.shop.page.Test;

import lingxi.shop.page.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PageTest {
    @Autowired
    private PageService service;
    @Test
    public void  test1(){
        service.createHtml(141L);

    }}
