package com.book.bookshop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.bookshop.entity.Address;
import com.book.bookshop.entity.Express;
import com.book.bookshop.mapper.AddressMapper;
import com.book.bookshop.mapper.ExpressMapper;
import org.springframework.stereotype.Service;


@Service
public class ExpressService extends ServiceImpl<ExpressMapper, Express> {
}
