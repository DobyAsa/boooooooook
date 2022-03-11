package com.book.bookshop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.book.bookshop.entity.Book;
import com.book.bookshop.entity.User;
import com.book.bookshop.entity.enums.Category;
import com.book.bookshop.mapper.BookMapper;
import com.book.bookshop.mapper.CartMapper;
import com.book.bookshop.mapper.OrderItemMapper;
import com.book.bookshop.mapper.OrderMapper;
import com.book.bookshop.service.BookService;
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
        for(int i = 0;;i++){
            Thread.sleep(i*1000);
            System.out.println(i+"秒已过");

        }
    }

    @Test
    public void tes1t(HttpServletRequest request) {
    String path = request.getServletContext().getRealPath("/");
    String uploadPath = path + "/statics/images/";
        System.out.println(uploadPath);
    }
    @Test
    public void te2s1t() {
        String p = this.getClass().getResource("/static/images/").getPath();
        String path2 =  this.getClass().getClassLoader().getResource("static/images/").getPath();
        System.out.println(p);
        System.out.println(path2);
    }

}