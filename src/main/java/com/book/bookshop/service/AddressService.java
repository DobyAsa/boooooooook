package com.book.bookshop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.bookshop.entity.Address;
import com.book.bookshop.mapper.AddressMapper;
import org.springframework.stereotype.Service;


@Service
public class AddressService extends ServiceImpl<AddressMapper, Address> {
}
