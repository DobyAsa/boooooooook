package com.book.bookshop.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class Email {

    private static final String EMAIL = "994783520@qq.com"; // QQ邮箱
    private static final String STMP = "rdgvzkinzwxsbdeg"; // 开通QQ邮箱的POP3/SMTP服务才有： STYMP验证码
    private static final String USERNAME = "994783520"; // QQ 账号

    public static String sendEmail(String toEmail) {
        String result = null;
        try {
            // 创建Properties 类用于记录邮箱的一些属性
            Properties props = new Properties();
            // 表示SMTP发送邮件，必须进行身份验证
            props.put("mail.smtp.auth", "true");
            //此处填写SMTP服务器
            props.put("mail.smtp.host", "smtp.qq.com");
            //端口号，QQ邮箱端口587
            props.put("mail.smtp.port", "587");
            // 此处填写，写信人的账号
            props.put("mail.user", Email.EMAIL);


            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    return new PasswordAuthentication(Email.USERNAME, Email.STMP);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
            message.setFrom(form);

            // 设置收件人的邮箱
            InternetAddress to = new InternetAddress(toEmail);
            message.setRecipient(MimeMessage.RecipientType.TO, to);

            // 设置邮件标题
            message.setSubject("系统邮箱验证，请注意查收");

            StringBuffer stringBuffer = new StringBuffer();
            Random random = new Random();

            for (int i = 0; i < 6; i++) {
                int nextInt = random.nextInt(9);
                stringBuffer.append(nextInt);
            }
            // 设置邮件的内容体
            message.setContent("某系统的验证码:" + stringBuffer.toString(), "text/html;charset=UTF-8");

            // 最后当然就是发送邮件啦
            Transport.send(message);
            result = stringBuffer.toString();
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

}
