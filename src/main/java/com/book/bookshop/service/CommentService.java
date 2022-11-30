package com.book.bookshop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.bookshop.entity.Comment;
import com.book.bookshop.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
 * @Auther: jzhang
 * @Date: 2019/9/29 16:37
 * @Description:
 */
@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> {
}
