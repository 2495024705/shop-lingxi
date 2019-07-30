package lingxi.shop.item.web;

import lingxi.shop.common.vo.PageResult;
import lingxi.shop.item.pojo.CartDto;
import lingxi.shop.item.pojo.Sku;
import lingxi.shop.item.pojo.Spu;
import lingxi.shop.item.pojo.SpuDetail;
import lingxi.shop.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
@RequestMapping(value = "/spu/page",method = RequestMethod.GET)
    public ResponseEntity<PageResult<Spu>> querySpuByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,@RequestParam(value = "rows",defaultValue = "5") Integer rows,@RequestParam(value = "saleable",required = false) Boolean saleable,@RequestParam(value = "key",required = false) String key){
return ResponseEntity.ok(goodsService.querySpuByPage(page,rows,saleable,key));
}
@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu){
    goodsService.saveGoods(spu);
    return ResponseEntity.status(HttpStatus.OK).build();
}
@RequestMapping(value = "/spu/detail/{id}",method = RequestMethod.GET)
    public ResponseEntity<SpuDetail> queryDetailById(@PathVariable("id") Long id){

    return ResponseEntity.ok(goodsService.queryDetailById(id));
}
    @RequestMapping(value = "/spu/list",method = RequestMethod.GET)
    public ResponseEntity<List<Sku>> querySpuList(@RequestParam("id") Long id){

        return ResponseEntity.ok(goodsService.querySpuList(id));
    }
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> upDateGoods(@RequestBody Spu spu){
    goodsService.updateGoods(spu);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @RequestMapping(value = "/spu/{id}",method = RequestMethod.GET)
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
return ResponseEntity.ok(goodsService.querySpuById(id));
    }
    @GetMapping("/sku/list/ids")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("ids") List<Long> ids){
    return ResponseEntity.ok(goodsService.querySkuBySpuId(ids));
    }
    @PostMapping("/sku/decreaseStock")
    public ResponseEntity<Void> decreaseStock(@RequestBody List<CartDto> carts){
        goodsService.decreaseStock(carts);
    return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
