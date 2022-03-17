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
        url:contextPath + "/user/checkRegEmail",
        type:"post",
        data:{"email":email},
        success:function (data) {
            if (data=='success'){
                layer.msg('邮箱已存在')
                return;
            }
        }
    })
}
//用户注册
function register() {
    var datas = $("#regForm").serialize();
    $.ajax({
        url: contextPath + "/user/register",
        data: datas,
        method: "post",
        success: function (data) {
            if (data == 'success') {
                alert("注册成功，请登录！");
                $("#register").modal('hide');
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
                        } else if (data == 104) {
                            // layer.msg('该账号已被封禁！', {icon: 2,anim:6});
                            layer.confirm('账号已被封禁,是否申诉？', {
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
            } else {//验证吗错误
                layer.msg('验证码错误')
            }
        }
    })
}

function sendCode() {
    layer.msg('已发送，请查收')
    var email = $("#emailLogin").val();
    $.post(contextPath + "/user/sendEmail", {"email": email}, function (data) {
        if (data == 200){
            layer.msg('发送成功')
        }else layer.msg('发送失败')
    });
}