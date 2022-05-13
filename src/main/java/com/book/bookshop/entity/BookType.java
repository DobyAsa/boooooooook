package com.book.bookshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author:yizhongwei
 * @Date:5/8 19:33
 */
@Data
@TableName("bs_book_type")
public class BookType {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String typeName;
}
