package com.book.bookshop.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.book.bookshop.entity.*;
import com.book.bookshop.service.*;
import com.book.bookshop.utils.AlipayUtil;
import com.book.bookshop.utils.RecBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author:yizhongwei
 * @Date:2/12 18:01
 * 订单控制器
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private CartService cartService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private BookService bookService;
    @Autowired
    private ExpressService expressService;
    private AlipayUtil alipayUtil;

    @Autowired
    public void setAlipayUtil(AlipayUtil alipayUtil) {
        this.alipayUtil = alipayUtil;
    }

    /**
     * 确认订单
     */
    @RequestMapping("/confirm")
    public String confirm(Integer page, String ids, HttpSession session, Model model) {

        List<CartVo> cartVos = cartService.findCartByIds(ids);
        User user = (User) session.getAttribute("user");
        //获取当前用户的收货地址
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        List<Address> addressList = addressService.list(queryWrapper);

        //将购买的商品信息添加到session中
        session.setAttribute("cartVos", cartVos);

        RecBook.recBook(page, model, bookService);

        model.addAttribute("addressList", addressList);
        model.addAttribute("list", cartVos);
        model.addAttribute("ids", ids);
        return "confirm_order";
    }

    //提交订单
    @RequestMapping("/commitOrder")
    public String commitOrder(Integer addrId, HttpSession session) {
        List<CartVo> cartVos = (List<CartVo>) session.getAttribute("cartVos");
        String flag = orderService.buy(cartVos, addrId, session);
        if (flag.equals("success")) {
            //跳转至订单列表页
            return "redirect:/order/list";
        } else {
            //跳到首页
            return "redirect:/book/index";
        }
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
            System.out.println(flag);
            return "success";
        } else
            return "fail";
    }

    //删除所有订单
    @RequestMapping("/deleteAll")
    @ResponseBody
    public String deleteAll(HttpSession session) {
        QueryWrapper queryWrapper = new QueryWrapper();
        User user = (User) session.getAttribute("user");
        queryWrapper.eq("user_id", user.getId());
        List<Order> orders = orderService.list(queryWrapper);
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


    //显示订单列表
    @RequestMapping("/list")
    public String list() {
        return "order_list";
    }

    //获取订单记录
    @RequestMapping("/getOrderListData")
    public String getOrderListData(HttpSession session, OrderQueryVo orderQueryVo, Model model) {
        User user = (User) session.getAttribute("user");
        List<Order> orders = orderService.findUserOrder(user.getId(), orderQueryVo);
        model.addAttribute("orders", orders);
        model.addAttribute("pre", orderQueryVo.getPage() - 1);
        model.addAttribute("next", orderQueryVo.getPage() + 1);
        model.addAttribute("cur", orderQueryVo.getPage());
        model.addAttribute("pages", orderService.findUserOrderPages(user.getId(), orderQueryVo));
        model.addAttribute("pageSize", orderQueryVo.getPageSize());
        session.setAttribute("userOrderPages", orderService.findUserOrderPages(user.getId(), orderQueryVo));
        return "orderData";
    }


    @RequestMapping("/orderPay")
    public String orderPay(Integer orderId, Model model) {
        /*买家账号 ftwbqx4717@sandbox.com
         登录密码111111*/
        Order order = orderService.getById(orderId);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id", orderId);
        List<OrderItem> items = orderItemService.list(queryWrapper);
        double price = 0.0;
        String booksName = "";
        for (OrderItem item : items) {
            Integer bookId = orderItemService.getById(item.getId()).getBookId();
            Book book = bookService.getById(bookId);
            price += item.getCount() * book.getNewPrice();
            booksName += book.getName() + "、";
        }
        String form = alipayUtil.pay(order.getOrderNum(), String.valueOf(price), booksName);
        model.addAttribute("form", form);
        return "pay";
    }

/*    @GetMapping("/return")
    public String returnNotice(String out_trade_no, Model model){
        String query = alipayUtil.query(out_trade_no);
        System.out.println(query);
        model.addAttribute("query", query);
        return "order_list";
    }*/

    @PostMapping("/notify")
    public void notifyUrl(String trade_no, String total_amount, String trade_status) {
        System.out.println("支付宝订单编号：" + trade_no + ", 订单金额： " + total_amount + ",订单状态：" + trade_status);
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
            List<OrderItem> items = orderItemService.list(queryWrapper);
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

    //收货
    @RequestMapping("/takeOver")
    @ResponseBody
    public String takeOver(Integer orderId) {
        //根据orderId在在express表中查出对应的express,然后设置收货时间
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id", orderId);
        Express express = expressService.getOne(queryWrapper);
        express.setReceiveTime(new Date());
        expressService.updateById(express);
        Order order = orderService.getById(orderId);
        order.setOrderStatus("6");
        if (orderService.updateById(order)) {
            return "success";
        } else return "fail";
    }

}















