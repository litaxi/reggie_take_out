package cn.ltx;

import cn.ltx.reggie.util.SendSmsUtils;
import cn.ltx.reggie.util.ValidateCodeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx
 * @CLASS_NAME: SendSmsTest
 * @Author: 21130
 * @CreateTime: 2022-08-27  19:34
 * @Description:
 * @Version: 1.0
 */
@SuppressWarnings("ALL")
@SpringBootTest
public class SendSmsTest {
    @Test
    public void sendSms() throws Exception {
        String phoneNumber = "13707607031";
        String message = ValidateCodeUtils.generateValidateCode(6).toString();
        SendSmsUtils.SendSms(phoneNumber,message);
    }
}
