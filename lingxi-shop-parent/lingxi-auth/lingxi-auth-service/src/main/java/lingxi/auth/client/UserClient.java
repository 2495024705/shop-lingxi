package lingxi.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import shop.lingxi.user.api.UserApi;

@FeignClient("LINGXI-SHOP-USER-SERVICE")
public interface UserClient extends UserApi {

}
