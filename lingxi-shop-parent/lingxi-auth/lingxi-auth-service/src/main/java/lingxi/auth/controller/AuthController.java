package lingxi.auth.controller;

import lingxi.auth.entity.UserInfo;
import lingxi.auth.properties.JwtProperties;
import lingxi.auth.service.AuthService;
import lingxi.auth.utils.JwtUtils;
import lingxi.shop.common.enums.ExceptionEnum;
import lingxi.shop.common.exception.lingxiException;
import lingxi.shop.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties prop;

    /**
     * 登录授权
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) {
        //登录
       // response.setHeader("Access-Control-Allow-Origin", "*");

        String token = authService.login(username, password);
        //写入cookie
        CookieUtils.setCookie(request, response, prop.getCookieName(),
                token, prop.getCookieMaxAge(), true);
        return ResponseEntity.ok(token);
    }

@GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN") String token, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(token)) {
            throw new lingxiException(ExceptionEnum.NO_AUTH);
        }
        try {
            UserInfo userInfo = JwtUtils.getUserInfo(prop.getPublicKey(), token);
            String newToken = JwtUtils.generateToken(userInfo, prop.getPrivateKey(), prop.getExpire());
            CookieUtils.setCookie(request, response, prop.getCookieName(), newToken, prop.getCookieMaxAge(),true);

            return ResponseEntity.ok(userInfo);
        }catch (Exception e){
            //已经过期或者没有授权
            throw new lingxiException(ExceptionEnum.NO_AUTH);

        }

    }
}
