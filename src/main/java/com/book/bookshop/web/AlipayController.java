package com.book.bookshop.web;

import com.book.bookshop.utils.AlipayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author mumuwei
 * @date 0004
 */
@Controller
public class AlipayController {

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

    @GetMapping("/return")
    public String returnNotice(String out_trade_no, Model model){
        String query = alipayUtil.query(out_trade_no);
        model.addAttribute("query", query);
        return "query";
    }

    @PostMapping("/notify")
    public void notifyUrl(String trade_no, String total_amount, String trade_status){
        System.err.println("支付宝订单编号：" + trade_no + ", 订单金额： " + total_amount + ",订单状态：" + trade_status);
    }
}
