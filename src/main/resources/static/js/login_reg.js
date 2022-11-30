//验证用户名是否已经存在
function checkUser(obj) {
    $.ajax({
        url: contextPath + "/user/checkUserName",
        data: {"username": obj.value},
        method: "post",
        success: function (data) {
            $("#msg").css("display", "block");
            if (data == 102) {//用户存在
                $("#tip").html("用户名已存在");
                $("#tip").removeClass("alert-success");
                $("#tip").addClass("alert-danger");
            } else if (obj.value == '') {
                $("#tip").html("用户名不为空");
                $("#tip").removeClass("alert-success");
                $("#tip").addClass("alert-danger");
            } else {
                $("#tip").html("用户名可以注册");
                $("#tip").removeClass("alert-danger");
                $("#tip").addClass("alert-success");
            }
        }
    })
}

//检查注册页输入的邮箱是否已存在
function checkRegEmail() {
    var email = $("#email").val();
    $.ajax({
        url: contextPath + "/user/checkRegEmail",
        type: "post",
        data: {"email": email},
        success: function (data) {
            if (data == 'success') {
                layer.msg('邮箱已存在')
                return;
            }
        }
    })
}

//用户注册
function register() {
    var username = $("#regUsername").val();
    var pwd = $("#regPwd").val();
    var email = $("#email").val();
    var tel = $("#regTel").val();
    var school = $("#regSchool").val();
    var emailReg = new RegExp("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");//邮箱正则
    var schoolReg = new RegExp("^[\u4e00-\u9fa5]{0,}$");//汉字正则
    if (username == '') {
        layer.msg('用户名不为空', {icon: 7, anim: 6});
        return;
    }
    if (pwd == '') {
        layer.msg('密码不为空', {icon: 7, anim: 6});
        return;
    }
    if (email == '') {
        layer.msg('邮箱不为空', {icon: 7, anim: 6});
        return;
    }
    if (tel == '') {
        layer.msg('电话不为空', {icon: 7, anim: 6});
        return;
    }
    if (!emailReg.test(email)) {
        layer.msg('邮箱不合法！', {icon: 7, anim: 6});
        return;
    }

    $.ajax({
        url: contextPath + "/user/checkRegEmail",
        type: "post",
        data: {"email": email},
        success: function (data) {
            if (data == 'success') {
                layer.msg('邮箱已存在');
                return;
            } else {
                //电话号码正则
                var telReg = new RegExp("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");
                if (!telReg.test(tel)) {
                    layer.msg('电话号码不合法！', {icon: 7, anim: 6});
                    return;
                }
                var datas = $("#regForm").serialize();
                $.ajax({
                    url: contextPath + "/user/register",
                    data: datas,
                    method: "post",
                    success: function (data) {
                        if (data == 'success') {
                            layer.msg('注册成功，请登录')
                            $("#register").modal('hide');
                        }
                    }
                })
            }
        }
    })

}

//用户登录
function login() {
    var username = $("#username").val();
    var datas = $("#loginForm").serialize();
//通过名字获取  getElementsByName
    var obj1 = document.getElementsByName("sex");
    for (var i = 0; i < obj1.length; i++) {
        if (obj1[i].checked) {
            if (obj1[i].value == 'user') {//普通用户
                $.ajax({
                    url: contextPath + "/user/login",
                    data: datas,
                    method: "post",
                    success: function (data) {
                        $("#userTip").css("display", "none");
                        $("#pwdTip").css("display", "none");
                        $("#codeTip").css("display", "none");
                        if (data == 100) {
                            $("#loginModal").modal('hide');
                            window.location.href = contextPath + "/book/index";
                        } else if (data == 101) {
                            $("#userTip").css("display", "block");
                        } else if (data == 103) {
                            $("#codeTip").css("display", "block");
                        } else if (data != 100 || data != 101 || data != 102 || data != 103) {
                            // layer.msg('该账号已被封禁！', {icon: 2,anim:6});
                            layer.confirm('您因' + data + ',账号已被封禁,是否申诉？', {
                                icon: 3,
                                btn: ['是', '否']
                            }, function (index, layero) {
                                //按钮【按钮一】的回调
                                //检查该用户是否已经申诉
                                $.ajax({
                                    url: contextPath + "/user/checkAppeal",
                                    data: {"username": username},
                                    method: "post",
                                    success: function (data) {
                                        if (data == 'success') {
                                            layer.msg('您已提交申诉，请等待审核')
                                            return;
                                        } else {
                                            location.href = contextPath + "/user/toAppeal?username=" + username;
                                        }
                                    }
                                })

                            }, function (index) {
                                return;
                            });
                        } else {
                            $("#pwdTip").css("display", "block");
                        }
                    }
                })
            } else {//管理员
                $.ajax({
                    url: contextPath + "/admin/login",
                    data: datas,
                    method: "post",
                    success: function (data) {
                        $("#userTip").css("display", "none");
                        $("#pwdTip").css("display", "none");
                        $("#codeTip").css("display", "none");
                        if (data == 100) {
                            $("#loginModal").modal('hide');
                            // window.location.href = contextPath + "/book/index";
                            window.location.href = contextPath + "/admin/toBookAdmin";
                        } else if (data == 101) { //用户名不存在
                            $("#userTip").css("display", "block");
                        } else if (data == 103) { //验证码错误
                            $("#codeTip").css("display", "block");
                        } else if (data == 102) { //密码错
                            $("#pwdTip").css("display", "block");
                        }
                    }
                })
            }
        }

    }

}

//检测邮箱是否存在
function checkEmail(obj) {
    $.ajax({
        url: contextPath + "/user/checkEmail",
        data: {"email": obj.value},
        method: "post",
        success: function (data) {
            if (data == 101) {//邮箱不存在
                /*                var email = $("#emailLogin").val();
                                if (email=='') $("#noEmail").html("邮箱不为空");*/
                $("#emailTip").css("display", "block");
                return;
            } else {
                $("#emailTip").css("display", "none");
            }
        }
    })
}

//邮箱登录
function doEmailLogin() {
    var inputCode = $("#inputCode").val();
    var email = $("#emailLogin").val();
    if (email == '') {
        layer.msg('邮箱必填', {icon: 7, anim: 6});
        return;
    }
    if (inputCode == '') {
        layer.msg('验证码必填', {icon: 7, anim: 6});
        return;
    }
    $.ajax({
        url: contextPath + "/user/emailLogin",
        data: {"inputCode": inputCode, "email": email},
        method: "post",
        success: function (data) {
            /*            $("#userTip").css("display","none");
                        $("#pwdTip").css("display","none");
                        $("#codeTip").css("display","none");*/
            if (data == 100) {
                $("#emailLoginModal").modal('hide');
                window.location.href = contextPath + "/book/index";
            } else if (data == 102) {
                //未发送验证码
                layer.msg('请发送验证码')
                return;
            } else if (data != 100 || data != 101 || data != 102) {
                // layer.msg('该账号已被封禁！', {icon: 2,anim:6});
                layer.confirm('您因' + data + ',账号已被封禁,是否申诉？', {
                    icon: 3,
                    btn: ['是', '否']
                }, function (index, layero) {
                    //按钮【按钮一】的回调
                    //检查该用户是否已经申诉
                    $.ajax({
                        url: contextPath + "/user/checkAppeal",
                        data: {"username": username},
                        method: "post",
                        success: function (data) {
                            if (data == 'success') {
                                layer.msg('您已提交申诉，请等待审核')
                                return;
                            } else {
                                location.href = contextPath + "/user/toAppeal?username=" + username;
                            }
                        }
                    })

                }, function (index) {
                    return;
                });
            } else {//验证吗错误

                layer.msg('验证码错误')
            }
        }
    })
}

//发送验证码
function sendCode() {
    if ($('#emailTip').css('display') != 'none') {
        layer.msg('请输入正确的邮箱')
        return;
    }
    var email = $("#emailLogin").val();
    if (email == '') {
        layer.msg('请输入邮箱')
        return;
    }
//////////////////////////////////////
    //发送邮箱验证码按钮倒数
    var count = 20;
    var countdown = setInterval(CountDown, 500);

    function CountDown() {
        $("#sendBtn").attr("disabled", true);
        $("#sendBtn").html("请等待 " + count + " 秒!");
        if (count == 0) {
            $("#sendBtn").html("发送验证码").removeAttr("disabled");
            clearInterval(countdown);
        }
        count--;
    }

/////////////////////////////////////////////

    layer.msg('已发送，请注意查收')
    $.post(contextPath + "/user/sendEmail", {"email": email}, function (data) {
        if (data == 200) {
            layer.msg('发送成功，请输入验证码')
            $("#inputCode").attr("disabled", false)
        } else layer.msg('发送失败')
    });
}

$("#emailLoginBtn").click(function () {
    $("#loginModal").modal('hide');
})


