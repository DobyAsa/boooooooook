package com.book.bookshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.book.bookshop.entity.enums.Category;
import lombok.Data;

import java.util.Date;


@Data
@TableName("bs_book")
public class Book extends Model<Book> {
    //因为主键是自增的，如果不是需要输入则是IdType.Input
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String isbn;
    private String name;
    private String author;
    private String publisher;
    private Date publishDate;
    private double oldPrice;
    private double newPrice;
//    private String authorLoc;
//    private Suit suit;
    private Category category;
    private String info;
    private String imgUrl;
    @TableField(exist = false)
    private String cate;
    private Integer state;
    private Integer count;
    private Integer recommend;
}
