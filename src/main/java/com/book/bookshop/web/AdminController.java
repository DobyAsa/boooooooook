package com.book.bookshop.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.bookshop.entity.*;
import com.book.bookshop.entity.enums.Category;
import com.book.bookshop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author:yizhongwei
 * @Date:3/8 15:08
 * 管理员控制层
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    //    Logger logger = LoggerFactory.getLogger(Object.class);
//    Logger l = Logger.getLogger(AdminController.class)
    private AdminService adminService;
    private BookService bookService;
    private OrderService orderService;
    private OrderItemService orderItemService;
    private UserService userService;
    private AppealService appealService;
    private AddressService addressService;
    private ExpressService expressService;

    public AdminController(AdminService adminService, BookService bookService,
                           OrderService orderService, OrderItemService orderItemService,
                           UserService userService, AppealService appealService,
                           AddressService addressService, ExpressService expressService) {
        this.adminService = adminService;
        this.bookService = bookService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.userService = userService;
        this.appealService = appealService;
        this.addressService = addressService;
        this.expressService = expressService;
    }

    //管理员登录
    @ResponseBody
    @PostMapping("/login")
    public String login(Admin admin, HttpSession session, String code) {
        return adminService.loginCheck(admin, session, code);
    }

    //去后台管理首页
    @RequestMapping("/toBookAdmin")
    public String toAllBooks(Model model, HttpSession session, String category) {
        model.addAttribute("category", category);
        return "admin/bookAdmin";
    }

    //【分页】【全部书籍】【后台】
    @RequestMapping("/getBookListByPage")
    public String getBookListByPage(HttpSession session, Model model, Integer page, Integer pageSize, String category) {
        Page pages = new Page<Book>(page, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();

        if (category == null || category.equals("")) {
            IPage<Book> iPage = bookService.page(pages, queryWrapper);
            List<Book> bookList = iPage.getRecords();
            for (Book book : bookList) {
                if (book.getCategory().toString().equals("SELECTTED")) book.setCate("文学类");
                if (book.getCategory().toString().equals("RECOMMEND")) book.setCate("经管类");
                if (book.getCategory().toString().equals("BARGAGIN")) book.setCate("其他类");
            }
            model.addAttribute("bookList", bookList);
            model.addAttribute("pre", page - 1);
            model.addAttribute("next", page + 1);
            model.addAttribute("cur", page);
            model.addAttribute("pages", iPage.getPages());
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("total", iPage.getTotal());
        } else {//图书类型不为空
            queryWrapper.eq("category", category);
            IPage<Book> iPage = bookService.page(pages, queryWrapper);
            List<Book> bookList = iPage.getRecords();
            for (Book book : bookList) {
                if (book.getCategory().toString().equals("SELECTTED")) book.setCate("文学类");
                if (book.getCategory().toString().equals("RECOMMEND")) book.setCate("经管类");
                if (book.getCategory().toString().equals("BARGAGIN")) book.setCate("其他类");
            }
            model.addAttribute("bookList", bookList);
            model.addAttribute("pre", page - 1);
            model.addAttribute("next", page + 1);
            model.addAttribute("cur", page);
            model.addAttribute("pages", iPage.getPages());
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("total", iPage.getTotal());
        }
        return "admin/allBooksData";
    }


    //删除图书
    @ResponseBody
    @RequestMapping("/deleteBook")
    public String deleteBook(Integer bookId) {
        boolean b = bookService.removeById(bookId);
        if (b)
            return "success";
        else return "fail";

    }

    @RequestMapping("/toAddBook")
    public String toAddBook() {
        return "admin/addBook";
    }


    //新书上架
    @RequestMapping("/addBook")
    public String toAddBook(Book book, MultipartFile bookPic, String pubDate) throws IOException, ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (book.getCate().equals("文学类")) book.setCategory(Category.SELECTTED);
        if (book.getCate().equals("经管类")) book.setCategory(Category.RECOMMEND);
        if (book.getCate().equals("其他类")) book.setCategory(Category.BARGAGIN);
        book.setPublishDate(simpleDateFormat.parse(pubDate));
//        book.setAuthorLoc("中国");
//        book.setSuit(Suit.YES);
        String bookPicName = bookPic.getOriginalFilename();
        book.setImgUrl(bookPicName);
        String filePath = "D:/images/";
        File dest = new File(filePath + bookPicName);
        bookPic.transferTo(dest);
        bookService.save(book);
        return "admin/bookAdmin";
    }

    //跳转至更新图书页面
    @RequestMapping("/toUpdateBook")
    public String toUpdateBook(Model model, @RequestParam("bookId") Integer id, HttpSession session) {

        Book book = bookService.getById(id);
        if (book.getCategory().toString().equals("SELECTTED")) book.setCate("文学类");
        if (book.getCategory().toString().equals("RECOMMEND")) book.setCate("经管类");
        if (book.getCategory().toString().equals("BARGAGIN")) book.setCate("其他类");
        book.setOldPrice(book.getNewPrice());
        session.setAttribute("oldPrice", book.getOldPrice());
        model.addAttribute("book", book);
        return "admin/updateBook";
    }

    //更新图书
    @RequestMapping("/updateBook")
    public String updateBook(Book book, MultipartFile bookPic, String pubDate, HttpSession session, String oldInfo) throws IOException, ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String bookPicName = bookPic.getOriginalFilename();
        book.setImgUrl(bookPicName);
        book.setPublishDate(simpleDateFormat.parse(pubDate));
        System.out.println(book.getCate());
        if (book.getCate().equals("文学类")) {
            book.setCategory(Category.SELECTTED);
        } else if (book.getCate().equals("经管类")) {
            book.setCategory(Category.RECOMMEND);
        } else if (book.getCate().equals("其他类")) {
            book.setCategory(Category.BARGAGIN);
        }
        if (book.getInfo() == null || book.getInfo().equals("")) {
            book.setInfo(oldInfo);
        }
        String filePath = "D:/images/";
        File dest = new File(filePath + bookPicName);
        book.setOldPrice((double) session.getAttribute("oldPrice"));
        bookPic.transferTo(dest);
        bookService.updateById(book);
        return "admin/bookAdmin";
    }

    //多选删除图书
    @RequestMapping("/deleteBooks")
    @ResponseBody
    public String deleteBooks(String ids) {
        boolean flag = bookService.removeByIds(Arrays.asList(ids.split(",")));
        if (flag)
            return "success";
        else return "fail";

    }

    //去到全部用户管理页面
    @RequestMapping("/toAllUsers")
    public String toAllUsers(Model model) {
        List<User> userList = userService.list();
        model.addAttribute("userList", userList);
        return "admin/allUsers";
    }

    //封号
    @RequestMapping("/forbidUser")
    @ResponseBody
    public String forbidUser(Integer userId, String forbidReason, HttpSession session) {
        User user = userService.getById(userId);
        user.setState(2);
        user.setForbidReason(forbidReason);
        User onlineUser = (User) session.getAttribute("user");
        if (userService.saveOrUpdate(user)) {
            //封号的同时如果申诉列表中有该用户的待审核记录则将其设为不通过
           /* QueryWrapper<Appeal> queryWrapper = new QueryWrapper();
            queryWrapper.eq("user_id",userId).eq("state",1);
            Appeal appeal =appealService.getOne(queryWrapper);
            if (appeal!=null){
                appeal.setState(3);
                appealService.updateById(appeal);
            }*/
            if (onlineUser != null) {
                if (onlineUser.getUsername().equals(user.getUsername())) {
                    //如果封的号刚好在线，则强制其下线
                    session.removeAttribute("user");
                }
            }

            return "success";
        } else {
            return "fail";
        }
    }

    //解封
    @RequestMapping("/unforbidUser")
    @ResponseBody
    public String unforbidUser(Integer userId) {
        User user = userService.getById(userId);
        user.setState(1);
        if (userService.saveOrUpdate(user)) {
            //解封的同时如果申诉列表中有该用户的待审核记录则将其设为通过
            QueryWrapper<Appeal> queryWrapper = new QueryWrapper();
            queryWrapper.eq("user_id", userId).eq("state", 1);
            Appeal appeal = appealService.getOne(queryWrapper);
            if (appeal != null) {
                appeal.setState(2);
                appealService.updateById(appeal);
            }
            return "success";
        } else {
            return "fail";
        }
    }

    //批量封号
    @RequestMapping("/deleteUsers")
    @ResponseBody
    public String deleteUsers(String ids) {
        List<User> users = (List<User>) userService.listByIds(Arrays.asList(ids.split(",")));
        for (User user : users) {
            user.setState(2);
        }
        if (userService.saveOrUpdateBatch(users)) {
            return "success";
        } else {
            return "fail";
        }
    }

    //去到全部申诉记录页面
    @RequestMapping("/toAppealList")
    public String toAppealList(Model model) {
        List<Appeal> appealList = appealService.list();
        for (Appeal appeal : appealList) {
            User user = userService.getById(appeal.getUserId());
            appeal.setUsername(user.getUsername());
        }
        model.addAttribute("appealList", appealList);
        return "admin/appealList";
    }

    //申诉批准通过
    @RequestMapping("/approve")
    @ResponseBody
    public String approve(Integer userId, Integer appealId) {
        User user = userService.getById(userId);
        user.setState(1);
        Appeal appeal = appealService.getById(appealId);
        appeal.setState(2);
        if (userService.updateById(user) && appealService.updateById(appeal)) {
            return "success";
        }
        return "fail";

    }

    //申诉批准不通过
    @RequestMapping("/disapprove")
    @ResponseBody
    public String disapprove(Integer userId, Integer appealId) {
        User user = userService.getById(userId);
        user.setState(2);
        Appeal appeal = appealService.getById(appealId);
        appeal.setState(3);
        if (userService.updateById(user) && appealService.updateById(appeal)) {
            return "success";
        }
        return "fail";
    }

    //删除申诉记录
    @RequestMapping("/deleteAppeal")
    public String disapprove(Integer appealId, Model model) {
        appealService.removeById(appealId);
        return this.toAppealList(model);
    }

    //根据书名搜索
    @RequestMapping("/searchBook")
    public String searchBook(String inputBookName, Model model) {
        QueryWrapper<Book> queryWrapper1 = new QueryWrapper();
        QueryWrapper<Book> queryWrapper2 = new QueryWrapper();
        QueryWrapper<Book> queryWrapper3 = new QueryWrapper();
        List<Book> bookList = new ArrayList<>();
        queryWrapper1.like("name", inputBookName);
        bookList.addAll(bookService.list(queryWrapper1));
        queryWrapper2.like("author", inputBookName);
        bookList.addAll(bookService.list(queryWrapper2));
        queryWrapper3.like("info", inputBookName);
        bookList.addAll(bookService.list(queryWrapper3));
        for (Book book : bookList) {
            if (book.getCategory().toString().equals("SELECTTED")) book.setCate("文学类");
            if (book.getCategory().toString().equals("RECOMMEND")) book.setCate("经管类");
            if (book.getCategory().toString().equals("BARGAGIN")) book.setCate("其他类");
        }
        //去重
        Set<Book> bookSet = new HashSet();
        for (Book book : bookList) {
            bookSet.add(book);
        }
        model.addAttribute("bookList", bookSet);
        return "admin/searchBook";

    }

    //根据用户名搜索
    @RequestMapping("/searchUser")
    public String searchUser(String inputUsername, Model model) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("username", inputUsername);
        List<User> userList = userService.list(queryWrapper);
        model.addAttribute("userList", userList);
        return "admin/searchUsers";

    }

    //注销
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("admin");
        return "redirect:/book/index";
    }

    //商品下架
    @RequestMapping("/offShelf")
    public String offShelf(Integer bookId) {
        Book book = bookService.getById(bookId);
        book.setState(0);
        bookService.updateById(book);
        return "admin/bookAdmin";
    }

    //商品上架
    @RequestMapping("/onShelf")
    public String onShelf(Integer bookId) {
        Book book = bookService.getById(bookId);
        book.setState(1);
        bookService.updateById(book);
        return "admin/bookAdmin";
    }

    //补充库存
    @RequestMapping("/addCount")
    public String addCount(Integer bookId, Integer count) {
        Book book = bookService.getById(bookId);
        book.setCount(book.getCount() + count);
        bookService.updateById(book);
        return "admin/bookAdmin";
    }

    //修改价格
    @RequestMapping("/changePrice")
    public String changePrice(Integer bookId, double price) {
        Book book = bookService.getById(bookId);
        book.setOldPrice(book.getNewPrice());
        book.setNewPrice(price);
        bookService.updateById(book);
        return "admin/bookAdmin";
    }

    //去到所有订单
    @RequestMapping("/toAllOrder")
    public String toAllOrder(Model model) {
        List<User> userList = userService.list();
        model.addAttribute("userList", userList);
        return "admin/allOrder";
    }

    //获取订单记录
    /*@RequestMapping("/getOrderListData")
    public String getOrderListData(Model model, Integer page, Integer pageSize) {
*//*        Page pages = new Page<Order>(page, pageSize);
        IPage<Order> iPage = orderService.page(pages, new QueryWrapper<>());
        List<Order> orders = iPage.getRecords();*//*
        List<Order> orders = orderService.list();
        for (Order order:orders){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("order_id",order.getId());
            List<com.book.bookshop.entity.OrderItem> orderItems = orderItemService.list(queryWrapper);
            order.setOrderItems(orderItems);
            order.setUser(userService.getById(order.getUserId()));
            order.setAddress(addressService.getById(order.getAddressId()));
            double price = 0.0;
            for (com.book.bookshop.entity.OrderItem item:orderItems){
                Book book = bookService.getById(item.getBookId());
                item.setBook(book);
                price += item.getCount() * item.getBook().getNewPrice();
            }
            order.setTotalPrice(price);
            Express express = expressService.getOne(queryWrapper);
            if (express!=null) order.setExpress(express);
        }
        model.addAttribute("orders", orders);

*//*        model.addAttribute("pre", page - 1);
        model.addAttribute("next", page + 1);
        model.addAttribute("cur", page);
        model.addAttribute("pages", iPage.getPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("total",iPage.getTotal());*//*
        return "admin/orderData";
    }*/

    //获取订单记录
    @RequestMapping("/getOrderListData")
    public String getOrderListData(HttpSession session, OrderQueryVo orderQueryVo, Model model) {
        Integer userId = orderQueryVo.getUserId() == null ? null : orderQueryVo.getUserId();
        List<Order> orders = orderService.findUserOrder(userId, orderQueryVo);
        model.addAttribute("orders", orders);
        model.addAttribute("pre", orderQueryVo.getPage() - 1);
        model.addAttribute("next", orderQueryVo.getPage() + 1);
        model.addAttribute("cur", orderQueryVo.getPage());
        model.addAttribute("pages", orderService.findUserOrderPages(userId, orderQueryVo));
        model.addAttribute("pageSize", orderQueryVo.getPageSize());
        session.setAttribute("userOrderPages", orderService.findUserOrderPages(userId, orderQueryVo));
        return "admin/orderData";
    }

    //取消订单
    @RequestMapping("/cancelOrder")
    @ResponseBody
    public String cancelOrder(Integer orderId) {
        Order order = orderService.getById(orderId);
        if (order != null) {
            //返书本给库存
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("order_id", orderId);
            List<com.book.bookshop.entity.OrderItem> items = orderItemService.list(queryWrapper);
            for (OrderItem item : items) {
                Book book = bookService.getById(item.getBookId());
                book.setCount(book.getCount() + item.getCount());
                bookService.updateById(book);
                //更新订单item状态
                item.setState(3);
                orderItemService.updateById(item);
            }
            //更新订单状态
            order.setOrderStatus("4");
            if (orderService.updateById(order)) {
                return "success";
            }
            return "cancelFail";
        } else return "emptyOrder";
    }

    //发货
    @RequestMapping("/toSendOut")
    public String toSendOut(Integer orderId, Model model) {
        Order order = orderService.getById(orderId);
        model.addAttribute("order", order);
        return "admin/addExpress";
    }

    //发货  生成物流信息
    @RequestMapping("/addExpress")
    public String addExpress(Express express, String orderNum) {
        express.setSendTime(new Date());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_num", orderNum);
        Order order = orderService.getOne(queryWrapper);
        order.setOrderStatus("5");
        orderService.updateById(order);
        express.setOrderId(order.getId());
        expressService.save(express);
        return "admin/allOrder";
    }

    //删除订单
    @RequestMapping("/orderDelete")
    @ResponseBody
    public String orderDelete(String ids) {
        List<Order> orders = (List<Order>) orderService.listByIds(Arrays.asList(ids.split(",")));
        for (Order order : orders) {
            //如果订单是1待支付、2已支付/待发货、5已发货/待收货 状态则不允许删除
            if (order.getOrderStatus().equals("1") ||
                    order.getOrderStatus().equals("2") ||
                    order.getOrderStatus().equals("5")) {
                return "notDelete";
            }
        }
        //先把orderitem的条数删除，避免外键异常
        orderItemService.orderItemsDelete(ids);
        boolean flag = orderService.removeByIds(Arrays.asList(ids.split(",")));
        if (flag) {
            return "success";
        } else
            return "fail";
    }

    //删除所有订单
    @RequestMapping("/deleteAll")
    @ResponseBody
    public String deleteAll() {
        List<Order> orders = orderService.list();
        for (Order order : orders) {
            //如果订单是1待支付、2已支付/待发货、5已发货/待收货 状态则不允许删除
            if (order.getOrderStatus().equals("1") ||
                    order.getOrderStatus().equals("2") ||
                    order.getOrderStatus().equals("5")) {
                return "notDelete";
            }
        }
        //先把orderitem的条数删除，避免外键异常
        orderItemService.remove(new QueryWrapper<>());
        if (orderService.remove(new QueryWrapper<>())) {
            return "success";
        } else
            return "fail";
    }

    //设置推荐书本
    @RequestMapping("/setRecBook")
    public String setRecBook(Integer bookId) {
        Book book = bookService.getById(bookId);
        book.setRecommend(1);
        bookService.updateById(book);
        return "admin/bookAdmin";
    }

    //设置为非推荐书本
    @RequestMapping("/setNotRecBook")
    public String setNotRecBook(Integer bookId) {
        Book book = bookService.getById(bookId);
        book.setRecommend(0);
        bookService.updateById(book);
        return "admin/bookAdmin";
    }

}
