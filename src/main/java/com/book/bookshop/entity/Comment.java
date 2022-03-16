package com.book.bookshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "bs_comment")
public class Comment extends Model<Comment> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private Date createTime;
    private String content;
    @TableField(exist = false)
    private String username;
}
