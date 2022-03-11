package com.book.bookshop.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.book.bookshop.entity.*;
import com.book.bookshop.service.AddressService;
import com.book.bookshop.service.CartService;
import com.book.bookshop.service.OrderItemService;
import com.book.bookshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.ls.LSOutput;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
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

    /**
     * 确认订单
     */
    @RequestMapping("/confirm")
    public String confirm(String ids, HttpSession session, Model model) {

        List<CartVo> cartVos = cartService.findCartByIds(ids);
        User user = (User) session.getAttribute("user");
        //获取当前用户的收货地址
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        List<Address> addressList = addressService.list(queryWrapper);

        //将购买的商品信息添加到session中
        session.setAttribute("cartVos", cartVos);
        model.addAttribute("addressList", addressList);
        model.addAttribute("list", cartVos);
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
        //先把orderitem的条数删除，避免外键异常
        orderItemService.orderItemsDelete(ids);
        if (orderService.removeByIds(Arrays.asList(ids.split(",")))) {
            return "success";
        } else
            return "fail";
    }

    //删除所有订单
    @RequestMapping("/deleteAll")
    @ResponseBody
    public String deleteAll() {
        //先把orderitem的条数删除，避免外键异常
        orderItemService.remove(new QueryWrapper<>());
        if (orderService.remove(new QueryWrapper<>())) {
            return "success";
        } else
            return "fail";
    }


    //显示用户订单列表
    @RequestMapping("/list")
    public String list() {
        return "order_list";
    }
    //获取订单记录
    @RequestMapping("/getOrderListData")
    public String getOrderListData(HttpSession session, OrderQueryVo orderQueryVo, Model model){
        User user = (User) session.getAttribute("user");
        List<Order> orders = orderService.findUserOrder(user.getId(),orderQueryVo);
        model.addAttribute("orders",orders);
        model.addAttribute("pre",orderQueryVo.getPage() -1);
        model.addAttribute("next",orderQueryVo.getPage() + 1);
        model.addAttribute("cur",orderQueryVo.getPage());
        model.addAttribute("pages",orderService.findUserOrderPages(user.getId(),orderQueryVo));
        model.addAttribute("pageSize",orderQueryVo.getPageSize());
        return "orderData";
    }



}
















