package com.book.bookshop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.bookshop.entity.Admin;
import com.book.bookshop.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @Author:yizhongwei
 * @Date:3/8 16:25
 */
@Service
public class AdminService extends ServiceImpl<AdminMapper, Admin> {
    @Autowired
    private AdminMapper adminMapper;

    //用户登录验证
    public String loginCheck(Admin loginAdmin, HttpSession session, String code) {
        String code1 = (String) session.getAttribute("code");
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginAdmin.getUsername());
        Admin admin = adminMapper.selectOne(queryWrapper);
        if (admin == null) {
            return "101";//用户不存在
        } else {//否则用户存在
            if (!code1.equalsIgnoreCase(code)) {//// 无视大小写
//            model.addAttribute("msg","验证码输入错误");
                return "103";//验证码错误
            } else if (loginAdmin.getPassword().equals((admin.getPassword()))) {
                session.setAttribute("admin", admin);
                return "100";//密码正确 正常登录
            } else {
                return "102";//密码错误
            }
        }
    }
}
