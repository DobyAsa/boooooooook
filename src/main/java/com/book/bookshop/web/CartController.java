package com.book.bookshop.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.book.bookshop.entity.*;
import com.book.bookshop.mapper.BookMapper;
import com.book.bookshop.mapper.CartMapper;
import com.book.bookshop.service.BookService;
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
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookMapper bookMapper;

    //加入购物车
    @ResponseBody
    @RequestMapping("/addCart")
    public String addCart(Cart cart, HttpSession session) {
        User user = (User) session.getAttribute("user");

        cart.setUserId(user.getId());
        //查询该用户购物车中是否已存在书本
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        queryWrapper.eq("book_id", cart.getBookId());
        Cart oldCart = cartService.getOne(queryWrapper);
        Book book = bookService.getById(cart.getBookId());
        if (oldCart == null) {    //如果没有就添加，记得库存相应减少
            cartService.save(cart);
            book.setCount(book.getCount() - cart.getCount());
            bookService.updateById(book);
        } else {                  //如果有就追加
            oldCart.setCount(oldCart.getCount() + cart.getCount());
            cartService.updateById(oldCart);
            book.setCount(book.getCount() - cart.getCount());
            bookService.updateById(book);
        }
        return "success";
    }

    //跳转至购物车
    @RequestMapping("/list")
    public String toCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<CartVo> cartVos = cartService.findCartByUser(user.getId());

        //将用户的购物车信息存放到session中
        UserCartVo userCartVo = cartService.wrapperCart(cartVos);
        session.setAttribute("userCartInfo", userCartVo);

        model.addAttribute("cartList", cartVos);
        return "cart";
    }

    //更新购物车信息
    @ResponseBody
    @RequestMapping("/update")
    public String updateCart(HttpSession session, Cart cart) {
        cartService.updateById(cart);
        User user = (User) session.getAttribute("user");
        List<CartVo> cartVos = cartService.findCartByUser(user.getId());

        //将用户的购物车信息存放到session中
        UserCartVo userCartVo = cartService.wrapperCart(cartVos);
        session.setAttribute("userCartInfo", userCartVo);

        double price = cartService.getCartItemTotal(cartVos);
        return String.valueOf(price);
    }

    //删除购物车记录
    @ResponseBody
    @RequestMapping("/delete")
    public String delete(String ids) {
        return cartService.batchDelete(ids);
    }

    //减少图书时库存增加
    @ResponseBody
    @RequestMapping("/plusBook")
    public String plusBook(Integer cartId, Integer count) {
        Cart cart = cartService.getById(cartId);
        Book book = bookMapper.selectById(cart.getBookId());
        book.setCount(book.getCount() + 1);
        bookMapper.updateById(book);
        return "success";
    }

    //增加图书时库存减少
    @ResponseBody
    @RequestMapping("/minusBook")
    public String minusBook(Integer cartId, Integer count) {
        Cart cart = cartService.getById(cartId);
        Book book = bookMapper.selectById(cart.getBookId());
        book.setCount(book.getCount() - 1);
        if (book.getCount() < 0){
            return "fail";//库存不足
        }
        bookMapper.updateById(book);
        return "success";
    }


    //手动修改商品数量，库存相应更新
    @ResponseBody
    @RequestMapping("/onBlurCount")
    public String onBlurCount(Integer cartId, Integer count) {
        Cart cart = cartService.getById(cartId);
        Book book = bookMapper.selectById(cart.getBookId());
        //购物车添加，库存减少
        if (count>cart.getCount()){
            if (book.getCount()-(count-cart.getCount())<0){
                return book.getCount().toString();
            }
            book.setCount(book.getCount()-(count-cart.getCount()));
        }
        //购物车减少，库存增加
        if (count<cart.getCount()){
            book.setCount(book.getCount()+(cart.getCount()-count));
        }
        bookMapper.updateById(book);
        return "success";
    }
}
