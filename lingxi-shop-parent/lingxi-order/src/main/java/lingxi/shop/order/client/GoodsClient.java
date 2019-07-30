package lingxi.shop.order.client;


import lingxi.shop.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author bystander
 * @date 2018/10/4
 */
@FeignClient(value = "lingxi-shop-item-service")
public interface GoodsClient extends GoodsApi {
}
