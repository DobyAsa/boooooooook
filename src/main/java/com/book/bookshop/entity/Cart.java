package com.book.bookshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Auther: jzhang
 * @Date: 2019/9/26 11:26
 * @Description:
 */
@Data
@TableName(value = "bs_cart")
@EqualsAndHashCode(callSuper = false)
public class Cart extends Model<Cart> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer bookId;
    private Integer count;

    //屏蔽book属性和数据库表中字段映射
    @TableField(exist = false)
    private Book book;
}
