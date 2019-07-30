package lingxi.shop.order.enums;

public enum OrderStatusEnum {
    UN_PAY(1,"未付款"),
    PAYED(2,"已付款，未发货"),
    DELIVERED(3,"已发货，未确认"),
    SUCCESS(4,"已确认，交易完毕"),
    getUnPay(1,"未付款"),
    ;
    private int code;
    private String desc;

    OrderStatusEnum(int code,String desc){
        this.code=code;
        this.desc=desc;
    }

public int value(){
       return this.code;
}



}

