var local="http://127.0.0.1/"
//ajax封装

//亲切问候~~~~致最亲爱的您
var sayHello = () => {
    console.error(([] + ![])[--[~+""][+[]] * [~+[]] + ~~!+[]]
        + [][(([] + ![])[+!![] + !![] + !![]]) + (([] + {})[+!![]]) + (([] + !![])[+!![]]) + (([] + !![])[+[]])][(([] + {})[+!![] + !![] + !![] + !![] + !![]]) + (([] + {})[+!![]]) + (([] + [][[]])[+!![]]) + (([] + ![])[+!![] + !![] + !![]]) + (([] + !![])[+[]]) + (([] + !![])[+!![]]) + (([] + [][[]])[+[]]) + (([] + {})[+!![] + !![] + !![] + !![] + !![]]) + (([] + !![])[+[]]) + (([] + {})[+!![]]) + (([] + !![])[+!![]])]((([] + !![])[+!![]]) + (([] + !![])[+!![] + !![] + !![]]) + (([] + !![])[+[]]) + (([] + [][[]])[+[]]) + (([] + !![])[+!![]]) + (([] + [][[]])[+!![]]) + (([] + {})[+!![] + !![] + !![] + !![] + !![] + !![] + !![]]) + (([] + [][[]])[+[]]) + (([] + [][[]])[+!![]]) + (([] + !![])[+!![] + !![] + !![]]) + (([] + ![])[+!![] + !![] + !![]]) + (([] + {})[+!![] + !![] + !![] + !![] + !![]]) + (([] + ![])[+!![]]) + ((+([] + (+!![] + !![]) + (+!![] + !![] + !![] + !![] + !![])))[(([] + !![])[+[]]) + (([] + {})[+!![]]) + (([] + ([] + [])[(([] + {})[+!![] + !![] + !![] + !![] + !![]]) + (([] + {})[+!![]]) + (([] + [][[]])[+!![]]) + (([] + ![])[+!![] + !![] + !![]]) + (([] + !![])[+[]]) + (([] + !![])[+!![]]) + (([] + [][[]])[+[]]) + (([] + {})[+!![] + !![] + !![] + !![] + !![]]) + (([] + !![])[+[]]) + (([] + {})[+!![]]) + (([] + !![])[+!![]])])[+!![] + !![] + !![] + !![] + !![] + !![] + !![] + !![] + !![]]) + (([] + !![])[+[]]) + (([] + !![])[+!![]]) + (([] + [][[]])[+!![] + !![] + !![] + !![] + !![]]) + (([] + [][[]])[+!![]]) + (([] + ([] + [])[(([] + {})[+!![] + !![] + !![] + !![] + !![]]) + (([] + {})[+!![]]) + (([] + [][[]])[+!![]]) + (([] + ![])[+!![] + !![] + !![]]) + (([] + !![])[+[]]) + (([] + !![])[+!![]]) + (([] + [][[]])[+[]]) + (([] + {})[+!![] + !![] + !![] + !![] + !![]]) + (([] + !![])[+[]]) + (([] + {})[+!![]]) + (([] + !![])[+!![]])])[+([] + (+!![]) + (+!![] + !![] + !![] + !![]))])](+([] + (+!![] + !![] + !![]) + (+!![] + !![] + !![] + !![] + !![] + !![])))) + (([] + !![])[+!![] + !![] + !![]]))()(('%' + ([] + (+!![] + !![] + !![] + !![] + !![] + !![])) + ([] + (+!![] + !![] + !![] + !![] + !![] + !![] + !![] + !![]))))
        + (!(~+[]) + {})[+!![]]+ ({} + [])[[~!+[]] * ~+[]]+(`${""._}`)[([]+(+!![]+!![]+!![]+!![]+!![]))])//您好！
    console.error((!(~+[]) + {})[--[~+""][+[]] * [~+[]] + ~~!+[]] + ({} + [])[[~!+[]] * ~+[]]+
        ({} + [])[--[~+""][+[]] * [~+[]] + ~~!+[]] + (!(~+[]) + {})[--[~+""][+[]] * [~+[]] + ~~!+[]])//我爱JS
}

//公共方法 listChunk 获取分割的数组
var listChunk = (list, size = 1, cacheList = []) => {
    let tmp = [...list]
    if (size <= 0) {return cacheList}
    while (tmp.length) {cacheList.push(tmp.splice(0, size))}
    return cacheList;
};
// console.log(listChunk([1, 2, 3, 4, 5, 6, 7, 8, 9], 3)) // [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
// 公共方法 getListPublic 获取数组交集,多用于echarts *
var getListPublicPart = (list, ...args) => list.filter(item => args.every(list => list.includes(item)));
// console.log(getListPublicPart([2, 1], [2, 3])) // [2]
// 公共方法 getOffsetBydocument 获取当前元素相对于document的偏移量
var getOffsetBydocument = el => {
    let {top,left} = el.getBoundingClientRect();
    let {scrollTop,scrollLeft} = document.body;
    return {top: top + scrollTop,left: left + scrollLeft}
};
//获取元素类型
var elementType = obj => Object.prototype.toString.call(obj).replace(/^\[object (.+)\]$/, '$1').toLowerCase();
//console.log(elementType(document.getElementsByTagName("div")))//htmlcollection
//console.log(elementType(document.getElementsByTagName("div")[0]))//htmldivelement
//禁止复制粘贴
var forbidenCopy = () => {
    document.querySelector('html').oncopy = () => false;
    document.querySelector('html').onpaste = () => false;
}
//数组深拷贝
var arrayCopy = arr => arr.slice(0);
//清空数组
var arrayClear = arr => {
    arr.length = 0;
    return arr
}

/**
 * echarts自适应
 * @param echarts echarts对象
 * @param echatrs_id echarts的div的id
 */
function autoEcharts(echarts,echatrs_id){
    echarts.resize();
    $(window).on("resize",function () {
        var width=window.screen.width;//屏幕像素
        var height=window.screen.height;
        var heightChange = $("body").height();//窗口像素
        var widthChange = $("body").width();
        var scaleHeight = heightChange/height;
        var scaleWidth = widthChange/width;
        var $dom_id="#"+echatrs_id;
        $($dom_id).css("transform", "scale(" + scaleWidth + "," + scaleHeight + ")");//比例缩放
        $($dom_id).css("transform-origin","0 0");//按左上角缩放
    })
}
//随机颜色
function getRandomColor() {
    return (function(math, str, index) {
        console.log("m:"+math)
        console.log("s:"+str)
        console.log("c:"+index)
        return (index ? arguments.callee(math, str, index - 1) : '#') +
            str[math.floor(math.random() * 16)]
    })(Math, '0123456789abcdef', 5)
}


//对象:Object.hasOwnProperty("属性")  返回true标识对象有该属性，返回false表示对象没有该属性

//layui父页面带值给子页面
//    layer.open({
//                         type : 2,
//                         area : [ '900px', '600px' ],
//                         fixed : false, //不固定
//                         maxmin : true,
//                         title : '详细信息',
//                         content : 'findbyEmail_peple.jsp',
//                         success : function(layero, index) {
//                           var body = layer.getChildFrame('body', index); //得到子页面层的BODY
//                          //查找body旗下input框，设置框内默认值为：applicant
//                           body.find('#HiddenVal').val(applicant);
//                         }
//                     })
//
//
//鼠标移入移出mouseover mouseout
//$(dom).change()//主动触发dom元素的onchange事件
//str=str.split('')//将字符串转为数组.reverse()//反转.join('');//把数组中的所有元素放入一个字符串。
//append() - 在被选元素的结尾插入内容
// prepend() - 在被选元素的开头插入内容
// after() - 在被选元素之后插入内容
// before() - 在被选元素之前插入内容

////判断:当前元素是否是被筛选元素的子元素
// jQuery.fn.isChildOf = function(b){ return (this.parents(b).length > 0); };
// //判断:当前元素是否是被筛选元素的子元素或者本身
// jQuery.fn.isChildAndSelfOf = function(b){ return (this.closest(b).length > 0); }; $(document).click(function(event){ alert($(event.target).isChildAndSelfOf(".floatLayer")); });
// 可判断 当前点击元素是否为某元素本身或子元素，从而决定什么操作

//jQuery.parent(expr)，找父亲节点，可以传入expr进行过滤，比如$("span").parent()或者$("span").parent(".class")
// jQuery.parents(expr)，类似于jQuery.parents(expr),但是是查找所有祖先元素，不限于父元素
// jQuery.children(expr)，返回所有子节点，这个方法只会返回直接的孩子节点，不会返回所有的子孙节点
// jQuery.contents()，返回下面的所有内容，包括节点和文本。这个方法和children()的区别就在于，包括空白文本，也会被作为一个jQuery对象返回，children()则只会返回节点
// jQuery.prev()，返回上一个兄弟节点，不是所有的兄弟节点
// jQuery.prevAll()，返回所有之前的兄弟节点
// jQuery.next()，返回下一个兄弟节点，不是所有的兄弟节点
// jQuery.nextAll()，返回所有之后的兄弟节点
// jQuery.siblings()，返回兄弟姐妹节点，不分前后
// jQuery.find(expr)，跟jQuery.filter(expr)完全不一样：
//
// jQuery.filter()，是从初始的jQuery对象集合中筛选出一部分，而
//
// jQuery.find()，的返回结果，不会有初始集合中的内容，比如$("p").find("span")，是从<p>元素开始找<span>，等同于$("p span")

// Node.nodeName   //返回节点名称，只读
// Node.nodeType   //返回节点类型的常数值，只读
// Node.nodeValue  //返回Text或Comment节点的文本值，只读
// Node.textContent  //返回当前节点和它的所有后代节点的文本内容，可读写
// Node.baseURI    //返回当前网页的绝对路径
//
// Node.ownerDocument  //返回当前节点所在的顶层文档对象，即document
// Node.nextSibling  //返回紧跟在当前节点后面的第一个兄弟节点
// Node.previousSibling  //返回当前节点前面的、距离最近的一个兄弟节点
// Node.parentNode   //返回当前节点的父节点
// Node.parentElement  //返回当前节点的父Element节点
// Node.childNodes   //返回当前节点的所有子节点
// Node.firstChild  //返回当前节点的第一个子节点
// Node.lastChild   //返回当前节点的最后一个子节点
//
// //parentNode接口
// Node.children  //返回指定节点的所有Element子节点
// Node.firstElementChild  //返回当前节点的第一个Element子节点
// Node.lastElementChild   //返回当前节点的最后一个Element子节点
// Node.childElementCount  //返回当前节点所有Element子节点的数目。
//
// Node.appendChild(node)   //向节点添加最后一个子节点
// Node.hasChildNodes()   //返回布尔值，表示当前节点是否有子节点
// Node.cloneNode(true);  // 默认为false(克隆节点), true(克隆节点及其属性，以及后代)
// Node.insertBefore(newNode,oldNode)  // 在指定子节点之前插入新的子节点
// Node.removeChild(node)   //删除节点，在要删除节点的父节点上操作
// Node.replaceChild(newChild,oldChild)  //替换节点
// Node.contains(node)  //返回一个布尔值，表示参数节点是否为当前节点的后代节点。
// Node.compareDocumentPosition(node)   //返回一个7个比特位的二进制值，表示参数节点和当前节点的关系
// Node.isEqualNode(noe)  //返回布尔值，用于检查两个节点是否相等。所谓相等的节点，指的是两个节点的类型相同、属性相同、子节点相同。
// Node.normalize()   //用于清理当前节点内部的所有Text节点。它会去除空的文本节点，并且将毗邻的文本节点合并成一个。
//
// //ChildNode接口
// Node.remove()  //用于删除当前节点
// Node.before()  //
// Node.after()
// Node.replaceWith()
//
// document.doctype   //
// document.documentElement  //返回当前文档的根节点
// document.defaultView   //返回document对象所在的window对象
// document.body   //返回当前文档的<body>节点
// document.head   //返回当前文档的<head>节点
// document.activeElement  //返回当前文档中获得焦点的那个元素。
//
// //节点集合属性
// document.links  //返回当前文档的所有a元素
// document.forms  //返回页面中所有表单元素
// document.images  //返回页面中所有图片元素
// document.embeds  //返回网页中所有嵌入对象
// document.scripts  //返回当前文档的所有脚本
// document.styleSheets  //返回当前网页的所有样式表
//
// //文档信息属性
// document.documentURI  //表示当前文档的网址
// document.URL  //返回当前文档的网址
// document.domain  //返回当前文档的域名
// document.lastModified  //返回当前文档最后修改的时间戳
// document.location  //返回location对象，提供当前文档的URL信息
// document.referrer  //返回当前文档的访问来源
// document.title    //返回当前文档的标题
// document.characterSet属性返回渲染当前文档的字符集，比如UTF-8、ISO-8859-1。
// document.readyState  //返回当前文档的状态
// document.designMode  //控制当前文档是否可编辑，可读写
// document.compatMode  //返回浏览器处理文档的模式
// document.cookie   //用来操作Cookie
//
// document.open()   //用于新建并打开一个文档
// document.close()   //不安比open方法所新建的文档
// document.write()   //用于向当前文档写入内容
// document.writeIn()  //用于向当前文档写入内容，尾部添加换行符。
//
// document.querySelector(selectors)   //接受一个CSS选择器作为参数，返回第一个匹配该选择器的元素节点。
// document.querySelectorAll(selectors)  //接受一个CSS选择器作为参数，返回所有匹配该选择器的元素节点。
// document.getElementsByTagName(tagName)  //返回所有指定HTML标签的元素
// document.getElementsByClassName(className)   //返回包括了所有class名字符合指定条件的元素
// document.getElementsByName(name)   //用于选择拥有name属性的HTML元素（比如<form>、<radio>、<img>、<frame>、<embed>和<object>等）
// document.getElementById(id)   //返回匹配指定id属性的元素节点。
// document.elementFromPoint(x,y)  //返回位于页面指定位置最上层的Element子节点。
//
// document.createElement(tagName)   //用来生成HTML元素节点。
// document.createTextNode(text)   //用来生成文本节点
// document.createAttribute(name)  //生成一个新的属性对象节点，并返回它。
// document.createDocumentFragment()  //生成一个DocumentFragment对象
//
// document.createEvent(type)   //生成一个事件对象，该对象能被element.dispatchEvent()方法使用
// document.addEventListener(type,listener,capture)  //注册事件
// document.removeEventListener(type,listener,capture)  //注销事件
// document.dispatchEvent(event)  //触发事件
//
// document.hasFocus()   //返回一个布尔值，表示当前文档之中是否有元素被激活或获得焦点。
// document.adoptNode(externalNode)  //将某个节点，从其原来所在的文档移除，插入当前文档，并返回插入后的新节点。
// document.importNode(externalNode, deep)   //从外部文档拷贝指定节点，插入当前文档。
//
// Element.attributes  //返回当前元素节点的所有属性节点
// Element.id  //返回指定元素的id属性，可读写
// Element.tagName  //返回指定元素的大写标签名
// Element.innerHTML   //返回该元素包含的HTML代码，可读写
// Element.outerHTML  //返回指定元素节点的所有HTML代码，包括它自身和包含的的所有子元素，可读写
// Element.className  //返回当前元素的class属性，可读写
// Element.classList  //返回当前元素节点的所有class集合
// Element.dataset   //返回元素节点中所有的data-*属性。
//
// Element.clientHeight   //返回元素节点可见部分的高度
// Element.clientWidth   //返回元素节点可见部分的宽度
// Element.clientLeft   //返回元素节点左边框的宽度
// Element.clientTop   //返回元素节点顶部边框的宽度
// Element.scrollHeight  //返回元素节点的总高度
// Element.scrollWidth  //返回元素节点的总宽度
// Element.scrollLeft   //返回元素节点的水平滚动条向右滚动的像素数值,通过设置这个属性可以改变元素的滚动位置
// Element.scrollTop   //返回元素节点的垂直滚动向下滚动的像素数值
// Element.offsetHeight   //返回元素的垂直高度(包含border,padding)
// Element.offsetWidth    //返回元素的水平宽度(包含border,padding)
// Element.offsetLeft    //返回当前元素左上角相对于Element.offsetParent节点的垂直偏移
// Element.offsetTop   //返回水平位移
// Element.style  //返回元素节点的行内样式
//
// Element.children   //包括当前元素节点的所有子元素
// Element.childElementCount   //返回当前元素节点包含的子HTML元素节点的个数
// Element.firstElementChild  //返回当前节点的第一个Element子节点
// Element.lastElementChild   //返回当前节点的最后一个Element子节点
// Element.nextElementSibling  //返回当前元素节点的下一个兄弟HTML元素节点
// Element.previousElementSibling  //返回当前元素节点的前一个兄弟HTML节点
// Element.offsetParent   //返回当前元素节点的最靠近的、并且CSS的position属性不等于static的父元素。
//
// getBoundingClientRect()
// // getBoundingClientRect返回一个对象，包含top,left,right,bottom,width,height // width、height 元素自身宽高
// // top 元素上外边界距窗口最上面的距离
// // right 元素右外边界距窗口最上面的距离
// // bottom 元素下外边界距窗口最上面的距离
// // left 元素左外边界距窗口最上面的距离
// // width 元素自身宽(包含border,padding)
// // height 元素自身高(包含border,padding)
//
// getClientRects()   //返回当前元素在页面上形参的所有矩形。
//
// // 元素在页面上的偏移量
// var rect = el.getBoundingClientRect()
// return {
//   top: rect.top + document.body.scrollTop,
//   left: rect.left + document.body.scrollLeft
// }
//
// Element.getAttribute()：读取指定属性
// Element.setAttribute()：设置指定属性
// Element.hasAttribute()：返回一个布尔值，表示当前元素节点是否有指定的属性
// Element.removeAttribute()：移除指定属性
//
// Element.querySelector()
// Element.querySelectorAll()
// Element.getElementsByTagName()
// Element.getElementsByClassName()
//
// Element.addEventListener()：添加事件的回调函数
// Element.removeEventListener()：移除事件监听函数
// Element.dispatchEvent()：触发事件
//
// //ie8
// Element.attachEvent(oneventName,listener)
// Element.detachEvent(oneventName,listener)
//
// // event对象
// var event = window.event||event;
//
// // 事件的目标节点
// var target = event.target || event.srcElement;
//
// // 事件代理
// ul.addEventListener('click', function(event) {
//   if (event.target.tagName.toLowerCase() === 'li') {
//     console.log(event.target.innerHTML)
//   }
// });
//
// Element.scrollIntoView()   //滚动当前元素，进入浏览器的可见区域
//
// //解析HTML字符串，然后将生成的节点插入DOM树的指定位置。
// Element.insertAdjacentHTML(where, htmlString);
// Element.insertAdjacentHTML('beforeBegin', htmlString); // 在该元素前插入
// Element.insertAdjacentHTML('afterBegin', htmlString); // 在该元素第一个子元素前插入
// Element.insertAdjacentHTML('beforeEnd', htmlString); // 在该元素最后一个子元素后面插入
// Element.insertAdjacentHTML('afterEnd', htmlString); // 在该元素后插入
//
// Element.remove()  //用于将当前元素节点从DOM中移除
// Element.focus()   //用于将当前页面的焦点，转移到指定元素上
//
// //ie8以下
// Element.className  //获取元素节点的类名
// Element.className += ' ' + newClassName  //新增一个类名
//
// //判断是否有某个类名
// function hasClass(element,className){
//   return new RegExp(className,'gi').test(element.className);
// }
//
// //移除class
// function removeClass(element,className){
//   element.className = element.className.replace(new RegExp('(^|\\b)' + className.split(' ').join('|') + '(\\b|$)', 'gi'),'');
// }
//
// //ie10
// element.classList.add(className)  //新增
// element.classList.remove(className)  //删除
// element.classList.contains(className)  //是否包含
// element.classList.toggle(className)  //toggle class
//
// element.setAttribute('style','')
//
// element.style.backgroundColor = 'red'
//
// element.style.cssText //用来读写或删除整个style属性
//
// element.style.setProperty(propertyName,value)  //设置css属性
// element.style.getPropertyValue(property)  //获取css属性
// element.style.removeProperty(property)  //删除css属性
// 操作非内联样式
// //ie8
// element.currentStyle[attrName]
// //ie9+
// window.getComputedStyle(el,null)[attrName]
// window.getComputedStyle(el,null).getPropertyValue(attrName)
// //伪类
// window.getComputedStyle(el,':after')[attrName]
//
// arr
// a.valueof()   //返回数组本身
// a.toString()  //返回数组的字符串形式
// a.push(value,vlaue....)   //用于在数组的末端添加一个或多个元素，并返回添加新元素后的数组长度。
// pop()   //用于删除数组的最后一个元素，并返回该元素
// join()  //以参数作为分隔符，将所有数组成员组成一个字符串返回。如果不提供参数，默认用逗号分隔。
// concat()  //用于多个数组的合并。它将新数组的成员，添加到原数组的尾部，然后返回一个新数组，原数组不变。
// shift()  //用于删除数组的第一个元素，并返回该元素。
// unshift(value)  //用于在数组的第一个位置添加元素，并返回添加新元素后的数组长度。
// reverse()   //用于颠倒数组中元素的顺序，返回改变后的数组
// slice(start_index, upto_index);   //用于提取原数组的一部分，返回一个新数组，原数组不变。第一个参数为起始位置（从0开始），第二个参数为终止位置（但该位置的元素本身不包括在内）。如果省略第二个参数，则一直返回到原数组的最后一个成员。负数表示倒数第几个。
// splice(index, count_to_remove, addElement1, addElement2, ...);   //用于删除原数组的一部分成员，并可以在被删除的位置添加入新的数组成员，返回值是被删除的元素。第一个参数是删除的起始位置，第二个参数是被删除的元素个数。如果后面还有更多的参数，则表示这些就是要被插入数组的新元素。
// sort()   //对数组成员进行排序，默认是按照字典顺序排序。排序后，原数组将被改变。如果想让sort方法按照自定义方式排序，可以传入一个函数作为参数，表示按照自定义方法进行排序。该函数本身又接受两个参数，表示进行比较的两个元素。如果返回值大于0，表示第一个元素排在第二个元素后面；其他情况下，都是第一个元素排在第二个元素前面。
// map()   //对数组的所有成员依次调用一个函数，根据函数结果返回一个新数组。
// map(elem,index,arr)   //map方法接受一个函数作为参数。该函数调用时，map方法会将其传入三个参数，分别是当前成员、当前位置和数组本身。
// forEach()   //遍历数组的所有成员，执行某种操作,参数是一个函数。它接受三个参数，分别是当前位置的值、当前位置的编号和整个数组。
// filter()   //参数是一个函数，所有数组成员依次执行该函数，返回结果为true的成员组成一个新数组返回。该方法不会改变原数组。
// some()    //用来判断数组成员是否符合某种条件。接受一个函数作为参数，所有数组成员依次执行该函数，返回一个布尔值。该函数接受三个参数，依次是当前位置的成员、当前位置的序号和整个数组。只要有一个数组成员的返回值是true，则整个some方法的返回值就是true，否则false。
// every()   //用来判断数组成员是否符合某种条件。接受一个函数作为参数，所有数组成员依次执行该函数，返回一个布尔值。该函数接受三个参数，依次是当前位置的成员、当前位置的序号和整个数组。所有数组成员的返回值都是true，才返回true，否则false。
// reduce()   //依次处理数组的每个成员，最终累计为一个值。从左到右处理（从第一个成员到最后一个成员）
// reduceRight()  //依次处理数组的每个成员，最终累计为一个值。从右到左（从最后一个成员到第一个成员）
// indexOf(s)   //返回给定元素在数组中第一次出现的位置，如果没有出现则返回-1。可以接受第二个参数，表示搜索的开始位置
// lastIndexOf()  //返回给定元素在数组中最后一次出现的位置，如果没有出现则返回-1。

const fromNumber = (function () {
    const num = {
        '-': '([]+~+[])[+[]]',
        0: '+[]',
        1: '+!![]',
    }
    ~(function genNum(n) {
            if (n === 1) {
                return num[1]
            } else {
                return num[n] = genNum(n - 1) + num[1]
            }
        }
    )(9)
    return function (n) {
        if (num.hasOwnProperty(n)) {
            return num[n]
        } else {
            return num[n] = `+([]+${('' + n)
                .split('')
                .map(i => {
                    if (!num.hasOwnProperty(i)) {
                        throw new Error(`fromNumber not support: '${i}'`)
                    }
                    return `(${num[i]})`
                })
                .join('+')})`
        }
    }
})()
const char = {};
[
    '[]+{}',            // '[object Object]'
    '[]+![]',           // 'false'
    '[]+!![]',          // 'true'
    '[]+[][[]]',        // 'undefined'
    '[]+(+[]+[][[]])',  // 'NaN'
].forEach(s =>
    eval(s)
        .split('')
        .map((c, i) => {
            const _ = `(${s})[${fromNumber(i)}]`
            if (!char.hasOwnProperty(c)
                || char[c].length > _.length) {
                char[c] = _
            }
        })
)
for (let i = 0; i < 10; i++) {
    char[i] = `[]+(${fromNumber(i)})`
}

// Keep simple char
const simpleChar = {
    ...char,
}
function transform(code, map = char) {
    return code
        .split('')
        .map(c => {
            if (!char.hasOwnProperty(c)) {
                throw new Error(`fromString not support: '${c}'`)
            }
            return `(${char[c]})`
        })
        .join('+')
}
var fromString = (function () {
    // Generate base char map
    const char = {};
    [
        '[]+{}',            // '[object Object]'
        '[]+![]',           // 'false'
        '[]+!![]',          // 'true'
        '[]+[][[]]',        // 'undefined'
        '[]+(+[]+[][[]])',  // 'NaN'
    ].forEach(s =>
        eval(s)
            .split('')
            .map((c, i) => {
                const _ = `(${s})[${fromNumber(i)}]`
                if (!char.hasOwnProperty(c)
                    || char[c].length > _.length) {
                    char[c] = _
                }
            })
    )
    for (let i = 0; i < 10; i++) {
        char[i] = `[]+(${fromNumber(i)})`
    }

    // Keep simple char
    const simpleChar = {
        ...char,
    }

    // Transform char into magic string
    function transform(code, map = char) {
        return code
            .split('')
            .map(c => {
                if (!char.hasOwnProperty(c)) {
                    throw new Error(`fromString not support: '${c}'`)
                }
                return `(${char[c]})`
            })
            .join('+')
    }

    // Get character for `toString`
    // ''+''['constructor'] => 'function String() { [native code] }'
    const _functionString = `[]+([]+[])[${transform('constructor')}]`
    char.g = `(${_functionString})[${fromNumber(14)}]`
    char.v = `(${_functionString})[${fromNumber(25)}]`
    char.S = `(${_functionString})[${fromNumber(9)}]`
    char['('] = `(${_functionString})[${fromNumber(15)}]`
    char[')'] = `(${_functionString})[${fromNumber(16)}]`

    // Fill all lowercase char using `(Number).toString`
    for (let i = 10; i < 36; i++) {
        const c = i.toString(36)
        if (!char.hasOwnProperty(c)) {
            // i['toString'](36)
            char[c] = `(${fromNumber(i)})[${transform('toString')}](${fromNumber(36)})`
        }
    }

    // Fill other with `unescape`
    // There should be an other way because `unescape` is deprecated and will be remove in future
    for (let i = 0; i < 256; i++) {
        const hexStr = i.toString(16)
        const c = unescape(`%${hexStr}`)
        if (char.hasOwnProperty(c)) {
            continue
        }

        // []['sort']['constructor']('return unescape')()
        char[c] = `[][${transform('sort')}][${transform('constructor')}](${transform('return unescape')})()('%'+(${transform(hexStr)}))`
    }

    return function (str) {
        if (!str) {
            return ''
        }

        // Simple string
        if (str.split('').every(c => simpleChar[c])) {
            return transform(str, simpleChar)
        }

        // Complex string, use unescape
        const hexCodes = str
            .split('')
            .map(c => c.charCodeAt(0).toString(16))
            .map(hex => {
                switch (hex.length) {
                    case 1:
                        return transform(`0${hex}`)
                    case 2:
                        return transform(`${hex}`)
                    default:
                        return transform(`u${hex}`)
                }
            })
            .map(code => `('%'+${code})`)
            .join('+')
        return `[][${transform('sort')}][${transform('constructor')}](${transform('return unescape')})()(${hexCodes})`
    }
})();
console.log(fromString("js"))