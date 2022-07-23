let isAdd=true;
$(function () {
    let id=getWebURLKey("id");
    if(id==null){
        //新增
    }else{
        //查看
        $.get(`${ip}/hoppinzq?method=query&params={"id":${id}}`,function (data) {
            let msg=JSON.parse(data);
            if(msg.code!=200){

            }else{
                isAdd=false;
                $("#new_add").css("pointer-events","none");
                $("#show_tbody").html("");
                let pc=msg.data;
                $(".pctitle").html("爬虫(只读，不允许修改)："+pc.name);
                $(".pcIP").html(`接口：${ip}/hoppinzq?method=run&amp;params={"id":"${id}","url":"爬取的目标网页"}`)
                $("#url").val(pc.urldemo);
                if(pc.spiderBeanList.length>0){
                    $.each(pc.spiderBeanList,function (index,pcpz){
                        $("#show_tbody").append(`<tr>
                    <td>${pcpz.description}</td>
                    <td>${pcpz.key}</td>
                    <td>${pcpz.xpath==undefined?"":pcpz.xpath}</td>
                    <td>${pcpz.xpathFunction==undefined?"":pcpz.xpathFunction}</td>
                    <td>${pcpz.selector==undefined?"":pcpz.selector}</td>
                    <td>${pcpz.attr==undefined?"":pcpz.attr}</td>
                    <td>${pcpz.regex==undefined?"":pcpz.regex}</td>
                    <td>${pcpz.all?"是":"否"}</td>
                    <td>${pcpz.links?"是":"否"}</td>
                    <td>${pcpz.addLinks?"是":"否"}</td>
                    <td>
                        <a href="javaScript:void(0)" style="cursor: not-allowed">禁止</a>
                    </td>
                </tr>`)
                    })
                }
            }
        })
    }
    $(".sqzk").click(function () {
        $(this).next("div").slideToggle("slow");
    })
    $(".close_md").click(function () {
        $('#pachong').modal('hide');
        $('#testModal').modal('hide');
    })
    $('#new_add').click(function () {
        $('#radio_1_n').click();
        $('#radio_2_n').click();
        $('#radio_3_n').click();
        $('#pachong').modal('show');
    })
    $('#add_btn').click(function () {
        methods.addHandle()
    })

    $('#show_tbody').on('click','.edit', function () {
        trIndex = $('.edit', '#show_tbody').index($(this));
        addEnter = false;
        $(this).parents('tr').addClass('has_case');
        methods.editHandle(trIndex);
    })

    $('#search_btn').click(function () {
        methods.seachName();
    })

    $('#back_btn').click(function () {
        $('#Ktext').val(' ');
        methods.resectList();
    })

    $('.del').click(function () {
        $(this).parents('tr').remove();
    })

    $('#pachong').on('hide.bs.modal',function() {
        addEnter = true;
        $('#show_tbody tr').removeClass('has_case');
        $('#xztb input').val(' ');
        $('#xztb select').find('option:first').prop('selected', true)
    });
    $("#new_add_pc").click(function (){
        if(isAdd){
            $(".json").hide();
            $(".add_pc").show();
        }else{
            alert("该爬虫配置只读！")
        }
    })

    $("#text").click(function () {
        let arr=[]
        $('#show_tbody tr').each(function (index,tr){
            let o={};
            let $tds=$(tr).find("td");
            o.key=$tds[1].innerHTML;
            o.description=$tds[0].innerHTML;
            o.xpath=$tds[2].innerHTML;
            o.xpathFunction=$tds[3].innerHTML;
            o.selector=$tds[4].innerHTML;
            o.attr=$tds[5].innerHTML;
            o.regex=$tds[6].innerHTML;
            o.links=$tds[8].innerHTML=="是"?true:false;
            o.addLinks=$tds[9].innerHTML=="是"?true:false;
            o.isAll=$tds[7].innerHTML=="是"?true:false;
            arr.push(o);
        });
       if(arr.length>0){
           $('#testModal').modal('show');
           $("#test_l").buttonLoading().off("click").on("click", function () {
               $(".json").html("");
               let $me=$(this);
               $me.buttonLoading("start");
               if($("#url").val()==""){
                   alert("请填写url");
                   $("#url").focus();
                   $me.buttonLoading("stop");
                   return;
               }else{
                   let url =$("#url").val();
                   $.get(`${ip}/hoppinzq?method=test&params={"spiderMajor":{"name":"测试","desc":"测试","urldemo":"${url}"},"spiderBeans":${JSON.stringify(arr)}}`,function (data) {
                       $me.buttonLoading("stop");
                       $(".url__").val(url)
                       $(".json").html(new JSONFormat(data, 4).toString());
                   })
               }
           })
           $("#add_pc__").click(function () {
               if($(".pc__name").val()==""){
                   alert("名称必填");
                   $(".pc__name").focus();
                   return;
               }
               let name=$(".pc__name").val();
               let thread=$(".thread").val();
               let urldemo=$(".url__").val();
               let desc=$(".desc__").val();
               $.get(`${ip}/hoppinzq?method=insertSpiders&params={"spiderMajor":{"name":"${name}","desc":"${desc}","urldemo":"${urldemo}"},"spiderBeans":${JSON.stringify(arr)}}`,function (data) {
                   let m=JSON.parse(data);
                   if(m.code==200){
                       let p=m.data;
                       bootbox.alert({
                           title: "请记住您的专属爬虫接口",
                           message: `${ip}/hoppinzq?method=run&amp;params={"id":"${p.id}","url":"爬取的网站url"}`,
                           closeButton:false
                       })
                   }else{
                       alert("新增失败，请稍后尝试")
                   }
               })
           })
       }else{
           alert("请先添加数据")
       }
    })


})

var addEnter = true,
    noRepeat = true,
    jobArr = [],
    phoneArr = [],
    tdStr = '',
    trIndex,
    hasNullMes = false,
    tarInp = $('#xztb input'),
    tarSel = $('#xztb select');

var methods = {
    addHandle: function (the_index) {
        hasNullMes = false;
        methods.checkMustMes();
        if (hasNullMes) {
            return;
        }
        if (addEnter) {
            //methods.checkRepeat();
            if (noRepeat) {
                methods.setStr();
                $('#show_tbody').append('<tr>' + tdStr + '</tr>');
                $('#pachong').modal('hide');
            }
        }else{
            methods.setStr();
            $('#show_tbody tr').eq(trIndex).empty().append(tdStr);
            $('#pachong').modal('hide');
        }
    },
    editHandle: function (the_index) {
        $('#radio_1_n').click();
        $('#radio_2_n').click();
        $('#radio_3_n').click();
        let tar = $('#show_tbody tr').eq(the_index);
        let nowConArr = [];
        for (var i=0; i<tar.find('td').length-1;i++) {
            var a = tar.children('td').eq(i).html();
            nowConArr.push(a);
        }

        $('#pachong').modal('show');

        tarInp.eq(0).val(nowConArr[0]);
        tarInp.eq(1).val(nowConArr[1]);
        tarInp.eq(2).val(nowConArr[2]);
        tarSel.eq(0).val(nowConArr[3])
        tarInp.eq(3).val(nowConArr[4]);
        tarInp.eq(4).val(nowConArr[5]);
        tarInp.eq(5).val(nowConArr[6]);
        if(nowConArr[7]=="是"){
            tarInp.eq(6).click();
        }
        if(nowConArr[8]=="是"){
            tarInp.eq(8).click();
        }
        if(nowConArr[9]=="是"){
            tarInp.eq(10).click();
        }
        // for (var j=0;j<tarInp.length;j++) {
        //     tarInp.eq(j).val(nowConArr[j])
        // }
        // for (var p=0;p<tarSel.length;p++) {
        //     var the_p = p+tarInp.length;
        //     tarSel.eq(p).val(nowConArr[the_p]);
        // }

    },
    setStr: function () {

        tdStr = '';
        tdStr+= `<td>${tarInp.eq(0).val()}</td>
                <td>${tarInp.eq(1).val()}</td>
                   <td>${tarInp.eq(2).val()}</td>
<td>${tarSel.eq(0).val()}</td>
<td>${tarInp.eq(3).val()}</td><td>${tarInp.eq(4).val()}</td><td>${tarInp.eq(5).val()}</td><td>${tarInp.eq(6).get(0).checked?"是":"否"}</td>
<td>${tarInp.eq(8).get(0).checked?"是":"否"}</td>
<td>${tarInp.eq(10).get(0).checked?"是":"否"}</td><td><a href="#" class="edit">编辑</a> <a href="#" class="del">删除</a></td>`
        // for (var a=0; a<tarInp.length; a++) {
        //     tdStr+= '<td>' + tarInp.eq(a).val() + '</td>'
        // }
        // for (var b=0; b<tarSel.length; b++) {
        //     tdStr+= '<td>' + tarSel.eq(b).val() + '</td>'
        // }
        $('.del').on("click",function () {
            $(this).parents('tr').remove();
        })
    },
    seachName: function () {

        var a = $('#show_tbody tr');
        var nameVal = $('#Ktext').val().trim();
        var nameStr = '',
            nameArr = [];

        if (nameVal==='') {
            alert("搜索内容不能为空")
            return;
        }

        for (var c=0;c<a.length;c++) {
            var txt = $('td:first', a.eq(c)).html().trim();
            nameArr.push(txt);
        }

        a.hide();
        for (var i=0;i<nameArr.length;i++) {
            if (nameArr[i].indexOf(nameVal)>-1) {
                a.eq(i).show();
            }
        }
    },
    resectList: function () {
        $('#show_tbody tr').show();
    },
    checkMustMes: function () {

        if ($('.description').val().trim()==='') {
            alert("字段描述为必填项");
            $('.description').focus();
            hasNullMes = true;
            return
        }
        if ($('.key').val().trim()==='') {
            alert("键值为必填项");
            $('.key').focus();
            hasNullMes = true;
            return
        }
        if($('.xpath').val().trim()===''&&$('.cssSelector').val().trim()===''&&$('.regexS').val().trim()===''){
            alert("xpath，css选择器，正则表达式至少填写一个");
            hasNullMes = true;
            return
        }
    },
    checkRepeat: function () {

    }
}

function getWebURLKey(variable) {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (let i = 0; i < vars.length; i++) {
        let pair = vars[i].split("=");
        if (pair[0] == variable) {
            return this.reomveJing(pair[1]);
        }
    }
    return null;
}

function reomveJing(str) {
    return str.lastIndexOf("#")==str.length-1?str.substring(0,str.length-1):str;
}