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
@TableName(value = "bs_appeal")
public class Appeal extends Model<Appeal> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String appealReason;
    private Date createDate;
    private Integer state;
    @TableField(exist = false)
    private String username;
}
