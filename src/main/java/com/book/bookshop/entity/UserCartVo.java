package com.book.bookshop.entity;

import lombok.Data;

/**
 * @Author:yizhongwei
 * @Date:2/8 18:49
 * 用户购物车信息展示对象
 */
@Data
public class UserCartVo {
    private Integer num;
    private double totalPrice;
}
