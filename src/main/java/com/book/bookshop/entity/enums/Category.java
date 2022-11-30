package com.book.bookshop.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @Author:yizhongwei
 * @Date:1/10 23:02
 * @Descripttion:图书类型枚举类
 */
@Getter
public enum Category {
    SELECTTED(1, "文学类"), RECOMMEND(2, "经管类"), BARGAGIN(3, "其他类");

    @EnumValue//标记数据库存的值是code
    private int code;
    private String desc;
    Category(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
