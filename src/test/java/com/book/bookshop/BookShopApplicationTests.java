package com.book.bookshop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.book.bookshop.entity.*;
import com.book.bookshop.entity.enums.Category;
import com.book.bookshop.mapper.*;
import com.book.bookshop.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    @Autowired
    private CommentService commentService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private AddressMapper addressMapper;
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
    }

    @Test
    public void tes1t() {
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper();
//        queryWrapper.and(wrapper -> wrapper.eq("asd","asd").ne("asd","asd"));
        queryWrapper.eq("id", 38).eq("order_id", 35);
    }

    @Test
    public void te2s1t() {
        String p = this.getClass().getResource("/static/images/").getPath();
        String path2 = this.getClass().getClassLoader().getResource("static/images/").getPath();
        System.out.println(p);
        System.out.println(path2);
    }

    @Test
    public void asdas(){
        Book book = bookService.getById(1);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("book_id",1);
        List<Comment> comments = commentService.list(queryWrapper);
        for (Comment comment:comments){
            User user = userService.getById(comment.getUserId());
            comment.setUsername(user.getUsername());
        }
        System.out.println(comments);
    }

    //随机生成一个0到9的数，生成一百次。输出为0~9及其生成次数
    @Test
    public void asd(){
        Random random = new Random();
        Map map = new HashMap();
        for (int i=0;i<100;i++){
            int n  = random.nextInt(10);
            //            list.add();
            if (map.size()!=0){
                if (!map.containsKey(n)){
                    map.put(n,1);

                }else {
                    int c  = (int) map.get(n);
                    map.replace(n,++c);
                }
            }else {
                map.put(n,1);
            }


        }
        for (int i =0 ;i<map.size();i++){
            System.out.println(i+"出现的次数："+map.get(i));
        }

      }

      @Test
      public void asdasd() {
        String inputBookName = "男";
          QueryWrapper<Book> queryWrapper1 = new QueryWrapper();
          QueryWrapper<Book> queryWrapper2 = new QueryWrapper();
          QueryWrapper<Book> queryWrapper3 = new QueryWrapper();
          List<Book> bookList = new ArrayList<>();
          queryWrapper1.like("name",inputBookName);

           bookList.addAll(bookService.list(queryWrapper1));
           queryWrapper2.like("author",inputBookName);
          bookList.addAll(bookService.list(queryWrapper2));
          queryWrapper3.like("info",inputBookName);
          bookList.addAll(bookService.list(queryWrapper3));


          Iterator it = bookList.iterator();
          while(it.hasNext()) {
              Book book = (Book) it.next();
              if (book.getState() == 0) {
                  it.remove(); //移除该对象
              }
          }
          for (Book book : bookList){
              System.out.println(book);
          }
    }
    @Test
    public void asdaasd(){
        List<Order> orders = orderService.list();
        for (Order order:orders){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("order_id",order.getId());
            List<com.book.bookshop.entity.OrderItem> orderItems = orderItemService.list(queryWrapper);
            order.setOrderItems(orderItems);
            for (com.book.bookshop.entity.OrderItem item:orderItems){
                Book book = bookService.getById(item.getBookId());
                item.setBook(book);
            }
            System.out.println(order);
        }
    }

    @Test
    public void asdaa123sd(){
        OrderQueryVo orderQueryVo = new OrderQueryVo();
        orderQueryVo.setPage(2);
        orderQueryVo.setPageSize(4);
        Integer begin = (orderQueryVo.getPage() - 1) * orderQueryVo.getPageSize();
//        Integer end = orderQueryVo.getPage() * orderQueryVo.getPageSize(); bug之一：详细了解mysql limit用法
        Integer end =  orderQueryVo.getPageSize();
        orderQueryVo.setBegin(begin);
        orderQueryVo.setEnd(end);
        orderQueryVo.setUserId(null);
        List<Order> list = orderMapper.findOrderAndOrderDetailListByUser(orderQueryVo);
        System.out.println(list);
    }

}
