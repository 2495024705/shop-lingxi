package lingxi.shop.page.client;

import lingxi.shop.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("lingxi-shop-item-service")
public interface GoodsClient extends GoodsApi {
}
