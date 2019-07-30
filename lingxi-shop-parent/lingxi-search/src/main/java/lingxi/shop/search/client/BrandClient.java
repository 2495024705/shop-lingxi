package lingxi.shop.search.client;

import lingxi.shop.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("lingxi-shop-item-service")
public interface BrandClient extends BrandApi {
}
