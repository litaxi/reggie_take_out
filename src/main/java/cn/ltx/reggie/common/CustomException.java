package cn.ltx.reggie.common;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.common
 * @CLASS_NAME: CustomException
 * @Author: 21130
 * @CreateTime: 2022-08-24  16:44
 * @Description:
 * @Version: 1.0
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
