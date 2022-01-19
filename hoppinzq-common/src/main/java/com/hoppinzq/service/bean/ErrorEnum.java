package com.hoppinzq.service.bean;

/**
 * 系统错误提示
 * 待补充
 */
public enum ErrorEnum {

  //公共的报错
  COMMON_ERROR("错误！", 500),
  COMMON_USER_TOKEN_OUT_DATE("该用户信息已过期",403),
  COMMON_REQUEST_ERROR("请求类型不一致",405),
  COMMON_DATE_MUST_TIMESTAMP("日期必须是长整型的时间戳",501),
  COMMON_DATE_TARGET_MUST_STRING("转换目标类型为字符串",502),

  //zq网关抛出的异常
  ZQ_GATEWAY_FILE_LOAD_MUST_POST("调用失败:文件上传必须是POST请求", 5001),
  ZQ_GATEWAY_CANT_ENCODE("调用失败：无法解密", 5002),
  ZQ_GATEWAY_ENCODE_ERROR_FORMAT("调用失败：加密的格式有误", 5003),
  ZQ_GATEWAY_METHOD_NOT_FOUND("调用失败：参数'method'为空", 5004),
  ZQ_GATEWAY_PARAMS_NOT_FOUND("调用失败：参数'params'为空", 5005),
  ZQ_GATEWAY_API_NOT_FOUND("调用失败：指定API不存在",5006),
  ZQ_GATEWAY_TOKEN_NOT_FOUND("调用失败：参数'token'为空", 5007),
  ZQ_GATEWAY_REQUEST_REPEAT("重复的请求，请使用新的token", 5008),
  ZQ_GATEWAY_JSON_FORMAT_ERROR("调用失败：json字符串格式异常，请检查params参数", 5009),
  ZQ_GATEWAY_API_METHOD_PARAM_NOT_FOUND("调用失败：接口不存在指定的参数", 5010),
  ZQ_GATEWAY_API_METHOD_ERROR_DATA("调用失败：指定参数格式错误或值错误", 5011),

  //api
  API_NOT_FIND_INFERCE("调用失败：接口不存在",5901),
  // sql
  SQL_ERROR("mysql通用错误", 5100),
  SQL_INSERT_FAIL("增加失败", 5101),
  SQL_DELETE_FAIL("删除失败", 5102),
  SQL_UPDATE_FAIL("修改失败", 5103),
  SQL_RECORD_EXIST("添加重复记录", 5104),
  SQL_ID_NOT_EXIST("主键ID不能为空", 5105),
  SQL_VERSION_NOT_EXIST("数据版本version不能为空", 5105),

  // io
  FILE_IO_ERROR("io通用错误", 5200),
  FILE_NOT_EXIST("文件没找到，请联系我", 5201),
  FILE_DATA_NULL("文档中不不存在有效的数据", 5202),
  FILE_DATA_ERR("文档中的数据格式错误", 5203),

  // form
  INVALID_PASSWORD("密码格式错误", 5300),
  INVALID_EMAIL("邮件格式错误", 5301),
  INVALID_NAME("账号格式错误", 5302),
  INVALID_PARAMS("填写字段不合法", 5303),


  // shiro-login
  NO_LOGIN("用户未登录", 401),
  UNAUTHORIZED("权限不足", 7001),
  ADMIN_ONLY("只有管理员账号可以调用这个接口", 5402),
  NO_PERSSIOM("没有权限请求", 5403),
  WRONG_ACCOUNT_OR_PSW("账号或密码错误", 5404),
  WRONG_ACCOUNT_PSW("账号密码错误", 5405),
  WRONG_ACCOUNT_WRONG("用户没有权限（令牌、用户名、密码错误）", 401),

  // uploading
  UPLOAD_FILE_TYPE_ERROR("上传文件格式错误", 5500),
  UPLOAD_FILE_UPLOADING("uploading", 5501),
  UPLOAD_FILE_NOT_EXIST("文件不存在", 5502),
  UPLOAD_FILE_SIZE_MAX("上传的文件大小超出限制", 5503),

  // es
  ES_BIG_PAGE_SEARCH("单页查询数据不能超过10000!", 9000);

  public String msg;
  public int code;

  ErrorEnum(String msg, int code) {
    this.msg = msg;
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public int getCode() {
    return code;
  }

  public static ErrorEnum errorAddMsg(ErrorEnum errorEnum,String msg){
    errorEnum.msg=errorEnum.getMsg()+msg;
    return errorEnum;
  }

  public static ErrorEnum errorChangeMsg(ErrorEnum errorEnum,String msg){
    errorEnum.msg=msg;
    return errorEnum;
  }


  public static ErrorEnum stateOf(int index)
  {
    for (ErrorEnum state : values())
    {
      if (state.getCode()==index)
      {
        return state;
      }
    }
    return null;
  }
}
