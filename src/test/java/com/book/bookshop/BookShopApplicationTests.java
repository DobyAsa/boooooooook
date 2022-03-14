package com.book.bookshop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.book.bookshop.entity.Book;
import com.book.bookshop.entity.Order;
import com.book.bookshop.entity.OrderItem;
import com.book.bookshop.entity.User;
import com.book.bookshop.entity.enums.Category;
import com.book.bookshop.mapper.BookMapper;
import com.book.bookshop.mapper.CartMapper;
import com.book.bookshop.mapper.OrderItemMapper;
import com.book.bookshop.mapper.OrderMapper;
import com.book.bookshop.service.BookService;
import com.book.bookshop.service.OrderItemService;
import com.book.bookshop.service.OrderService;
import com.book.bookshop.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BookShopApplicationTests {


    //	@Value("${file.upload.path.relative}")
//	private String filePath;
    @Autowired
    private UserService userService;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;

    @Test
    void findBooks() {
        userService.list().forEach(System.out::println);
    }

    @Test
    public void findCartList() {
        cartMapper.findCartByUserId(5).forEach(System.out::println);
    }

    @Test
    public void findCartByIds() {
//		cartMapper.findCartByUserId(5).forEach(System.out::println);
        List<String> ids = new ArrayList();
        ids.add("10");
        ids.add("12");
        cartMapper.findCartByIds(ids).forEach(System.out::println);
    }

    @Test
    public void findOrderList() {
        // System.out.println(orderMapper.findOrderAndOrderDetailListByUser(5));
        ;
		/*		OrderQueryVo orderQueryVo = new OrderQueryVo();
		orderQueryVo.setUserId(1);
		orderQueryVo.setBegin(0);
		orderQueryVo.setEnd(2);
		orderQueryVo.setOrderBy(" bo.create_date desc");
		orderMapper.findOrderAndOrderDetailListByUser(orderQueryVo);*/
    }

    @Test
    public void orderNoTest() {
        List list = new ArrayList();
        list.add(1);
        list.add("1");
        list.add(1.1);
        System.out.println(list);
        System.out.println(list.getClass().getName());
    }

    @Test
    public void orderItemsDelete() {
        Integer[] orderIds = {14, 16};
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("order_id", orderIds);
        System.out.println(orderItemMapper.selectList(queryWrapper));

        //orderItemMapper.delete(queryWrapper);
    }

    @Test
    public void test() {
        String ids = "16,14";
        String[] strIds = ids.split(",");
        List<Integer> orderIds = new ArrayList();
        for (String id : strIds) {
            orderIds.add(Integer.parseInt(id));
        }
        System.out.println(orderIds);
    }

    @Test
    public void t1est() throws InterruptedException {
        Integer orderId = 31;
        System.out.println(orderId);
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
        System.out.println(booksName);
        System.out.println(price);
    }

    @Test
    public void tes1t() {
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper();
//        queryWrapper.and(wrapper -> wrapper.eq("asd","asd").ne("asd","asd"));
        queryWrapper.eq("id", 38).eq("order_id", 35);
        System.out.println(orderItemMapper.selectOne(queryWrapper));
    }

    @Test
    public void te2s1t() {
        String p = this.getClass().getResource("/static/images/").getPath();
        String path2 = this.getClass().getClassLoader().getResource("static/images/").getPath();
        System.out.println(p);
        System.out.println(path2);
    }

}