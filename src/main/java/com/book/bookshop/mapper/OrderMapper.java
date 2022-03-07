package com.book.bookshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.book.bookshop.entity.Order;
import com.book.bookshop.entity.OrderQueryVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author:yizhongwei
 * @Date:2/14 0:23
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {
    //根据用户Id查询订单以及订单明细
    List<Order> findOrderAndOrderDetailListByUser(OrderQueryVo orderQueryVo);

    /**
     * 查询记录总数
     */
    Integer findOrderCountByUser(OrderQueryVo orderQueryVo);
}
