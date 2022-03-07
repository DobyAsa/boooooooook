package com.book.bookshop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Author:yizhongwei
 * @Date:3/7 22:04
 */
@Controller
public class Upload {
    @RequestMapping("/toUpload")
    public String  toUpload(){
        return "upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("bookPic") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        String filePath = "D:/images/";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            System.out.println("success");
            return "上传成功";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "上传失败！";
    }
}
