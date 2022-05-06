package com.book.bookshop.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.book.bookshop.entity.*;
import com.book.bookshop.service.*;
import com.book.bookshop.utils.Email;
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
    //验证邮箱是否存在
    @ResponseBody
    @PostMapping("/checkEmail")
    public String checkEamil(String email) {
        return userService.checkEmail(email);
    }
    //用户注册
    @ResponseBody
    @PostMapping("/register")
    public String register(User user) {
        userService.save(user);
        return "success";
    }

    //用户登录
    @ResponseBody
    @PostMapping("/login")
    public String login(User user, HttpSession session, String code) {
        return userService.loginCheck(user, session, code);
    }
    //邮箱登录
    @ResponseBody
    @PostMapping("/emailLogin")
    public String emailLogin(String inputCode, String email,HttpSession session){
        return userService.emailLoginCheck(inputCode,email,session);
    }

    //发送邮件，获取验证码
    @PostMapping("/sendEmail")
    @ResponseBody
    public String sendEmail(String email, HttpSession session){
        String eCode =  Email.sendEmail(email);
        if (eCode==null){
            return "201";//发送失败
        }
        session.setAttribute("eCode",eCode);
        //System.out.println(verCode);
        return "200";//发送成功
    }
    //注销
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/book/index";
    }

    //跳转个人信息页面
    @RequestMapping("/userInfo")
    public String userInfo(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "userInfo";
    }

    @RequestMapping("/checkRegEmail")
    @ResponseBody
    public String checkRegEmail(String email){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("email", email);
        //如果查询到则返回success

        if (userService.list(queryWrapper).size()>0){
            return "success";
        }else return "fail";

    }
    //个人信息修改
    @PostMapping("/userInfoChange")
    @ResponseBody
    public String userInfoChange(User userInfo, HttpSession session) {
        User user = (User) session.getAttribute("user");
        userInfo.setId(user.getId());
        //把会话中旧的user去掉
        session.removeAttribute("user");
        //改成修改后的新的user
        session.setAttribute("user", userInfo);
        //这方式是根据主键id来修改的
        if (userService.saveOrUpdate(userInfo)) {
            return "success";
        } else
            return "fail";
    }

    //跳到密码修改页面
    @RequestMapping("/toChangePwd")
    public String toChangePwd() {
        return "pwdChange";
    }

    @RequestMapping("/toChangePwdbyEmail")
    public String toChangePwdbyEmail(String email,HttpSession session) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("email", email);
        User user = userService.getOne(queryWrapper);
        session.setAttribute("user",user);
        return "pwdChange";
    }


    //密码修改
    @RequestMapping("/pwdChange")
    @ResponseBody
    public String pwdChange(String newPwd, HttpSession session) {
        User user = (User) session.getAttribute("user");
        user.setPassword(newPwd);
        if (userService.saveOrUpdate(user)) {
            session.removeAttribute("user");
            return "success";
        } else
            return "fail";
    }

    //跳到订单评论页面
/*    @RequestMapping("/toComment")
    public String toComment(Integer orderId, Model model, HttpSession session) {
        Order order = orderService.getById(orderId);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id", orderId);
        List<OrderItem> items = orderItemService.list(queryWrapper);

        double price = 0.0;
        List<Book> books = new ArrayList<>();
        for (OrderItem item : items) {
            Integer bookId = orderItemService.getById(item.getId()).getBookId();
            Book book = bookService.getById(bookId);
            item.setBook(book);
            price += item.getCount() * book.getNewPrice();
            books.add(book);
        }
        order.setOrderItems(items);
        order.setTotalPrice(price);
        User user = (User) session.getAttribute("user");
        order.setUser(user);
        Address address = addressService.getById(order.getAddressId());
        order.setAddress(address);
        model.addAttribute("order", order);
        session.setAttribute("booksOfComment", books);
        session.setAttribute("orderOfComment", order);
        return "commentPage";
    }*/
    @RequestMapping("/toComment")
    public String toComment(Integer orderItemId, Model model, HttpSession session) {
        //根据itemid获取书本的id，
        OrderItem orderItem = orderItemService.getById(orderItemId);
        Book book = bookService.getById(orderItem.getBookId());
        // 再获取session的userid
        User user = (User) session.getAttribute("user");
        model.addAttribute("book",book);
        model.addAttribute("user",user);
        model.addAttribute("orderItem",orderItem);
        return "commentPage";
    }


/*    @RequestMapping("/comment")
    public String comment(HttpSession session, String content) {
        List<Book> booksOfComment = (List<Book>) session.getAttribute("booksOfComment");
        Order order = (Order) session.getAttribute("orderOfComment");
        User user = (User) session.getAttribute("user");
        List<Comment> comments = new ArrayList<>();
        for (Book book : booksOfComment) {
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

    }*/
    //点击评论
    @RequestMapping("/comment")
    public String comment(HttpSession session, String content,Integer orderItemId) {
        User user = (User) session.getAttribute("user");
        OrderItem orderItem = orderItemService.getById(orderItemId);
        Book book = bookService.getById(orderItem.getBookId());
        Comment comment = new Comment();
        comment.setUsername(user.getUsername());
        comment.setCreateTime(new Date());
        comment.setContent(content);
        comment.setBookId(book.getId());
        comment.setUserId(user.getId());
        commentService.save(comment);
        orderItem.setState(2);
        orderItemService.updateById(orderItem);
        return "order_list";

    }

    //跳转至申诉页面（用户名
    @RequestMapping(value = "/toAppeal", method = RequestMethod.GET)
    public String toAppeal(String username, Model model) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        User user = userService.getOne(queryWrapper);
        model.addAttribute("user", user);
        return "appealPage";
    }

    //跳转至申诉页面（邮箱）
    @RequestMapping(value = "/toAppealByEmail", method = RequestMethod.GET)
    public String toAppealByEmail(String email, Model model) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("email", email);
        User user = userService.getOne(queryWrapper);
        model.addAttribute("user", user);
        return "appealPage";
    }

    //提交申诉
    @RequestMapping(value = "/appeal", method = RequestMethod.POST)
    @ResponseBody
    public String appeal(Integer userId, String appealReason) {
        User user = userService.getById(userId);
        Appeal appeal = new Appeal();
        appeal.setForbidReason(user.getForbidReason());
        appeal.setUserId(userId);
        appeal.setAppealReason(appealReason);
        appeal.setCreateDate(new Date());
        appeal.setState(1);
        if (appealService.save(appeal)) {
            return "success";
        } else
            return "fail";
    }

    //检查是否已经提交申诉
    @RequestMapping(value = "/checkAppeal", method = RequestMethod.POST)
    @ResponseBody
    public String appeal(String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        User user = userService.getOne(queryWrapper);
        QueryWrapper<Appeal> queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("user_id", user.getId()).eq("state", 1);
        Appeal appeal = appealService.getOne(queryWrapper1);
        if (appeal != null) {//userId已存在且待审核
            return "success";
        } else
            return "fail";
    }

    @RequestMapping(value = "/checkAppealByEmail", method = RequestMethod.POST)
    @ResponseBody
    public String appealByEmail(String email) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("email", email);
        User user = userService.getOne(queryWrapper);
        QueryWrapper<Appeal> queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("user_id", user.getId()).eq("state", 1);
        Appeal appeal = appealService.getOne(queryWrapper1);
        if (appeal != null) {//userId已存在且待审核
            return "success";
        } else
            return "fail";
    }

    @RequestMapping(value = "/forgetPwd", method = RequestMethod.GET)
    public String forgetPwd(){
        return "forgetPwd";
        }

    }

