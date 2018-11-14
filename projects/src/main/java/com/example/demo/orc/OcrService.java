package com.example.demo.orc;

import com.baidu.aip.util.Base64Util;
import com.example.demo.support.FileUtil;
import com.example.demo.support.HttpUtil;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;

/**
 * @author kejun
 * @date 2018/11/14 下午1:35
 */
@Service
public class OcrService {
    public static void main(String[] args) {
        // 通用识别url
        String otherHost = "https://aip.baidubce.com/rest/2.0/image-classify/v1/flower";
        // 本地图片路径
        String filePath = "/Users/juin/img/test.png";//#####本地文件路径#####
        try {
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
            String accessToken = AuthHandler.getAuth();//#####调用鉴权接口获取的token#####
            System.out.println("accessToken:"+accessToken);
            String result = HttpUtil.post(otherHost, accessToken, params);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
