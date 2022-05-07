package com.book.bookshop.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.bookshop.entity.Book;
import com.book.bookshop.entity.Comment;
import com.book.bookshop.entity.User;
import com.book.bookshop.service.BookService;
import com.book.bookshop.service.CommentService;
import com.book.bookshop.service.UserService;
import com.book.bookshop.utils.RecBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;


/**
 * @Author:yizhongwei
 * @Date:1/11 16:37
 */
@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    //跳转到首页
    @RequestMapping("/index")
    public String toindex() {
        return "index";
    }

    //获取图书信息
    @RequestMapping("/getBookData")
    public String getBookData(Model model, Integer page, Integer category) {
        //mybatis-plus 分页
        Page pages = new Page<>(page, 4);
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category).eq("state",1);
        IPage<Book> iPage = bookService.page(pages, queryWrapper);
        model.addAttribute("bookList", iPage.getRecords());
        model.addAttribute("pre", iPage.getCurrent() - 1);
        model.addAttribute("next", iPage.getCurrent() + 1);
        model.addAttribute("category", category);
        model.addAttribute("cur",iPage.getCurrent());
        model.addAttribute("last",iPage.getPages());
        return "bookData";
    }

    //图书列表页
    @RequestMapping("/bookList")
    public String bookList(String category, Model model){
        model.addAttribute("category",category);
        return "books_list";
    }
    //获取图书列表数据
    @RequestMapping("/getBookListData")
    public String getBookListData(String category, Integer page,Integer pageSize, Model model){
        Page pages = new Page<Book>(page, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category);
        IPage<Book> iPage = bookService.page(pages, queryWrapper);
        List<Book> bookList = new ArrayList<>();
        for (Book book:iPage.getRecords()){
            if (book.getState()==1){
                bookList.add(book);
            }
        }
        model.addAttribute("bookList", bookList);
        model.addAttribute("pre", iPage.getCurrent() - 1);
        model.addAttribute("next", iPage.getCurrent() + 1);
        model.addAttribute("category", category);
        model.addAttribute("cur",iPage.getCurrent());
        model.addAttribute("pages",iPage.getPages());
        //pageSize回传至前端页面booksListData.html
        model.addAttribute("pageSize",pageSize);
        return "booksListData";
    }

    //商品详情页
    @RequestMapping("/detail")
    public String bookDetail(Integer id,Model model,Integer page){
        Book book = bookService.getById(id);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("book_id",id);
        List<Comment> comments = commentService.list(queryWrapper);
        for (Comment comment:comments){
            User user = userService.getById(comment.getUserId());
            comment.setUsername(user.getUsername());
        }

        RecBook.recBook(page,model,bookService);

        model.addAttribute("commentCount",comments.size());
        model.addAttribute("comments",comments);
        model.addAttribute("book",book);
        return "details";
    }

    //搜索图书
    @RequestMapping("/searchBook")
    public String search(String inputBookName, Model model){
        QueryWrapper<Book> queryWrapper1 = new QueryWrapper();
        QueryWrapper<Book> queryWrapper2 = new QueryWrapper();
        QueryWrapper<Book> queryWrapper3 = new QueryWrapper();
        List<Book> bookList = new ArrayList<>();
        queryWrapper1.like("name",inputBookName);//根据书名
        bookList.addAll(bookService.list(queryWrapper1));
        queryWrapper2.like("author",inputBookName);//根据作者
        bookList.addAll(bookService.list(queryWrapper2));
        queryWrapper3.like("info",inputBookName);//根据简介
        bookList.addAll(bookService.list(queryWrapper3));
        //去除已下架的，使用迭代器，不然会出现ConcurrentModificationException
        Iterator it = bookList.iterator();
        while(it.hasNext()) {
            Book book = (Book) it.next();
            if (book.getState() == 0) {
                it.remove();
            }
        }
        //去重
        Set<Book> bookSet = new HashSet();
        for (Book book :bookList){
            bookSet.add(book);
        }
        model.addAttribute("bookList", bookSet);
        return "searchPage";
    }
}
