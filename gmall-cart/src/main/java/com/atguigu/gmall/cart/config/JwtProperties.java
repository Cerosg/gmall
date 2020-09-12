package com.atguigu.gmall.cart.config;

import com.atguigu.gmall.common.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月10日 18时26分
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtProperties {
    private PublicKey publicKey;

    private String pubKeyPath;// 公钥路径
    private String cookieName;// cookie名称
    // userKey名称：当用户以游客身份浏览页面并添加商品到购物车时，该字段作为Map<userKey,Map<skuId,skuJson>>
    private String userKeyName;

    @PostConstruct
    public void init() {
        try {
            // 把公私钥对应文件的内容读取出来赋值为对应的对象，方便将来使用
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成公钥和私钥失败，失败原因：" + e.getMessage());
        }
    }

}
