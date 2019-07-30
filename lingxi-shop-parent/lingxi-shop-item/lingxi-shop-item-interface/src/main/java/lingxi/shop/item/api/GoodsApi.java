package lingxi.shop.item.api;

import lingxi.shop.common.vo.PageResult;
import lingxi.shop.item.pojo.CartDto;
import lingxi.shop.item.pojo.Sku;
import lingxi.shop.item.pojo.Spu;
import lingxi.shop.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/goods")
public interface GoodsApi {

    /**
     * 分页查询商品
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @RequestMapping(value = "/spu/page",method = RequestMethod.GET)
    PageResult<Spu> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", defaultValue = "true") Boolean saleable,
            @RequestParam(value = "key", required = false) String key);

    /**
     * 根据spu商品id查询详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/spu/detail/{id}",method = RequestMethod.GET)
    SpuDetail queryDetailById(@PathVariable("id") Long id);

    /**
     * 根据spu的id查询sku
     * @param id
     * @return
     */
    @RequestMapping(value = "/spu/list",method = RequestMethod.GET)
    public List<Sku> querySpuList(@RequestParam("id") Long id);
    @RequestMapping(value = "/spu/{id}",method = RequestMethod.GET)
    Spu querySpuById(@PathVariable("id") Long id);
    @GetMapping("/sku/list/ids")
    List<Sku> querySkuBySpuId(@RequestParam("ids") List<Long> ids);
//    decreaseStock
@PostMapping("/sku/decreaseStock")
void decreaseStock(@RequestBody List<CartDto> carts);
//    decreaseStock
}