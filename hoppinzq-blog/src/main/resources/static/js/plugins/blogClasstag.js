function Tag(inputId){
	var obj = new Object();
	if(inputId==null||inputId==""){
		alert("初始化失败，请检查参数！");
		return;
	}
	obj.inputId = inputId;
	//初始化
	obj = (function(obj){
		obj.tagValue=[];
		obj.isDisable = false;
		return obj;
	})(obj);
	
	//初始化界面
	obj.initView=function(){
		let inputObj = $("#"+this.inputId);
		let inputId = this.inputId;
		let inputClass=inputObj.attr("class");
		inputObj.css("display","none");
		let $tagsContaineDiv=$(`<div class="tagsContaine" id="${inputId}_tagcontaine"></div>`);
		$tagsContaineDiv.append(`<div class="tagList ${inputClass}"></div>`);
		$input=$("<input type='text' class='tagInput'/>");
		$input.appendTo($tagsContaineDiv);
		if(inputClass=="active-tag"){
			$btn=$("<button class=\"btn btn-sm btn-dark me-1 mt-3 mt-sm-0\">添加</button>").off("click").on("click",function (){
				var inputValue = $input.val();
				if(inputValue!=""){
					tagTake.setInputValue(inputId,[{
						label:inputValue,
						value:""
					}]);
				}
				$input.val("");
			});
			$btn.appendTo($tagsContaineDiv);
		}

		inputObj.after($tagsContaineDiv);
		var tagInput = $("#"+inputId+"_tagcontaine .tagInput");
		if(!this.isDisable){
			$("#"+inputId+"_tagcontaine").attr("ds","1");
			tagInput.keydown(function(event){
				if(event.keyCode==13){
			         var inputValue = $(this).val();
			         tagTake.setInputValue(inputId,[{
						 label:inputValue,
						 value:""
					 }]);
			         $(this).val("");
			    }
			});
		}else{
			$("#"+inputId+"_tagcontaine").attr("ds","0");
			tagInput.remove();
		}
		if(this.tagValue!=null&&this.tagValue.length!=0){
			tagTake.setInputValue(inputId,this.tagValue);
			if(this.isDisable){
				$("#"+inputId+"_tagcontaine .tagList .tagItem .delete").remove();
			}
		}
	}
	obj.disableFun=function(){
		if(this.isDisable){
			return;
		}
		var inputId = this.inputId;
		var tagInput = $("#"+inputId+"_tagcontaine .tagInput");
		tagInput.remove();
		this.isDisable = true;
		$("#"+inputId+"_tagcontaine").attr("ds","0");
		$("#"+inputId+"_tagcontaine .tagList .tagItem .delete").remove();
		tagTake.initTagEvent(inputId);
		
	}
	obj.unDisableFun = function(){
		if(!this.isDisable){
			return;
		}
		var inputId = this.inputId;
		var tagContaine = $("#"+inputId+"_tagcontaine");
		tagContaine.append('<input type="text" class="tagInput"/>');
		this.isDisable = false;
		$("#"+inputId+"_tagcontaine").attr("ds","1");
		var tagInput = $("#"+inputId+"_tagcontaine .tagInput");
		tagInput.keydown(function(event){
				if(event.keyCode==13){
			         var inputValue = $(this).val();
			         tagTake.setInputValue(inputId,[{
						 label:inputValue,
						 value:""
					 }]);
			         $(this).val("");
			    }
		});
		$("#"+inputId+"_tagcontaine .tagList .tagItem").append('<div class="delete"></div>');
		tagTake.initTagEvent(inputId);
		
	}
	
	return obj;
}

var tagTake ={
	"setInputValue":function(inputId,inputValue){
		if(inputValue==null||inputValue.length==0){
			return;
		}
		var tagListContaine = $("#"+inputId+"_tagcontaine .tagList");
		// inputValue = inputValue.replace(/，/g,",");
		// var inputValueArray = inputValue.split(",");
		for(var i=0;i<inputValue.length;i++){
			var valueItem =inputValue[i];
			if(valueItem!=null){
				//label value
				var appendListItem = tagTake.getTagItemModel(valueItem);
				tagListContaine.append(appendListItem);
			}
		}
		tagTake.resetTagValue(inputId);
		tagTake.initTagEvent(inputId);
	},
	"initTagEvent":function(inputId){
		$("#"+inputId+"_tagcontaine .tagList .tagItem .delete").off();
		$("#"+inputId+"_tagcontaine .tagList .tagItem").off();
		var ds =  $("#"+inputId+"_tagcontaine").attr("ds");
		if(ds=="0"){
			return;
		}
		$("#"+inputId+"_tagcontaine .tagList .tagItem .delete").mousedown(function(){
			if($(this).parent().data("id")==""){
				$(this).parent().remove();
				tagTake.resetTagValue(inputId);
			}else{
				alert("已有的小类不允许操作！")
			}
		});
		
		$("#"+inputId+"_tagcontaine .tagList .tagItem").dblclick(function(){
			var tagItemObj = $(this);
			if(tagItemObj.data("id")==""){
				$(this).css("display","none");
				var updateInputObj = $("<input type='text' class='updateInput' value='"+tagItemObj.find("span").html()+"'>");
				updateInputObj.insertAfter(this);
				updateInputObj.focus();
				updateInputObj.blur(function(){
					var inputValue = $(this).val();
					if(inputValue!=null&&inputValue!=""){
						tagItemObj.find("span").html(inputValue);
						tagItemObj.css("display","block");
					}else{
						tagItemObj.remove();
					}
					updateInputObj.remove();
					tagTake.resetTagValue(inputId);

				});
				updateInputObj.keydown(function(event){
					if(event.keyCode==13){
				        var inputValue = $(this).val();
						if(inputValue!=null&&inputValue!=""){
							tagItemObj.find("span").html(inputValue);
							tagItemObj.css("display","block");
						}else{
							tagItemObj.remove();
						}
						updateInputObj.remove();
						tagTake.resetTagValue(inputId);
				    }
				});
			}else{
				alert("已有的小类不允许操作！");
				return;
			}
		});
	},
	"resetTagValue":function(inputId){
		var tags = $("#"+inputId+"_tagcontaine .tagList .tagItem");
		var tagsStr="";
		for(var i=0;i<tags.length;i++){
			tagsStr+=tags.eq(i).find("span").html()+",";
		}
		tagsStr = tagsStr.substr(0,tagsStr.length-1);
		$("#"+inputId).val(tagsStr);
	},
	"getTagItemModel":function(valueItem){
		return `<div class="tagItem" data-id="${valueItem.value}"><span>${valueItem.label}</span><div class="delete"></div></div>`;
	}
}

