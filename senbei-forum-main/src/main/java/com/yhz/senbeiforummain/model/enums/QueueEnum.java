package com.yhz.senbeiforummain.model.enums;

/**
 *  rabbitmq 队列枚举类
 * @author yanhuanzhan
 * @date 2023/3/30 - 18:04
 */
public enum QueueEnum {
    /**
     * 订单取消死信队列
     */
    QUEUE_ORDER_CANCEL("order-cancel-queue","order-cancel-exchange","order.cancel"),
    /**
     * 订单取消ttl队列
     */
    QUEUE_TTL_ORDER_CANCEL("order-cancel-ttl-queue","order-cancel-ttl-exchange","order.cancel.ttl")
    ;


    QueueEnum(String name, String exchange, String routeKey) {
        this.name = name;
        this.exchange = exchange;
        this.routeKey = routeKey;
    }

    private String name;
    /**
     * 交换机
     */
    private String exchange;
    /**
     * 路由键
     */
    private String routeKey;

    public String getName() {
        return name;
    }

    public String getExchange() {
        return exchange;
    }

    public String getRouteKey() {
        return routeKey;
    }
}
