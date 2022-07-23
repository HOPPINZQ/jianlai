$(function () {
    $.get(`${ip}/apiParams`, function (data) {
        let api = data.api;
        $.each(api, function (index_service, service) {
            let $zwagger_def = $("<div class=\"panel panel-default\"></div>");
            $zwagger_def.append(`<div class="panel-heading zwagger-service-title">${service.apiServiceTitle}<span class="zwagger-service-hide ">收起/展开</span></div><div class="panel-body zwagger-service-description"><p>${service.apiServiceDescription}</p></div>`)
            let $panelBody = $('<div class="panel-body zwagger-body"></div>');
            let $zwaggerService = $(`<div class="panel-group zwagger-service" id="zwagger-service-${index_service}" role="tablist" aria-multiselectable="true"></div>`);
            let serviceMethods = service.serviceMethods;
            $.each(serviceMethods, function (index_method, method) {
                let zwaggerMethod = $(`<div class="panel panel-default zwagger-method"></div>`);
                let $methodHead = $(`<div class="panel-heading zwagger-method-header" role="tab" id="zwagger-service-${index_service}-method-${index_method}-heading">
							<h4 class="panel-title">
								<a role="button" data-toggle="collapse" data-parent="#zwagger-service-${index_service}" href="#zwagger-service-${index_service}-method-${index_method}" aria-expanded="true" aria-controls="zwagger-service-${index_service}-method-${index_method}">
								${method.serviceMethod}<span class="method-title">${method.methodTitle}</span>
								</a>
							</h4>
						</div>`);
                zwaggerMethod.append($methodHead)
                let $method = $(`<div id="zwagger-service-${index_service}-method-${index_method}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="zwagger-service-${index_service}-method-${index_method}-heading"></div>`);
                let $methodPanel = $("<div class=\"panel-body\"></div>");
                $methodPanel.append(`<p>${method.methodDescription}</p>`);
                //表格1
                let methodParms = method.serviceMethodParams;
                let $zwaggerParamsTable = $(`<div class="col-lg-6 col-sm-12"></div>`)
                let $zwaggerParamsTable2 = $(`<div class="panel panel-default"></div>`);
                $zwaggerParamsTable2.append(`<div class="panel-heading">入参</div>`);
                $.each(methodParms, function (index_params, param) {
                    let $table = $(`<table class="table table-hover table-params"></table>`);
                    if (param.serviceMethodParamTypeParams.length == 0) {
                        $table.append(`<caption>${param.serviceMethodParamType}  ${param.serviceMethodParamName}</caption>`);
                    } else {
                        let $tbody = $("<tbody></tbody>");
                        $.each(param.serviceMethodParamTypeParams, function (index_method_param, method_param) {
                            $tbody.append(`<tr><th>${method_param.beanParamName}</th><th>${method_param.beanParamType}</th><th></th></tr>`)
                        })
                        $table.append(`<caption>${param.serviceMethodParamType}——${param.serviceMethodParamName}<span class="zwagger-param-hide">收起/展开</span></caption>
							<thead><tr><th>字段名</th><th>数据类型</th><th>注释</th></tr></thead>
							<tbody>${$tbody.html()}</tbody>`);
                    }
                    $zwaggerParamsTable2.append($table);
                })
                $zwaggerParamsTable.append($zwaggerParamsTable2)
                //表格1
                $methodPanel.append($zwaggerParamsTable);
                //表格2
                let $zwaggerReturnTable = $(`<div class="col-lg-6 col-sm-12">
                  <div class="panel panel-default">
                    <div class="panel-heading">返回值</div>
                    <table class="table table-hover">
                      <caption>${method.serviceMethodReturn.rawType == undefined ? method.serviceMethodReturn : method.serviceMethodReturn.rawType}<span class="zwagger-param-hide">收起/展开</span></caption>
                      ${method.serviceMethodReturn.rawType == undefined ? "" : "<caption>泛型：" + method.serviceMethodReturn.actualTypeArguments[0] + "</caption>"}
                      <thead>
                        <tr>
                          <th>字段名</th>
                          <th>数据类型</th>
                          <th>注释</th>
                        </tr>
                      </thead>
                      <tbody>
                      </tbody>
                    </table>
                  </div>
                </div>`);
                $methodPanel.append($zwaggerReturnTable);

                //额外信息
                let $zwaggerForm = $(`<div class="col-lg-12 col-sm-12 zwagger-form">
                  <div class="panel panel-default">
                    <div class="panel-heading">代理额外内容</div>
                    <div class="panel-body">
                      <p>这些是额外信息<span class="zwagger-extra-hide">收起/展开</span></p>
                    </div>
                    <div class="row form-group form-inline">
                      <div class="col-lg-2">
                        <span>是否缓存</span>
                      </div>
                      <div class="col-lg-2">
                        <div class="checkbox">
                          <label>
                            <input type="checkbox" ${method.isCache ? "checked" : ""}>是
                          </label>
                          <label>
                            <input type="checkbox" ${method.isCache ? "" : "checked"}>否
                          </label>
                        </div>
                      </div>
                      <div class="col-lg-2">
                        <span>缓存时间</span>
                      </div>
                      <div class="col-lg-2">
                        <span>${method.cacheTime}</span>
                      </div>
                      <div class="col-lg-2">
                        <span>是否存在缓存数据</span>
                      </div>
                      <div class="col-lg-2">
                        <div class="checkbox">
                          <label>
                            <input type="checkbox" ${method.cache ? "checked" : ""}>是
                          </label>
                          <label>
                            <input type="checkbox" ${method.cache ? "" : "checked"}>否
                          </label>
                        </div>
                      </div>
                    </div>
                    <div class="row form-group form-inline">
                      <div class="col-lg-2">
                        <span>是否限流</span>
                      </div>
                      <div class="col-lg-2">
                        <div class="checkbox">
                          <label>
                            <input type="checkbox" ${method.isLimit ? "checked" : ""}>是
                          </label>
                          <label>
                            <input type="checkbox" ${method.isLimit ? "" : "checked"}>否
                          </label>
                        </div>
                      </div>
                      <div class="col-lg-2">
                        <span>限流次数</span>
                      </div>
                      <div class="col-lg-2">
                        <span>${method.limitNumber}</span>
                      </div>
                      <div class="col-lg-2">
                        <span>是否加同步锁</span>
                      </div>
                      <div class="col-lg-2">
                        <div class="checkbox">
                          <label>
                            <input type="checkbox" ${method.isLock ? "checked" : ""}>是
                          </label>
                          <label>
                            <input type="checkbox" ${method.isLock ? "" : "checked"}>否
                          </label>
                        </div>
                      </div>
                    </div>
                    <div class="row form-group form-inline">
                      <div class="col-lg-3">
                        <span>接收请求类型</span>
                      </div>
                      <div class="col-lg-3">
                        <div class="checkbox">
                          <label>
                            <input type="checkbox" ${method.requestType == "GET" ? "checked" : ""}>仅GET请求
                          </label>
                        </div>
                      </div>
                      <div class="col-lg-3">
                        <div class="checkbox">
                          <label>
                            <input type="checkbox" ${method.requestType == "POST" ? "checked" : ""}>仅POST请求
                          </label>
                        </div>
                      </div>
                      <div class="col-lg-3">
                        <div class="checkbox">
                          <label>
                            <input type="checkbox" ${method.requestType == "ALL" ? "checked" : ""}>不限制
                          </label>
                        </div>
                      </div>
                    </div>
                    <div class="row form-group form-inline">
                      <div class="col-lg-2">
                        <span>调用权限</span>
                      </div>
                      <div class="col-lg-10">
                        <div class="col-lg-3">
                          <div class="checkbox">
                            <label>
                              <input type="checkbox" ${method.methodRight == "NO_RIGHT" ? "checked" : ""}>未登录权限
                            </label>
                          </div>
                        </div>
                        <div class="col-lg-3">
                          <div class="checkbox">
                            <label>
                              <input type="checkbox" ${method.methodRight == "LOGIN" ? "checked" : ""}>登录权限
                            </label>
                          </div>
                        </div>
                        <div class="col-lg-3">
                          <div class="checkbox">
                            <label>
                              <input type="checkbox" ${method.methodRight == "MEMBER" ? "checked" : ""}><span>会员权限</span>
                            </label>
                          </div>
                        </div>
                        <div class="col-lg-3">
                          <div class="checkbox">
                            <label>
                              <input type="checkbox" ${method.methodRight == "ADMIN" ? "checked" : ""}>超级管理员权限
                            </label>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="row form-group form-inline">
                      <div class="col-lg-3">
                        <span>是否幂等</span>
                      </div>
                      <div class="col-lg-3">
                        <div class="checkbox">
                          <label>
                            <input type="checkbox" ${method.tokenCheck ? "checked" : ""}>是
                          </label>
                          <label>
                            <input type="checkbox" ${method.tokenCheck ? "" : "checked"}>否
                          </label>
                        </div>
                      </div>
                      <div class="col-lg-3">
                        <span>是否封装返回值</span>
                      </div>
                      <div class="col-lg-3">
                        <div class="checkbox">
                          <label>
                            <input type="checkbox" ${method.methodReturn ? "checked" : ""}>是
                          </label>
                          <label>
                            <input type="checkbox" ${method.methodReturn ? "" : "checked"}>否
                          </label>
                        </div>
                      </div>
                    </div>
                    <div class="row form-group form-inline">
                      <div class="col-lg-2">
                        <span>超时机制</span>
                      </div>
                      <div class="col-lg-2">
                        <div class="checkbox">
                          <label>
                            <input type="checkbox" ${method.isTimeout ? "checked" : ""}>是
                          </label>
                          <label>
                            <input type="checkbox" ${method.isTimeout ? "" : "checked"}>否
                          </label>
                        </div>
                      </div>
                      <div class="col-lg-2">
                        <span>超时时间</span>
                      </div>
                      <div class="col-lg-2">
                        <span>${method.timeout}</span>
                      </div>
                  </div>
                </div>`);
                $methodPanel.append($zwaggerForm)
                $method.append($methodPanel);
                zwaggerMethod.append($method)
                $zwaggerService.append(zwaggerMethod);
            })
            $panelBody.append($zwaggerService);
            $zwagger_def.append($panelBody);
            $zwagger_def.appendTo($(".zwagger-api"));
        })
        $(".zwagger-param-hide").off("click").on("click", function () {
            $(this).parents("table").find("thead,tbody").each(function (index, element) {
                $(element).fadeToggle(500);
            })
        });
        $(".zwagger-extra-hide").off("click").on("click", function () {
            $(this).parents(".panel").find(".form-inline").each(function (index, element) {
                $(element).fadeToggle(500);
            })
        });
        $(".zwagger-service-hide").off("click").on("click", function () {
            $(this).parents(".panel").children(".zwagger-body").fadeToggle(500)
        });
        $(".checkbox").css("pointer-events", "none");
    })
})