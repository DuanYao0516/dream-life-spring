package com.example.hellospring.controller.user;

import com.example.hellospring.service.MailService;
import com.example.hellospring.util.Result;
import com.example.hellospring.util.ResultGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Controller
public class MailController {
    @Autowired
    private MailService mailService;

    @PostMapping("mailCaptcha")
    @ResponseBody
    public Result mail(HttpServletRequest request, @RequestParam("email") String email) {
        if (!mailService.isEmailValid(email)) {
            return ResultGenerator.genFailResult("邮箱已被注册！！！");
        }

        String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String message = "您的注册验证码为："+ checkCode;

        // 开发中若不想真正发送邮件，请注释掉下面这行代码
        mailService.sendSimpleMail(email, "欢迎使用 DreamLife!", message);
        System.out.println(message);

        request.getSession().setAttribute("checkCode", checkCode);
        // 验证码有效时间为十分钟
        request.getSession().setAttribute("ExpirationTime", System.currentTimeMillis() + 600000);
        // 连续发送验证码次数最多为八次
        if (request.getSession().getAttribute("sendTimes") == null) {
            request.getSession().setAttribute("sendTimes", 1);
        } else if ((int) request.getSession().getAttribute("sendTimes") >= 8) {
            return ResultGenerator.genFailResult("连续发送次数超限！！！");
        }
        request.getSession().setAttribute("sendTimes", (int) request.getSession().getAttribute("sendTimes") + 1);
        return ResultGenerator.genSuccessResult();
    }
}
