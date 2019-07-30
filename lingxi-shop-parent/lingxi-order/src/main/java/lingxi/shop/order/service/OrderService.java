package lingxi.shop.order.service;

import lingxi.auth.entity.UserInfo;
import lingxi.shop.common.enums.ExceptionEnum;
import lingxi.shop.common.exception.lingxiException;
import lingxi.shop.common.utils.JsonUtils;
import lingxi.shop.item.pojo.Sku;
import lingxi.shop.order.client.AddressClient;
import lingxi.shop.order.client.GoodsClient;
import lingxi.shop.order.dto.AddressDTO;
import lingxi.shop.order.dto.OrderDto;
import lingxi.shop.order.dto.OrderStatusEnum;
import lingxi.shop.order.interceptor.UserInterceptor;
import lingxi.shop.order.mapper.OrderDetailMapper;
import lingxi.shop.order.mapper.OrderMapper;
import lingxi.shop.order.mapper.OrderStatusMapper;
import lingxi.shop.order.pojo.Order;
import lingxi.shop.order.pojo.OrderDetail;
import lingxi.shop.order.pojo.OrderStatus;
import lingxi.shop.order.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author bystander
 * @date 2018/10/4
 */
@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private IdWorker idWorker;

//    @Autowired
//    private PayHelper payHelper;

    @Autowired
    private PayLogService payLogService;

    @Autowired
    private AmqpTemplate amqpTemplate;


    @Transactional
    public Long createOrder(OrderDto orderDto) {
        //生成订单ID，采用自己的算法生成订单ID
        long orderId = idWorker.nextId();

        //填充order，订单中的用户信息数据从Token中获取，填充到order中
        Order order = new Order();
        order.setCreateTime(new Date());
        order.setOrderId(orderId);
        order.setPaymentType(orderDto.getPaymentType());
        order.setPostFee(0L);  //// TODO 调用物流信息，根据地址计算邮费

        //获取用户信息
        UserInfo user = UserInterceptor.getLoginUser();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getName());
        order.setBuyerRate(false);  //卖家为留言

        //收货人地址信息，应该从数据库中物流信息中获取，这里使用的是假的数据
        AddressDTO addressDTO = AddressClient.findById(orderDto.getAddressId());
        if (addressDTO == null) {
            // 商品不存在，抛出异常
            throw new lingxiException(ExceptionEnum.RECEIVER_ADDRESS_NOT_FOUND);
        }
        order.setReceiver(addressDTO.getName());
        order.setReceiverAddress(addressDTO.getAddress());
        order.setReceiverCity(addressDTO.getCity());
        order.setReceiverDistrict(addressDTO.getDistrict());
        order.setReceiverMobile(addressDTO.getPhone());
        order.setReceiverZip(addressDTO.getZipCode());
        order.setReceiverState(addressDTO.getState());


        //付款金额相关，首先把orderDto转化成map，其中key为skuId,值为购物车中该sku的购买数量
        Map<Long, Integer> skuNumMap = orderDto.getCarts().stream().collect(Collectors.toMap(c -> c.getSkuId(), c -> c.getNum()));

        //查询商品信息，根据skuIds批量查询sku详情
        List<Sku> skus = goodsClient.querySkuBySpuId(new ArrayList<>(skuNumMap.keySet()));

        if (CollectionUtils.isEmpty(skus)) {
            throw new lingxiException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        Double totalPay = 0.0;

        //填充orderDetail
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();

        //遍历skus，填充orderDetail
        for (Sku sku : skus) {
            Integer num = skuNumMap.get(sku.getId());
            totalPay += num * sku.getPrice();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setOwnSpec(sku.getOwnSpec());
            orderDetail.setSkuId(sku.getId());
            orderDetail.setTitle(sku.getTitle());
            orderDetail.setNum(num);
            orderDetail.setPrice(sku.getPrice().longValue());
            orderDetail.setImage(StringUtils.substringBefore(sku.getImages(), ","));

            orderDetails.add(orderDetail);
        }

        order.setActualPay((totalPay.longValue() + order.getPostFee()));  //todo 还要减去优惠金额
        order.setTotalPay(totalPay.longValue());

        //保存order
        orderMapper.insertSelective(order);

        //保存detail
        orderDetailMapper.insertList(orderDetails);


        //填充orderStatus
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.INIT.value());
        orderStatus.setCreateTime(new Date());

        //保存orderStatus
        orderStatusMapper.insertSelective(orderStatus);

        //减库存
       //goodsClient.decreaseStock(orderDto.getCarts());


        //todo 删除购物车中已经下单的商品数据, 采用异步mq的方式通知购物车系统删除已购买的商品，传送商品ID和用户ID
        HashMap<String, Object> map = new HashMap<>();
        try {
            map.put("skuIds", skuNumMap.keySet());
            map.put("userId", user.getId());
            amqpTemplate.convertAndSend("ly.cart.exchange", "cart.delete", JsonUtils.serialize(map));
        } catch (Exception e) {
            log.error("删除购物车消息发送异常，商品ID：{}", skuNumMap.keySet(), e);
        }

        log.info("生成订单，订单编号：{}，用户id：{}", orderId, user.getId());
        return orderId;

    }

//    public String generateUrl(Long orderId) {
//        //根据订单ID查询订单
//        Order order = queryById(orderId);
//        //判断订单状态
//        if (order.getOrderStatus().getStatus() != OrderStatusEnum.INIT.value()) {
//            throw new LyException(ExceptionEnum.ORDER_STATUS_EXCEPTION);
//        }
//
//        //todo 这里传入一份钱，用于测试使用，实际中使用订单中的实付金额
//        St ring url = payHelper.createPayUrl(orderId, "灵溪商城测试", /*order.getActualPay()*/1L);
//        if (StringUtils.isBlank(url)) {
//            throw new LyException(ExceptionEnum.CREATE_PAY_URL_ERROR);
//        }
//
//        //生成支付日志
//        payLogService.createPayLog(orderId, order.getActualPay());
//
//        return url;
//
//    }
//
    public Order queryById(Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            throw new lingxiException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.select(orderDetail);
        order.setOrderDetails(orderDetails);
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        if (orderStatus == null) {
            throw new lingxiException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        order.setOrderStatus(orderStatus);
        return order;
    }

//    @Transactional
//    public void handleNotify(Map<String, String> msg) {
//        payHelper.handleNotify(msg);
//    }

//    public PageResult<Order> queryOrderByPage(Integer page, Integer rows) {
//
//        //开启分页
//        PageHelper.startPage(page, rows);
//
//        Example example = new Example(Order.class);
//
//        //查询订单
//        List<Order> orders = orderMapper.selectByExample(example);
//
//
//        //查询订单详情
//        for (Order order : orders) {
//            OrderDetail orderDetail = new OrderDetail();
//            orderDetail.setOrderId(order.getOrderId());
//            List<OrderDetail> orderDetailList = orderDetailMapper.select(orderDetail);
//
//            order.setOrderDetails(orderDetailList);
//
//            //查询订单状态
//            OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(order.getOrderId());
//            order.setOrderStatus(orderStatus);
//        }
//
//        PageInfo<Order> pageInfo = new PageInfo<>(orders);
//
//        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList());
//    }
}
