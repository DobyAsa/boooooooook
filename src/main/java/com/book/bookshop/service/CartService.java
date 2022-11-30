package com.book.bookshop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.bookshop.entity.Book;
import com.book.bookshop.entity.Cart;
import com.book.bookshop.entity.CartVo;
import com.book.bookshop.entity.UserCartVo;
import com.book.bookshop.mapper.BookMapper;
import com.book.bookshop.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Author:yizhongwei
 * @Date:1/29 14:57
 */
@Service
public class CartService extends ServiceImpl<CartMapper, Cart> {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private BookMapper bookMapper;

    //根据用户查询购物车记录
    public List<CartVo> findCartByUser(Integer userId) {
        return cartMapper.findCartByUserId(userId);
    }

    //根据购物车id查询记录
    public List<CartVo> findCartByIds(String ids) {
        return cartMapper.findCartByIds(Arrays.asList(ids.split(",")));
    }


    //统计当前用户购物车的商品总价
    public double getCartItemTotal(List<CartVo> list) {
        double sum = 0.0;
        for (CartVo cart : list) {
            sum += cart.getCount() * cart.getNewPrice();
        }
        return sum;
    }


    //删除购物车，传进来的是购物的ids
    public String batchDelete(String ids) {
        if (ids != null) {
            String[] idArray = ids.split(",");

            List<String> cartIds = Arrays.asList(idArray);
            List<Cart> carts = cartMapper.selectBatchIds(cartIds);
            //删除同时将数量返还给库存
            for (Cart cart : carts) {
                Book book = bookMapper.selectById(cart.getBookId());
                book.setCount(book.getCount() + cart.getCount());
                bookMapper.updateById(book);
            }
            int i = cartMapper.deleteBatchIds(cartIds);
            if (i > 0) {
                return "success";
            }
        }
        return "fail";
    }

    /**
     * 包装用户购物车信息数据
     */
    public UserCartVo wrapperCart(List<CartVo> list) {
        UserCartVo userCartVo = new UserCartVo();
        userCartVo.setNum(list.size());
        userCartVo.setTotalPrice(getCartItemTotal(list));
        return userCartVo;
    }
}
