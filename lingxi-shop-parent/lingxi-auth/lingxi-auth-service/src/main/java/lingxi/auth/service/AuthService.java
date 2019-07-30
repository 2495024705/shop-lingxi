package lingxi.auth.service;

import lingxi.auth.client.UserClient;
import lingxi.auth.entity.UserInfo;
import lingxi.auth.properties.JwtProperties;
import lingxi.auth.utils.JwtUtils;
import lingxi.shop.common.enums.ExceptionEnum;
import lingxi.shop.common.exception.lingxiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import shop.lingxi.user.pojo.User;
@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {
    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties properties;

    public String login(String username, String password) {
        try {
            User user = userClient.queryUserByUser(username, password);
            if (user == null) {
                throw new lingxiException(ExceptionEnum.USER_LOGIN_NOT);
            }
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), username), properties.getPrivateKey(), properties.getExpire());
            return token;
        } catch (Exception e) {
log.error("用户或密码错误", username,password);
            throw new lingxiException(ExceptionEnum.TOKEN_PRODUCT_ERROR);
        }

    }



}
