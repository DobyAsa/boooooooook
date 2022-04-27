package com.book.bookshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "bs_express")
public class Express extends Model<Express> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String expNum;//快递单号
    private String expCompany;
    private Integer addressId;
    private Integer orderId;
    private Date sendTime;
    private Date receiveTime;
    private String courier;
    private String courierPhone;
}
