package cn.ltx.reggie.web.controller;

import cn.ltx.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.web.controller
 * @CLASS_NAME: CommonController
 * @Author: 21130
 * @CreateTime: 2022-08-25  11:19
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/common")
@SuppressWarnings("ALL")
public class CommonController {
    @Value("${img.path}")
    private String basePath;

    /**
     * 图片上传
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("/upload")
    public R<String> upload(MultipartFile file) throws Exception {
        //获取图片元名称
        String originalFilename = file.getOriginalFilename();
        //截取图片后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID生成32位图片名成并拼接后缀名
        String fileName = UUID.randomUUID() + suffix;
        //创建文件
        File dir = new File(basePath);
        //判断该文件目录是否存在
        if (!dir.exists()) {
            //不存在则创建
            dir.mkdirs();
        }
        //将图片保存到指定目录下
        file.transferTo(new File(basePath + fileName));
        //返回图片名称，用于前端请求下载图片
        return R.success(fileName);
    }

    /**
     * 图片下载
     * @param response
     * @param name
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse response, String name)/* throws Exception*/ {
        BufferedInputStream bis = null;
        ServletOutputStream ps = null;
        try {
            //使用字节包装输入流读取
            bis = new BufferedInputStream(new FileInputStream(basePath + name));
            ps = response.getOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = bis.read(buff))!=-1) {
                ps.write(buff, 0,len);
                ps.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(bis!=null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(ps!=null) {
                try {
                    ps.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
