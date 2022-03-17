package com.book.bookshop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.bookshop.entity.Appeal;
import com.book.bookshop.entity.OrderItem;
import com.book.bookshop.mapper.AppealMapper;
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
public class AppealService extends ServiceImpl<AppealMapper, Appeal> {
}
