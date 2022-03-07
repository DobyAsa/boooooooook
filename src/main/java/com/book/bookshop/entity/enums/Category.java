package com.book.bookshop.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @Author:yizhongwei
 * @Date:1/10 23:02
 * @Descripttion:图书类型枚举类
 */
@Getter
public enum  Category {
    SELECTTED(1,"精选图书"), RECOMMEND(2,"推荐图书"), BARGAGIN(3,"特价图书");
    Category(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    @EnumValue//标记数据库存的值是code
    private final int code;
    private final String desc;
}
