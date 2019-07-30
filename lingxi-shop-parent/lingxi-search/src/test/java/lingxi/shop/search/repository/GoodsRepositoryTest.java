package lingxi.shop.search.repository;

import lingxi.shop.common.vo.PageResult;
import lingxi.shop.item.pojo.Spu;
import lingxi.shop.search.client.GoodsClient;
import lingxi.shop.search.pojo.Goods;
import lingxi.shop.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private SearchService searchService;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void test1() {
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void LoadData() {
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            PageResult<Spu> spuPageResult = goodsClient.querySpuByPage(page, rows, true, null);
            List<Spu> items = spuPageResult.getItems();
            List<Goods> collect = items.stream().map(searchService::queryList).collect(Collectors.toList());
            goodsRepository.saveAll(collect);
            page++;
            size = items.size();

        } while (size == 100);
    }
}