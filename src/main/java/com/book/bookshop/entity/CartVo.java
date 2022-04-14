package com.book.bookshop.entity;

import lombok.Data;

/**
 * @Author:yizhongwei
 * @Date:2/7 22:46
 * 多表连接
 */
@Data
public class CartVo {
    private Integer id;
    private Integer userId;
    private Integer bookId;
    private Integer count;
    private String bookName;
    private String imgUrl;
    private double newPrice;

}
