package com.hoppinzq.service.exception;

import com.hoppinzq.service.bean.ApiResponse;
import com.hoppinzq.service.bean.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

/**
 * 捕获抛出的异常
 */
@RestControllerAdvice
public class ResultReturnExceptionHandler {

	protected static Logger log= LoggerFactory.getLogger(ResultReturnExceptionHandler.class);

	/** 捕捉shiro的异常 *//*
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(ShiroException.class)
	public Result handle401(ShiroException e) {
		log.error(e.getMessage(), e);
		return Result.error(ErrorEnum.UNAUTHORIZED);
	}

	*//** 捕捉UnauthorizedException *//*
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedException.class)
	public Result handle401() {
		return Result.error(ErrorEnum.UNAUTHORIZED);
	}*/

	/** 文件上传大小异常 */
	@ExceptionHandler(MultipartException.class)
	public ApiResponse handleMultipart(Throwable t) {
		log.error(t.getMessage(), t);
		return ApiResponse.error(ErrorEnum.UPLOAD_FILE_SIZE_MAX);
	}

	/** jackson转换Bean * */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ApiResponse handleJsonConv(Throwable t) {
		log.error(t.getMessage(), t);
		return ApiResponse.error(ErrorEnum.ZQ_GATEWAY_JSON_FORMAT_ERROR);
	}

	 /** 异常参数处理器 */
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse handleRRException(Throwable e) {
        //log.error(e.getMessage(), e);
        return ApiResponse.error(ErrorEnum.COMMON_ERROR.code, e.getMessage());
    }
    
	/** 自定义异常  */
	@ExceptionHandler(ResultReturnException.class)
	public ApiResponse handleRRException(ResultReturnException e) {
		log.error(exTraceBack(e), e);
		return ApiResponse.error(e.getCode(), e.getMsg());
	}

	@ExceptionHandler(Exception.class)
	public ApiResponse handleException(Exception e) {
		log.error(exTraceBack(e), e);
		return ApiResponse.error(500,e.getMessage());
	}

	public static String exTraceBack(Exception e) {
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] stackTrace = e.getStackTrace();
		for (int i = 0; i < stackTrace.length; i++) {
			sb.append("<---");
			sb.append(String.format("[%s * %s]  ", stackTrace[i].getClassName(), stackTrace[i].getMethodName()));
		}
		sb.append(e.getMessage());
		return sb.toString();
	}
}
