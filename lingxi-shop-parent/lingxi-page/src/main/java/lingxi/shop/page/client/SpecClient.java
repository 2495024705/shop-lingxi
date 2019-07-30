package lingxi.shop.page.client;

import lingxi.shop.item.api.SpecApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("lingxi-shop-item-service")
public interface SpecClient extends SpecApi {
}
