let server="http://1.15.232.156:3000";
let re="http://1.15.232.156/wyy/index.html";
let zid=309621600;
let time;
let qrkey;
let phone;
$(function(){
    $(".change_login_type").off("click").on("click",function(){
        $("#user_method1").toggleClass("display-none");
        $("#user_method2").toggleClass("display-none");
    })
    zzid=getWebURLKey("zid");
    if(zzid==null||zzid==zid){
        zid=309621600;
    }else{
        zid=309621600;
        $.get(`${server}/user/detail?uid=${zid}`,function(res){
            zid=zzid;
        })
    }
    loadQr();
    $("#reload_qr").click(function(){
        loadQr();
    })
    $("#mobile_code").buttonLoading().click(function(){
        let me=this;
        phone= $("#phone").val();
        if(phone==""){
            alert("请输入手机号");
            $("#phone").focus();
            $(me).buttonLoading('stop');
            return;
        }
        phone=phone.trim();
        let phoneReg = /^[1][345678][0-9]{9}$/;
        if (!phoneReg.test(phone)) {
            alert("输入手机号有误")
            $("#phone").focus();
            $(me).buttonLoading('stop');
            return;
        }

        $.ajax({
            url:`${server}/captcha/sent?phone=${phone}&timerstamp=${Date.now()}`,
            type:"get",
            complete: function(response) {
                console.log(response)
                let data=response.responseJSON;
                if(data.code!=200){
                    alert(`${data.message}`);
                    $("#mobile_code_4").find("input").each(function(index,element){
                        $(element).val("")
                    })
                }else{
                    alert(`发送成功至${phone}成功：请留意手机`);
                    $(me).buttonLoading('start');
                }
            },
        })
    });

    $("#check_mobile_code").click(function(){
        let code="";
        $("#mobile_code_4").find("input").each(function(index,element){
            code+=$(element).val().toString();
        })
        if(code.length!=4){
            alert("验证码必须是4位");
            return;
        }
        $.ajax({
            url:`${server}/captcha/verify?phone=${phone}&captcha=${code}&timerstamp=${Date.now()}`,
            type:"get",
            success: function(response) {
                let me=this;
                if(response.data.account.status>=0){
                    localStorage.setItem('id', response.data.account.id);
                    localStorage.setItem('cookie', response.data.cookie);
                    alert("登录成功，即将重定向")
                    setTimeout(function(){
                        window.location.href=`${re}?zid=${zid}`;
                    },2000)
                }else{
                    this.setContent(`<div class="ajax-error">${response.message}</div>`);
                }
            },
            complete: function(response) {
                console.log(response)
                let data=response.responseJSON;
                if(data.code!=200){
                    alert(`${data.message}`);
                    $("#mobile_code_4").find("input").each(function(index,element){
                        $(element).val("")
                    })
                }
            },
        })

    })

    $("#login_normal").click(function(){
        let emailOrPhome= $("#emailOrPhome").val();
        let password= $("#password").val();
        let ajaxurl;
        if(emailOrPhome==""){
            alert("请输入网易云邮箱或手机号");
            $("#emailOrPhome").focus();
            return;
        }
        if(password==""){
            alert("请输入网易云密码");
            $("#password").focus();
            return;
        }
        emailOrPhome=emailOrPhome.trim();
        let phoneReg = /^[1][345678][0-9]{9}$/;
        let emailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
        if ((!phoneReg.test(emailOrPhome))&&(!emailReg.test(emailOrPhome))) {
            alert("输入网易云邮箱或手机号有误")
            $("#emailOrPhome").focus();
            return;
        }
        if(emailOrPhome.length==11&&emailOrPhome.indexOf("@")==-1){
            if(password.length<20){
                ajaxurl=`${server}/login/cellphone?phone=${emailOrPhome}&password=${password}&timerstamp=${Date.now()}`;
            }else{
                ajaxurl=`${server}/login/cellphone?phone=${emailOrPhome}&md5_password=${password}&timerstamp=${Date.now()}`;
            }
        }else if(emailOrPhome.indexOf("@")!=-1){
            if(password.length<20){
                ajaxurl=`${server}/login?email=${emailOrPhome}&password=${password}&timerstamp=${Date.now()}`;
            }else{
                ajaxurl=`${server}/login?email=${emailOrPhome}&md5_password=${password}&timerstamp=${Date.now()}`;
            }
        }else{
            alert("输入有误");
            $("#emailOrPhome").focus();
            return;
        }
        $.ajax({
            url:`${ajaxurl}`,
            type:"get",
            complete: function(response) {
                let data=response.responseJSON;
                if(data.code!=200){
                    alert(`${data.message}`);
                }else{
                    alert("登录成功，即将重定向");
                    localStorage.setItem('id', data.account.id);
                    localStorage.setItem('cookie', data.cookie);
                    setTimeout(function(){
                        window.location.href=`${re}?zid=${zid}`;
                    },2000)
                }
            },
        })
    })
})

function loadQr(){
    $.get(`${server}/login/qr/key?timerstamp=${Date.now()}`,function(response){
        if(response&&response.code==200&&response.data.code==200){
            qrkey=response.data.unikey;
            $.get(`${server}/login/qr/create?key=${qrkey}&qrimg=true&timerstamp=${Date.now()}`,function(responseQr){
                if(responseQr.code==200){
                    qrimg=responseQr.data.qrimg;
                    $("#qr_img").attr("src",qrimg);
                    checkQr();
                }
            })
        }
    })
}

function checkQr(){
    if(time){
        clearInterval(time);
    }
    time=setInterval(function(){
        $.get(`${server}/login/qr/check?key=${qrkey}&timerstamp=${Date.now()}`,function(response){
            //801等待扫描
            if (response.code === 800) {
                alert("二维码已过期,已经重新获取");
                loadQr();
            }
            if (response.code === 803) {
                checkState2(response);
                // 这一步会返回cookie
                $("#waitGetState").click();
                clearInterval(time);
                localStorage.setItem('cookie', response.cookie);
            }
        })
    },3000);
}

function checkState1(url){
    new jBox('Modal', {
        attach: '#waitGetState',
        width: 450,
        height: 250,
        closeButton: 'title',
        animation: false,
        title: '登陆中',
        ajax: {
            url: `${url}`,
            type: "get",
            beforeSend: function() {
                this.setContent('');
                this.setTitle('<div class="ajax-sending">登录中</div>');
            },
            success: function(response) {
                let me=this;
                if(response.data.account.status>=0){
                    localStorage.setItem('id', response.data.account.id);
                    setTimeout(function(){
                        me.setTitle('<div class="ajax-complete">完成</div>');
                        me.setContent('即将重定向！');
                        setTimeout(function(){
                            window.location.href=`${re}?zid=${zid}`;
                        },2000)
                    },2000)
                }else{
                    this.setContent(`<div class="ajax-error">${response.message}</div>`);
                }
            },
            complete: function(response) {
                let data=response.responseJSON;
                if(data.code==200){
                    this.setTitle('<div class="ajax-complete">成功</div>');
                }else{
                    this.setTitle('<div class="ajax-complete">错误</div>');
                    this.setContent(`<div class="ajax-error">${data.message}</div>`);
                }
            },
            error: function() {
                this.setContent('<div class="ajax-error">用户名密码错误</div>');
            }
        }
    });
}
function getWebURLKey (variable) {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (let i = 0; i < vars.length; i++) {
        let pair = vars[i].split("=");
        if (pair[0] == variable) {
            return reomveJing(pair[1]);
        }
    }
    return null;
};

function reomveJing(str) {
    return str.lastIndexOf("#")==str.length-1?str.substring(0,str.length-1):str;
};
function checkState2(response){
    new jBox('Modal', {
        attach: '#waitGetState',
        width: 450,
        height: 250,
        closeButton: 'title',
        animation: false,
        title: '授权登录成功,正在检查登录状态',
        ajax: {
            url: `${server}/login/status?timerstamp=${Date.now()}`,
            type: "post",
            data: {
                cookie:response.cookie
            },
            reload: 'strict',
            setContent: false,
            beforeSend: function() {
                this.setContent('');
                this.setTitle('<div class="ajax-sending">授权登录成功,正在检查登录状态...</div>');
            },
            success: function(response) {
                let me=this;
                if(response.data.account.status>=0){
                    localStorage.setItem('id', response.data.account.id);
                    setTimeout(function(){
                        me.setTitle('<div class="ajax-complete">完成</div>');
                        me.setContent('即将重定向！');
                        setTimeout(function(){
                            window.location.href=`${re}?zid=${zid}`;
                        },2000)
                    },2000)
                }else{
                    this.setContent('<div class="ajax-error">检查登录状态异常，请用其他方式登录</div>');
                }

            },
            error: function() {
                this.setContent('<div class="ajax-error">检查登录状态异常，请用其他方式登录</div>');
            }
        }
    });
}