package com.book.bookshop.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.bookshop.entity.Book;
import com.book.bookshop.service.BookService;
import org.springframework.ui.Model;

/**
 * @Author:yizhongwei
 * @Date:5/8 0:28
 * 封装推荐书本模块
 */
public class RecBook {
    public static void recBook(Integer page, Model model, BookService bookService) {
        Page pages = new Page<>(page == null ? 1 : page, 3);
        QueryWrapper<Book> queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("recommend", 1).eq("state", 1);
        IPage<Book> iPage = bookService.page(pages, queryWrapper1);
        model.addAttribute("recBook", iPage.getRecords());
        model.addAttribute("pre", iPage.getCurrent() - 1);
        model.addAttribute("next", iPage.getCurrent() + 1);
        model.addAttribute("cur", iPage.getCurrent());
        model.addAttribute("pages", iPage.getPages());
    }
}
