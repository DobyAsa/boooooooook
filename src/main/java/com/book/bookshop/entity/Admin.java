package com.book.bookshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "bs_admin")
public class Admin extends Model<Admin> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String bk;//预留字段
}
