package com.book.bookshop.web;

import com.book.bookshop.entity.User;
import com.book.bookshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author:yizhongwei
 * @Date:1/17 20:29
 * 用户控制器
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //验证用户是否存在
    @ResponseBody
    @PostMapping("/checkUserName")
    public String checkUserName(String username) {
        return userService.checkUser(username);
    }

    //用户注册
    @ResponseBody
    @PostMapping("/register")
    public String register(User user){
        userService.save(user);
        return "success";
    }

    //用户登录
    @ResponseBody
    @PostMapping("/login")
    public String login(User user , HttpSession session){
        return userService.loginCheck(user,session);
    }

    //注销
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/book/index";
    }

    //跳转个人信息页面
    @RequestMapping("/userInfo")
    public String userInfo(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        return "userInfo";
    }

    //个人信息修改
    @PostMapping("/userInfoChange")
    @ResponseBody
    public String userInfoChange(User userInfo,HttpSession session){
        System.out.println(userInfo);
        User user = (User) session.getAttribute("user");
        userInfo.setId(user.getId());
        //把会话中旧的user去掉
        session.removeAttribute("user");
        //改成修改后的新的user
        session.setAttribute("user",userInfo);
        //这方式是根据主键id来修改的
        if (userService.saveOrUpdate(userInfo)){
            return "success";
        }else
            return "fail";
    }

    //跳到密码修改页面
    @RequestMapping("/toChangePwd")
    public String toChangePwd(){
        return "pwdChange";
    }
    //密码修改
    @RequestMapping("/pwdChange")
    @ResponseBody
    public String pwdChange(String newPwd,HttpSession session){
        User user = (User) session.getAttribute("user");
        user.setPassword(newPwd);
        if (userService.saveOrUpdate(user)){
            session.removeAttribute("user");
            return "success";
        }else
            return "fail";
    }

}
