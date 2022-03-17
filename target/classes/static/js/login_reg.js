//验证用户名是否已经存在
function checkUser(obj) {
    $.ajax({
        url: contextPath + "/user/checkUserName",
        data:{"username":obj.value},
        method:"post",
        success:function (data) {
            $("#msg").css("display","block");
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

//用户注册
function register() {
    var datas = $("#regForm").serialize();
    $.ajax({
        url: contextPath + "/user/register",
        data:datas,
        method:"post",
        success:function (data) {
            if(data == 'success'){
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
        for(var i=0; i<obj1.length; i ++){
            if(obj1[i].checked){
                if (obj1[i].value == 'user'){//普通用户
                    $.ajax({
                        url: contextPath + "/user/login",
                        data:datas,
                        method:"post",
                        success:function (data) {
                            $("#userTip").css("display","none");
                            $("#pwdTip").css("display","none");
                            $("#codeTip").css("display","none");
                            if(data == 100){
                                $("#loginModal").modal('hide');
                                window.location.href = contextPath + "/book/index";
                            }  else if(data == 101) {
                                $("#userTip").css("display","block");
                            }   else if(data == 103){
                                $("#codeTip").css("display","block");
                            }   else if (data == 104){
                                // layer.msg('该账号已被封禁！', {icon: 2,anim:6});
                                layer.confirm('账号已被封禁,是否申诉？', {
                                    icon:3,
                                    btn: ['是', '否']
                                }, function(index, layero){
                                    //按钮【按钮一】的回调
                                    alert(username)
                                    location.href = contextPath + "/user/toAppeal?username=" + username;
                                }, function(index){
                                    return ;
                                });
                            }
                                else {
                                $("#pwdTip").css("display","block");
                            }
                        }
                    })
                }else {//管理员
                    $.ajax({
                        url: contextPath + "/admin/login",
                        data:datas,
                        method:"post",
                        success:function (data) {
                            $("#userTip").css("display","none");
                            $("#pwdTip").css("display","none");
                            $("#codeTip").css("display","none");
                            if(data == 100){
                                $("#loginModal").modal('hide');
                                // window.location.href = contextPath + "/book/index";
                                window.location.href = contextPath + "/admin/toBookAdmin";
                            }  else if(data == 101) { //用户名不存在
                                $("#userTip").css("display","block");
                            }   else if(data == 103){ //验证码错误
                                $("#codeTip").css("display","block");
                            } else if (data == 102) { //密码错
                                $("#pwdTip").css("display","block");
                            }
                        }
                    })
                }
            }

        }

}