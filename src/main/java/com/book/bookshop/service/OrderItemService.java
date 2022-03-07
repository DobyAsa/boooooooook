package com.book.bookshop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.bookshop.entity.OrderItem;
import com.book.bookshop.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: jzhang
 * @Date: 2019/9/29 16:37
 * @Description:
 */
@Service
public class OrderItemService extends ServiceImpl<OrderItemMapper, OrderItem> {
    @Autowired
    private OrderItemMapper orderItemMapper;

    //删除orderItem
    public boolean orderItemsDelete(String ids) {
        String[] strIds = ids.split(",");
        List<Integer> orderIds = new ArrayList();
        for (String id : strIds) {
            orderIds.add(Integer.parseInt(id));
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("order_id", orderIds);

        if (orderItemMapper.delete(queryWrapper) > 0) {
            return true;
        } else
            return false;
    }
}
