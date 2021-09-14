package com.hoppinzq.service.vo;

/**
 * 系统错误提示
 */
public enum ErrorEnum {

  // 200-->Success!
  // 5000-->Fail！

  // common
  COMMON_ERROR("错误！", -1),
  COMMON_PARAMS_ERR("提交参数不合法", 5001),
  COMMON_PARAMS_ID_ERR("提交参数ID不合法", 5002),
  COMMON_EMPTY_CONDITION_RESULT("没有找到符合条件的数据", 5003),
  COMMON_PARAMS_NOT_EXIST("提交的字段不存在,或者参数格式错误", 5004),

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

  // NoSQL

  public final String msg;
  public final int code;

  ErrorEnum(String msg, int code) {
    this.msg = msg;
    this.code = code;
  }
}
