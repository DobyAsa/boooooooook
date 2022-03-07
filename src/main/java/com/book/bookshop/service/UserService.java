package com.book.bookshop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.bookshop.entity.User;
import com.book.bookshop.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @Author:yizhongwei
 * @Date:1/17 20:36
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    //验证用户是否存在
    // 根据页面控制层传过来的用户名去数据中中查找是否有与之相同的用户名
    public String checkUser(String username) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return "101";
        }
        else return "102";
    }

    //用户登录验证
    public String loginCheck(User loginUser, HttpSession session){
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginUser.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if (user==null){
            return "101";//用户不存在
        }else {//否则用户存在
            if (loginUser.getPassword().equals((user.getPassword()))){
                session.setAttribute("user",user);
                return "100";//密码正确 正常登录
            } else {
                return "102";//密码错误
            }
        }
    }



}
