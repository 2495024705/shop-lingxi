package lingxi.shop.page.client;

import lingxi.shop.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("lingxi-shop-item-service")
public interface CategoryClient extends CategoryApi {

}
