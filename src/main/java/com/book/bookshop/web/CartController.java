package com.book.bookshop.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.book.bookshop.entity.Cart;
import com.book.bookshop.entity.CartVo;
import com.book.bookshop.entity.User;
import com.book.bookshop.entity.UserCartVo;
import com.book.bookshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author:yizhongwei
 * @Date:1/29 14:59
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    //加入购物车
    @ResponseBody
    @RequestMapping("/addCart")
    public String addCart(Cart cart, HttpSession session){
        User user = (User) session.getAttribute("user");

        cart.setUserId(user.getId());
        //查询该用户购物车中是否已存在书本
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        queryWrapper.eq("book_id",cart.getBookId());
        Cart oldCart = cartService.getOne(queryWrapper);

        if (oldCart == null){    //如果没有就添加
            cartService.save(cart);
        }else {                  //如果有就追加
            oldCart.setCount(oldCart.getCount() + cart.getCount());
            cartService.updateById(oldCart);
        }
        return "success";
    }

    //跳转至购物车
    @RequestMapping("/list")
    public String toCart(HttpSession session, Model model){
        User user =(User) session.getAttribute("user");
        List<CartVo> cartVos = cartService.findCartByUser(user.getId());

        //将用户的购物车信息存放到session中
        UserCartVo userCartVo = cartService.wrapperCart(cartVos);
        session.setAttribute("userCartInfo",userCartVo);

        model.addAttribute("cartList",cartVos);
        return "cart";
    }

    //更新购物车信息
    @ResponseBody
    @RequestMapping("/update")
    public String updateCart(HttpSession session,Cart cart){
        cartService.updateById(cart);
        User user =(User) session.getAttribute("user");
        List<CartVo> cartVos = cartService.findCartByUser(user.getId());

        //将用户的购物车信息存放到session中
        UserCartVo userCartVo = cartService.wrapperCart(cartVos);
        session.setAttribute("userCartInfo",userCartVo);

        double price = cartService.getCartItemTotal(cartVos);
        return String.valueOf(price);
    }

    //删除购物车记录
    @ResponseBody
    @RequestMapping("/delete")
    public String delete(String ids){
            return cartService.batchDelete(ids);
    }
}
