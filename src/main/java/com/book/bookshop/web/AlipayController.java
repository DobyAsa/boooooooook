package com.book.bookshop.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.book.bookshop.entity.Order;
import com.book.bookshop.service.OrderService;
import com.book.bookshop.utils.AlipayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AlipayController {
    @Autowired
    private OrderService orderService;

    private AlipayUtil alipayUtil;

    @Autowired
    public void setAlipayUtil(AlipayUtil alipayUtil) {
        this.alipayUtil = alipayUtil;
    }

    @GetMapping("/index1")
    public String index(){
        return "index1";
    }

    @PostMapping("/create")
    public String create(String id, String price, String title, Model model){
        String pay = alipayUtil.pay(id, price, title);
        model.addAttribute("form", pay);
        return "pay";
    }

/*    @GetMapping("/return")
    public String returnNotice(String out_trade_no, Model model){
        String query = alipayUtil.query(out_trade_no);
        model.addAttribute("query", query);
        return "query";
    }*/

    @PostMapping("/notify")
    public void notifyUrl(String trade_no,String out_trade_no, String total_amount, String trade_status){

        System.out.println("订单编号："+out_trade_no+ ", 订单金额： " + total_amount + ",订单状态：" + trade_status);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_num",out_trade_no);
        Order order = orderService.getOne(queryWrapper);
        /*TRADE_SUCCESS 交易成功
         * TRADE_CLOSED 交易关闭
         * TRADE_FINISHED 交易完成
         * WAIT_BUYER_PAY 交易创建
         * */
        if (trade_status.equals("TRADE_SUCCESS")){
            order.setOrderStatus("2");
        }else {
            order.setOrderStatus("3");
        }
        orderService.updateById(order);
    }
}
