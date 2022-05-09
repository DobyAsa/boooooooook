package com.book.bookshop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.bookshop.entity.User;
import com.book.bookshop.mapper.UserMapper;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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
        } else return "102";
    }
    //验证邮箱是否存在
    public String checkEmail(String email) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            //不存在，可以注册
            return "101";
        } else return "100";
    }
    //用户登录验证
    public String loginCheck(User loginUser, HttpSession session,String code) {
        //获取生成的验证码
        String code1 = (String) session.getAttribute("code");
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginUser.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return "101";//用户不存在
        } else {//否则用户存在
            if (!code1.equalsIgnoreCase(code)) {//// 无视大小写
//            model.addAttribute("msg","验证码输入错误");
                return "103";//验证码错误
            } else if (user.getState()==2){
                return user.getForbidReason();//该账号被封禁
            }
            else if (loginUser.getPassword().equals((user.getPassword()))) {
                session.setAttribute("user", user);
                return "100";//密码正确 正常登录
            } else {
                return "102";//密码错误
            }
        }
    }

    //邮箱登录验证
    public String emailLoginCheck(String inputCode, String email,HttpSession session){
        String realCode = (String) session.getAttribute("eCode");
        if (realCode==null){
            return "102";//请发送验证码
        }
        if (!realCode.equals(inputCode)){
            return "101";//邮箱验证码错误
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        User user = userMapper.selectOne(queryWrapper);
        if (user.getState()==2){
            return user.getForbidReason();//该账号被封禁
        }
        session.setAttribute("user",user);
        return "100";
    }


}
