package shop.lingxi.service;

import lingxi.shop.common.enums.ExceptionEnum;
import lingxi.shop.common.exception.lingxiException;
import lingxi.shop.common.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import shop.lingxi.mapper.UserMapper;
import shop.lingxi.user.pojo.User;
import shop.lingxi.utils.CodecUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;
    private static final String KEY_PERFIX = "user:verify:phone";

    public  void register(User user, String code) {
        //校验验证码
        String s = redisTemplate.opsForValue().get(key + user.getPhone());

        //对密码加密
        if (!StringUtils.equals(code, s)){
            throw new lingxiException(ExceptionEnum.USER_CODE_NOT);
        }
        String salt=CodecUtils.generateSalt();
        user.setSalt(salt);
       // CodecUtils.md5Hex(user.getPassword(),salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));

        //写入数据库
        user.setCreated(new Date());
        userMapper.insert(user);

    }
    static String key=KEY_PERFIX;
    public void sendCode(String phone) {

        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        String code= NumberUtils.generateCode(6);
       // code="520521";
        map.put("code", code);
       amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code", map);
        System.out.println(code+"          ffffffffffffffffffffffff");
        redisTemplate.opsForValue().set(key+phone, code,5, TimeUnit.MINUTES);
    }


    public Boolean checkData(String data, Integer type) {
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new lingxiException(ExceptionEnum.USER_NOT_TYPE);

        }

        int user1 = userMapper.selectCount(user);

        return user1 == 0;
    }

    public User queryUserByUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        User user1 = userMapper.selectOne(user);
        if (user1==null){
            throw new lingxiException(ExceptionEnum.USER_LOGIN_NOT);
        }
        if (!StringUtils.equals(user1.getPassword(), CodecUtils.md5Hex(password, user1.getSalt()))){
            throw new lingxiException(ExceptionEnum.USER_LOGIN_PASSWORD_ERROR);
        }
       return user1;


    }
}
