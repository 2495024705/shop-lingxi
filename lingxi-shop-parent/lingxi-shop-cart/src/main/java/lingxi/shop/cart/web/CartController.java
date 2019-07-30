package lingxi.shop.cart.web;

import lingxi.shop.cart.pojo.Cart;
import lingxi.shop.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author bystander
 * @date 2018/10/3
 */
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加商品到购物车
     *
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }


    /**
     * 从购物车中删除商品
     *
     * @param id
     * @return
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") Long skuId) {
        cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }


    /**
     * 更新购物车中商品的数量
     *
     * @param id  商品ID
     * @param num 修改后的商品数量
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num) {
        cartService.updateNum(skuId, num);
        return ResponseEntity.ok().build();
    }


    /**
     * 查询购物车
     *
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Cart>> listCart() {
        return ResponseEntity.ok(cartService.listCart());
    }


}
