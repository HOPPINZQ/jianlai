var player=null;
var logI=0,zoom=1;
var logCss=['alert alert-primary','alert alert-secondary','alert alert-success','alert alert-warning'];
var adFrontShow=false,adPauseShow=false;
$(document).ready(function($) {
    loadPlayer();
    $('.choiceinput').click(function(){
        var thisAdFrontShow=parseInt($('#adfrontshow').val())==1?true:false;
        if(thisAdFrontShow!=adFrontShow){
            adFrontShow=thisAdFrontShow;
        }
        var thisAdPauseShow=parseInt($('#adpauseshow').val())==1?true:false;
        if(thisAdPauseShow!=adPauseShow){
            adPauseShow=thisAdPauseShow;
        }
        playerClearAndLoad();
    });
});
function playerClearAndLoad(){
    if(player){
        player.remove();
        player=null;
        $('.video').html('');
        loadPlayer();
    }
}
function loadPlayer(){
    var videoObject = {
        container: '.video', //容器的ID或className
        title:'黑神化 悟空-国产3A大作',//视频标题
        autoplay: false, //是否自动播放
        poster: 'http://hoppinzq.com/zui/static/video/poster.png', //封面图片
        preview: { //预览图片
            file: ['http://hoppinzq.com/zui/static/video/1.png', 'http://hoppinzq.com/zui/static/video/2.png', 'http://hoppinzq.com/zui/static/video/3.png', 'http://hoppinzq.com/zui/static/video/4.png'],
            scale: 2,//预览图片截图间隔时间，单位：秒
            thumbnail:[10,10],//缩略图的每行数量，列数量（共多少列）
            type:0//预览图样式，0=单个，1=横排
        },
        loaded: 'loadedHandler', //当播放器加载后执行的函数
        rightBar:true,
        screenshot:true,
        smallWindows:true,
        playbackrateOpen:true,
        //controls:true,
        webFull:true,
        theatre:true,
        //language:'en',
        logo:'http://hoppinzq.com/zui/static/image/hoppinlogo.png',
        next:{
            link:'https://www.ckplayer.com',
            content:'.video-next'
        },
        ended:'.video-ended',
        prompt: [ //提示点
            {
                words: '对战：精英怪•赤尻马猴',
                time: 210
            },
            {
                words: '对战：亢金龙',
                time: 386
            }
        ],
        menu:[
            {
                title:'hoppin博客',
                link:'http://1.15.232.156/'
            },
            {
                title:'version:X3',
                underline:true
            },
            {
                title:'关于视频',
                click:'aboutShow'
            }
        ],
        information:{
            'ID：':'1e0bdbe69e692383',
            '已加载：':'{loadTime}秒',
            '总时长：':'{duration}秒',
            '视频尺寸：':'{videoWidth}x{videoHeight}',
            '音量：':'{volume}%',
            'FPS：':'{fps}',
            '音频解码：':'{audioDecodedByteCount} Byte',
            '视频解码：':'{videoDecodedByteCount} Byte',
            'TITLE：':'演示视频',
            'COPYRIGHT：':'视频版权归《黑神化.悟空》所有'
        },
        track:[
            {
                kind:'subtitles',
                src:'http://hoppinzq.com/zui/static/video/zh.vtt',
                srclang:'zh',
                label:'中文',
                default:false,
            },
            {
                kind:'subtitles',
                src:'http://hoppinzq.com/zui/static/video/en.vtt',
                srclang:'en',
                label:'English',
                default:false,
            }
        ],
        crossOrigin:'Anonymous',

        video: [

            ['http://hoppinzq.com/zui/static/video/1_480.mp4', 'video/mp4', '标清'],
            ['http://hoppinzq.com/zui/static/video/1_1920x1080.mp4', 'video/mp4', '超清']
        ]
    };
    if(parseInt($('#flashplayer').val())==1){
        videoObject['flashplayer']=true;
    }
    var ad=null;
    if(adFrontShow){//增加前贴片广告
        if(ad==null){
            ad={};
        }
        ad['front']={
            closeTime:3,//5秒后显示关闭广告
            closeButtonClick:'closeFront',//点击关闭按钮触发事件
            list:[
                {
                    file: 'https://ckplayer-video.oss-cn-shanghai.aliyuncs.com/ckplayer-ad/front01.mp4',
                    type: 'video/mp4',
                    link:'http://1.15.232.156/',
                    time: 5
                },
                {
                    file: 'ad/front.png',
                    type: 'picture',
                    link: 'http://1.15.232.156/',
                    time: 5
                },
                {
                    content: '.adfront',
                    type: 'node',
                    time: 5
                }
            ]
        };
    }
    if(adPauseShow){//增加暂停广告
        if(ad==null){
            ad={};
        }
        ad['pause']={
            close:true,
            list:[
                {
                    file: 'ad/pause.png',
                    link: 'http://1.15.232.156/',
                    time: 5
                },
                {
                    content: '.adpause',
                    type: 'node',
                    time: 5
                }
            ]
        };
    }
    if(ad!=null){
        videoObject['ad']=ad;
    }
    new ckplayer(videoObject);
}
function loadedHandler(name){//播放器加载成功后
    consoloLog('播放器正在构建监听，监听视频总时间，已播放时间，播放状态，暂停状态，是否暂停，音量改变，缓冲，时间跳转状态，全屏状态，播放结束状态');
    player=name;
    console.log(name)
    player.addListener('loadedMetaData', loadedMetaDataHandler); //监听元数据
    player.addListener('error', errorHandler); //监听错误
    player.addListener('time', timeHandler); //监听已播放时间
    player.addListener('play', playHandler); //监听播放状态
    player.addListener('pause', pauseHandler); //监听暂停状态
    player.addListener('volume', volumeHandler); //监听音量改变
    player.addListener('muted', mutedHandler); //监听静音
    player.addListener('buffer', bufferHandler); //监听缓冲状态
    player.addListener('seek', seekHandler); //监听跳转状态
    player.addListener('full', fullHandler); //监听全屏状态切换
    player.addListener('ended', endedHandler); //监听视频播放结束
    player.addListener('screenshot', screenshotHandler); //监听截图
    player.addListener('playbackRate', playbackRateHandler); //监听播速变化
    player.addListener('definition', definitionHandler); //监听播速清晰度变化
    player.addListener('webfull', webfullHandler); //监听网页全屏
    player.addListener('theatre', theatreHandler); //监听剧场模式
    player.addListener('smallWindows', smallWindowsHandler); //监听小窗口模式
    player.addListener('zoom', zoomHandler); //监听缩放
    player.addListener('loop', loopHandler); //监听循环播放
    player.addListener('frontAd', frontAdHandler); //监听前置广告
    player.addListener('frontAdClick', frontAdClickdHandler); //监听前置广告
    player.addListener('frontAdEnded', frontAdEndedHandler); //监听前置广告结束
    player.addListener('pauseAd', pauseAdHandler); //暂停广告
    player.addListener('pauseAdClick', pauseAdClickHandler); //监听单击暂停广告
    player.addListener('pauseAdClose', pauseAdCloseHandler); //监听单击暂停广告被关闭
    player.addListener('track', trackHandler); //监听字幕改变
    player.addListener('mouseActive', mouseActiveHandler); //监听鼠标活跃状态
}
function loadedMetaDataHandler(obj){//监听元数据
    var html='元数据读取完成<br/>';
    //var detail=obj['detail'];
    html+='元数据-分析如下：<br>';
    html+='播放器宽度（width）:'+obj['width']+'px<br>';
    html+='播放器高度（height）:'+obj['height']+'px<br>';
    html+='视频宽度（videoWidth）:'+obj['videoWidth']+'px<br>';
    html+='视频高度（videoHeight）:'+obj['videoHeight']+'px<br>';
    html+='视频总时间（duration）:'+obj['duration']+'秒<br>';
    html+='视频音量（volume）:'+obj['volume']+'<br>';
    consoloLog(html);

}
function getMetaData(){//主动获取元数据
    var html='元数据获取完成<br/>';
    html+='元数据-分析如下：<br>';
    html+='播放器宽度（width）:'+player.width()+'px<br>';
    html+='播放器高度（height）:'+player.height()+'px<br>';
    html+='视频宽度（videoWidth）:'+player.videoWidth()+'px<br>';
    html+='视频高度（videoHeight）:'+player.videoHeight()+'px<br>';
    html+='视频总时间（duration）:'+player.duration()+'秒<br>';
    html+='视频音量（volume）:'+player.volume()+'<br>';
    consoloLog(html);
}
function errorHandler(obj){
    consoloLog('错误编码：'+obj['code']+'，错误信息：'+obj['message']);
}
function timeHandler(){
    if(parseInt($('#addListenerTime').val())){
        consoloLog('监听到播放时间：'+player.time()+'秒');
    }
}
function playHandler(){
    consoloLog('监听到播放');
}
function pauseHandler(){
    consoloLog('监听到暂停');
}
function volumeHandler(v){
    consoloLog('监听到音量改变：'+v);
}
function mutedHandler(b){
    consoloLog('监听静音，是否处于静音：'+b.toString());
}
function bufferHandler(state){
    consoloLog('监听到缓冲，缓冲状态：'+state);
}
function seekHandler(obj){
    consoloLog('时间跳转(seek)，状态：'+obj['state']+'，跳转时间：'+obj['time']);
}
function endedHandler(){
    consoloLog('视频播放结束了');
}

function screenshotHandler(base64){
    var html='<img src="'+base64+'" style="max-width:100%">';
    consoloLog(html);
}
function playbackRateHandler(num){
    consoloLog('修改倍速：'+num);
}
function definitionHandler(obj){
    consoloLog('修改清晰度，ID：'+obj['id']+'，地址：'+obj['video']+'，名称：'+obj['title']);
}
function fullHandler(b){
    consoloLog('监听全屏,是否处于全屏：'+b.toString());
}
function webfullHandler(b){
    consoloLog('监听页面全屏，是否处于页面全屏：'+b.toString());
}
function theatreHandler(b){
    consoloLog('监听剧场模式，是否处于剧场模式：'+b.toString());
}
function smallWindowsHandler(b){
    consoloLog('监听小窗口模式，是否处于小窗口：'+b.toString());
}
function zoomHandler(num){
    consoloLog('视频缩放至：'+num+'%');
}
function loopHandler(b){
    consoloLog('监听循环播放，是否处于循环播放：'+b.toString());
}
function frontAdHandler(obj){
    if(obj['type']!='node'){
        consoloLog('播放前置广告，type：'+obj['type']+'，地址：'+obj['file']);
    }
    else{
        consoloLog('播放前置广告，type：'+obj['type']+'，显示层：'+obj['content']);
    }
}
function frontAdClickdHandler(obj){
    console.log(obj);
    consoloLog('监听点击前置广告');
}
function frontAdEndedHandler(){
    consoloLog('前置广告播放结束');
}
function pauseAdHandler(obj){
    consoloLog('播放暂停广告');
}
function pauseAdClickHandler(obj){
    console.log(obj);
    consoloLog('监听点击暂停广告');
}
function pauseAdCloseHandler(){
    consoloLog('暂停广告被关闭了');
}
function trackHandler(obj){
    if(obj){
        consoloLog('修改了字幕：'+obj['label']);
    }
}
function mouseActiveHandler(b){
    consoloLog('鼠标是否活跃（控制栏等是否显示）'+b.toString());
}
function closeFront(){
    var alert=mayiui.alert('点击关闭广告按钮可以执行其它操作，如果直接关闭广告可以不使用“closeButtonClick”属性',{
        title:'是否关闭广告',
        button:[
            {
                val:'关闭广告',
                class:'primary',
                fun:function(){
                    player.closeFrontAd(),
                        mayiui.close(alert);
                }
            },
            {
                val:'什么也不做',
                class:'secondary',
                fun:function(){
                    mayiui.close(alert);
                }
            }
        ],
        closeHandler:function(ele){
        }
    });
}
//下面为控制
function videoSize(b){//控制播放器尺寸，b=true为增加
    var w=$('.video').width(),h=$('.video').height();
    if(b){
        if(w<$('body').width()){
            w+=10;
        }
    }
    else{
        if(w>520){
            w-=10;
        }
    }
    $('.video').css('width',w+'px');
}
function screenshot(){
    var html='<img src="'+player.screenshot()+'" style="max-width:100%">';
    consoloLog(html);
    player.closeScreenshot(true);
}
//添加一个文件到播放器中
function newElement(){
    player.layer('.elementtemp');

}
function deleteElement() {
    player.closeLayer('.elementtemp');
}
var dmArr=[];
function newDanmu() {
    //弹幕
    var color=['#FFF','#FFDD00','#e4ff02','#ad1','#11ddb7','#FF0000'];
    var ele=player.layer({
        class:'dm',
        content:'弹幕，'+getNowTime()
    });
    ele.css({
        'top':Math.ceil(Math.random()*(player.height()-30))+'px',
        'font-size':Math.ceil(Math.random()*6)+14+'px',
        'color':color[Math.ceil(Math.random()*color.length)-1]
    });
    ele.mouseover(function(){
        ele.animatePause();
    });
    ele.mouseout(function(){
        ele.animatePlay();
    });
    ele.animate('left:'+(-ele.getWidth()+'px'),Math.ceil(Math.random()*20)*1000,'',function(){
        deleteChild(ele);
    });
    dmArr.push(ele);
}
function danmuHide(){
    for(var i=0;i<dmArr.length;i++){
        dmArr[i].hide();
    }
}
function danmuShow(){
    for(var i=0;i<dmArr.length;i++){
        dmArr[i].show();
    }
}
function deleteChild(ele) {
    for(var i=0;i<dmArr.length;i++){
        if(dmArr[i]==ele){
            dmArr.splice(i,1);
            ele.remove();
            break;
        }
    }
}

function getNowTime(){
    var date = new Date();
    this.hour = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
    this.minute = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
    this.second = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
    this.milliSeconds = date.getMilliseconds();
    var currentTime = this.hour + ':' + this.minute + ':' + this.second + '.' + this.milliSeconds;
    return currentTime;
}
function consoloLog(title,name){
    var css=logCss[logI];
    logI++;
    if(logI>logCss.length-1){
        logI=0;
    }
    if(!isUndefined(name)){
        name='  播放器名称：'+name;
    }
    else{
        name='';
    }
    var html='<div class="'+css+'"><p>'+getNowTime()+name+'</p><p class="title">'+title+'</p></div>';
    var thisHtml=$.trim($('.loglist').html());
    if(!thisHtml){
        $('.loglist').append(html);
    }
    else{
        $('.loglist div').eq(0).before(html);
    }
    var len=$('.loglist div').length;
    if(len>60){
        $('.loglist div').last().remove();
    }
}

function isUndefined(value) {
    try {
        if(value === 'undefined' || value === undefined || value === null || value === 'NaN' || value === NaN) {
            return true;
        }
    } catch(event) {
        return true;
    }
    return false;
}
function adjump(){
    showMessage('用户点击了跳过广告按钮，可以进行跳转到注册页或直接跳过广告')
}
