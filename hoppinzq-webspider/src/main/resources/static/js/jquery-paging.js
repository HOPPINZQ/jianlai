;(function($){
	var num = 0;
	var index = 0;
	var lt=0;
	var gt=0;
	var prev=[1];
	var arrHas=[]
	var numClick=0;
	var This;
	var defaults={
		PageNum:6,
		PageBtn:5, 
		pageMax:false,
		pageMaxHideShow:false,
		pageDownUpHide:false, 
		pageInput:false, 
		pagingBtnHide:false, 
		pagingBtnPaging:true,
		pagingDisplay:"flex",
	}
	$.fn.extend({
		"paging":function(options){
			var opts=$.extend({},defaults,options);
			This=$(this).children();
			This.addClass("paging_box")
			This.hide();
			first();
			var numLI=Math.ceil((This.length)/opts.PageNum);
			for(var i=0;i<numLI;i++){
				has(".paging_btn span:eq(0)")
				num++
				$(".paging_btn").append("<span>" + num + "</span>"); 
			}
			$(".paging_btn span:eq("+ (opts.PageBtn-1) +")").nextAll().hide()
			$(".paging_btn span").click(function(){
				index = ($(this).index());
				if(index==0){
					first();
				}else{
					gt=parseInt(index*opts.PageNum);
					lt=gt+opts.PageNum;
					main(lt,(gt-1))
				}
				has(this)
				maxHide(index)
			})
			$(".paging_up,.paging_down").click(function(){
				index = ($(this).index());
				var btnActive=$(".paging_btn span[class^=paging_active]");
				if(index==1){
					if(btnActive.index()==0){
						return false;
					}else{
						var prevLen=0;
						$.each(prev,function(i,v){
							prevLen=i
						})
						if(prev[prevLen]==numLI){
							lt=opts.PageNum*numLI,gt=((opts.PageNum*numLI)-opts.PageNum);
						}
						var arrHasLen=0
						$.each(arrHas,function(i,v){
							arrHasLen=i
						})
						if(arrHas[arrHasLen]==$(".paging_content .paging_textBox button").html()){
							if(opts.pageInput==true){
								main((lt-=opts.PageNum),((gt-=opts.PageNum)))
							}
							main((lt-=opts.PageNum),((gt-=opts.PageNum)-1))
						}else{
							main((lt-=opts.PageNum),((gt-=opts.PageNum)-1))
							console.log(222)
						}
						has(btnActive.prev())
						if(btnActive.index()==1){
							first()
						}
					}
					prevPaging(btnActive.index()-1)
				}else if(index==3){
					if(btnActive.index()==(numLI-1)){
						return false;
					}else{
						if(btnActive.index()==0){
							lt=opts.PageNum,gt=lt-opts.PageNum;
						}
						has(btnActive.next())
						main((lt+=opts.PageNum),((gt+=opts.PageNum)-1))
					}
					nextPaging(btnActive.index()+1)
				}
			})
			$(".paging_first,.paging_last").click(function(){
				index = ($(this).index());
				if(index==0){
					first()
					maxHide(1)
				}else if(index==4){
					main(opts.PageNum*numLI,((opts.PageNum*numLI)-opts.PageNum)-1);
					has(".paging_btn span:eq("+ (numLI-1) +")")
					prev.push(numLI)
					nextPaging(numLI)
				}
			})
			function nextPaging(spanHtml){
				if(opts.pagingBtnPaging==true){
					if(spanHtml>numLI-opts.PageBtn+1){
						$(".paging_btn span").hide();
						$(".paging_btn span:eq("+ (numLI-opts.PageBtn-1) +")").nextAll().show()
					}else{
						if(spanHtml==1){
							return;
						}else{
							$(".paging_btn span:eq("+ (spanHtml+(opts.PageBtn-2)) +")").prevAll().show();
							$(".paging_btn span:lt("+ (spanHtml-(opts.PageBtn-3)) +")").hide();
							prev.push(spanHtml);
						}
					}
				}
			}
			function prevPaging(spanHtml){
					if(opts.pagingBtnPaging==true){
						if(spanHtml<3){
						$(".paging_btn span").hide();
						$(".paging_btn span:lt("+ (opts.PageBtn) +")").show();
					}else{
						$(".paging_btn span:eq("+ (spanHtml-(opts.PageBtn-2)) +")").nextAll().show();
						$(".paging_btn span:gt("+ (spanHtml+(opts.PageBtn-3)) +")").hide();
						prev.push(spanHtml);
					}
				}
			}
			function maxHide(btnIndex){
				numClick++
					prev.push(btnIndex);
					var prevLen=0;
					$.each(prev,function(i,v){
						prevLen=i
					})
					if(prev[prevLen]>prev[prevLen-1] || (btnIndex==3 && numClick==1)){
						nextPaging(btnIndex)
					}else{
						prevPaging(btnIndex)
					}
			}
			if(opts.pagingBtnHide==true){
				$(".paging_btn span").addClass("paging_none");
				$(".paging_btn span:eq(0)").removeClass("paging_none")
			}
			if(opts.pageInput==true){
				$(".paging_content").append("<div class='paging_textBox'>第<input type='text'/>页<button>前往</button></div>");
				$(".paging_textBox button").click(function(){
					var inpVal=+$(this).siblings("input").val();
					if(numLI<(inpVal) || (inpVal)<=0){
						alert("查无此页")
					}else{
						lt=opts.PageNum*inpVal,gt=((lt-opts.PageNum)-1);
						main(lt,gt);
						if(inpVal==1){
							first()
						}
						has(".paging_btn span:eq("+ (inpVal-1) +")");
						maxHide(inpVal-1);
						arrHas.push($(".paging_content .paging_textBox button").html())
					}
					$(this).siblings().val("")
				})
			}
			function first(){
				This.hide()
				$(".paging_box:lt("+ opts.PageNum +")").css("display",opts.pagingDisplay);
				has(".paging_btn span:eq(0)")
			}
			function main(mainNumLt, mainNumGt){
				This.hide()
				$(".paging_box:lt(" + mainNumLt + "):gt(" + mainNumGt + ")").css("display",opts.pagingDisplay)
			}
			if(opts.pageMax==true){
				$(".paging_content").append("<p class='paging_max'>共"+ numLI +"页</p>")
			}
			function has(This){
				var BtnIdex=$(This).index();
				$(This).addClass("paging_active").siblings().removeClass("paging_active");
				if(opts.pagingBtnHide==true){
					$(This).removeClass("paging_none").siblings().addClass("paging_none")
				}
				if(opts.pageMaxHideShow==true){
					if(BtnIdex==(numLI-1)){
						$(".paging_max").addClass("paging_none");
					}else{
						$(".paging_btn .paging_max").removeClass("paging_none")
					}
				}
				if(opts.pageDownUpHide==true){
					if(BtnIdex==0){
						$(".paging_up").css({position: "absolute",opacity: 0,zIndex:"-1000"});
					}else{
						$(".paging_up").css({position: "static",opacity: 1,zIndex:"10"});
					}
					if(BtnIdex==(numLI-1)){
						$(".paging_down").css({position: "absolute",opacity: 0,zIndex:"-1000"});
					}else{
						$(".paging_down").css({position: "static",opacity: 1,zIndex:"100"});
					}
				}
			}
			has()
			if(opts.pageDownUpHide==true){
				$(".paging_up").css({position: "absolute",opacity: 0,zIndex:"-1000"});
			}
		}
	})
})(jQuery)
