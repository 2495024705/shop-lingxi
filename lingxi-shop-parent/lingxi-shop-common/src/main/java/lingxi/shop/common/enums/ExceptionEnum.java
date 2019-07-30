package lingxi.shop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    REICE_CANNOT_BE_NULL(400,"价格不能为空"),
    GOODS_NOT_SALEABLE(500,"商品未上架"),
    CATEGORY_NOTf_fOND(404,"品牌不存在"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    CATEFORY_BRAND_SAVE_ERROR(500,"新增品牌失败"),
    INVALID_FILE_TYPE(500,"图片类型不匹配"),
    SPEC_NOTf_fOND(500,"规格组修改失败"),
    SPEC_DELETE_fOND(500,"规格组删除失败"),
    SPEC_UPDATE_ERROR(500,"没有该id"),
    SPEC_SAVE_ERROR(500,"该规格组已存在"),
    GOODS_FIND_NOTFOUND(500,"商品不存在"),
    IMAGE_NOT_FOND(404,"图片无法找到"),
    SPU_INSET_ERROR(500,"新增商品失败"),
    STORE_NOT_FOUND(404,"商品库存不存在"),
    USER_NOT_TYPE(500,"传入类型不对"),
    USER_CODE_NOT(500,"验证码错误"),
    USER_LOGIN_NOT(500,"用户错误或不存在"),
    USER_LOGIN_PASSWORD_ERROR(500,"密码错误"),
    TOKEN_PRODUCT_ERROR(500,"token生成失败"),
    RECEIVER_ADDRESS_NOT_FOUND(500,"没有登录地址"),
    GOODS_NOT_FOUND(500,"商品不存在"),
    Stock_NOT_ERROR(500,"修改商品数量失败"),
    ORDER_NOT_FOUND(500,"订单不存在"),
    NO_AUTH(403,"未授权")
    ;
    //public static final ExceptionEnum RECEIVER_ADDRESS_NOT_FOUND = ;
    private  int code;
    private String msg;
}
