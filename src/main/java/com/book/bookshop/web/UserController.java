package com.book.bookshop.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.book.bookshop.entity.*;
import com.book.bookshop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author:yizhongwei
 * @Date:1/17 20:29
 * 用户控制器
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private BookService bookService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private AppealService appealService;
    //验证用户是否存zai
    @ResponseBody
    @PostMapping("/checkUserName")
    public String checkUserName(String username) {
        return userService.checkUser(username);
    }

    //用户注册
    @ResponseBody
    @PostMapping("/register")
    public String register(User user){
        userService.save(user);
        return "success";
    }

    //用户登录
    @ResponseBody
    @PostMapping("/login")
    public String login(User user , HttpSession session,String code){
        return userService.loginCheck(user,session,code);

    }

    //注销
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/book/index";
    }

    //跳转个人信息页面
    @RequestMapping("/userInfo")
    public String userInfo(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        return "userInfo";
    }

    //个人信息修改
    @PostMapping("/userInfoChange")
    @ResponseBody
    public String userInfoChange(User userInfo,HttpSession session){
        System.out.println(userInfo);
        User user = (User) session.getAttribute("user");
        userInfo.setId(user.getId());
        //把会话中旧的user去掉
        session.removeAttribute("user");
        //改成修改后的新的user
        session.setAttribute("user",userInfo);
        //这方式是根据主键id来修改的
        if (userService.saveOrUpdate(userInfo)){
            return "success";
        }else
            return "fail";
    }

    //跳到密码修改页面
    @RequestMapping("/toChangePwd")
    public String toChangePwd(){
        return "pwdChange";
    }
    //密码修改
    @RequestMapping("/pwdChange")
    @ResponseBody
    public String pwdChange(String newPwd,HttpSession session){
        User user = (User) session.getAttribute("user");
        user.setPassword(newPwd);
        if (userService.saveOrUpdate(user)){
            session.removeAttribute("user");
            return "success";
        }else
            return "fail";
    }

    //跳到订单评论页面
    @RequestMapping("/toComment")
    public String toComment(Integer orderId, Model model,HttpSession session){
        Order order = orderService.getById(orderId);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id",orderId);
        List<OrderItem> items = orderItemService.list(queryWrapper);

        double price = 0.0;
        List<Book> books = new ArrayList<>();
        for (OrderItem item:items) {
            Integer bookId = orderItemService.getById(item.getId()).getBookId();
            Book book = bookService.getById(bookId);
            item.setBook(book);
            price += item.getCount() * book.getNewPrice();
            books.add(book);
        }
        order.setOrderItems(items);
        order.setTotalPrice(price);
        User user = (User)session.getAttribute("user");
        order.setUser(user);
        Address address = addressService.getById(order.getAddressId());
        order.setAddress(address);
        model.addAttribute("order",order);
        session.setAttribute("booksOfComment",books);
        session.setAttribute("orderOfComment",order);
        return "commentPage";
    }

    //点击评论
    @RequestMapping("/comment")
    public String comment(HttpSession session ,String content){
        List<Book> booksOfComment = (List<Book>)session.getAttribute("booksOfComment");
        Order order = (Order)session.getAttribute("orderOfComment");
        User user = (User)session.getAttribute("user");
        List<Comment> comments = new ArrayList<>();
        for (Book book:booksOfComment){
           Comment comment = new Comment();
            comment.setBookId(book.getId());
            comment.setContent(content);
            comment.setCreateTime(new Date());
            comment.setUserId(user.getId());
            comments.add(comment);
        }
        commentService.saveBatch(comments);
        order.setOrderStatus("3");
        orderService.updateById(order);
        return "order_list";

    }

    @RequestMapping( value = "/toAppeal" ,method=RequestMethod.GET)
    public String toAppeal(String username,Model model){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username);
        User user = userService.getOne(queryWrapper);
        model.addAttribute("user",user);
        return "appealPage";
    }

    @RequestMapping( value = "/appeal" ,method=RequestMethod.POST)
    @ResponseBody
    public String appeal(Integer userId,String appealReason){
        Appeal appeal = new Appeal();
        appeal.setUserId(userId);
        appeal.setAppealReason(appealReason);
        appeal.setCreateDate(new Date());
        appeal.setState(1);
        if (appealService.save(appeal)){
            return "success";
        } else
        return "fail";
    }
}
