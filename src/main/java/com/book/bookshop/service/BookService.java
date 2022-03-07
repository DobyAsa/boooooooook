package com.book.bookshop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.bookshop.entity.Book;
import com.book.bookshop.mapper.BookMapper;
import org.springframework.stereotype.Service;

/**
 * @Author:yizhongwei
 * @Date:1/10 23:22
 */
@Service
public class BookService extends ServiceImpl<BookMapper, Book> {
}
