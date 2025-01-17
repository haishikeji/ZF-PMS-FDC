package com.px.configuration;

import com.px.utils.PageOfficeUtils;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import javax.servlet.Servlet;

@Configuration
public class PageOfficeConfig {

    @Value("${posyspath}")
    private String poSysPath;
    @Bean
    public ServletRegistrationBean pageofficeRegistrationBean() {
        com.zhuozhengsoft.pageoffice.poserver.Server poserver
                = new com.zhuozhengsoft.pageoffice.poserver.Server();
        poserver.setSysPath(poSysPath);
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(poserver, "/pageoffice"); // 指定 URL 路径
        return servletRegistrationBean;
    }
   /* @Bean
    public ServletRegistrationBean pageofficeRegistrationBean()  {
        return new ServletRegistrationBean();
    }*/
    //        com.zhuozhengsoft.pageoffice.poserver.Server poserver
//                = new com.zhuozhengsoft.pageoffice.poserver.Server();
//        poserver.setSysPath(poSysPath);//设置PageOffice注册成功后,license.lic文件存放的目录
    //TODO 缺少PageOffice配置
    //
//        ServletRegistrationBean srb = new ServletRegistrationBean(poserver);
//        srb.addUrlMappings("/poserver.zz");
//        srb.addUrlMappings("/poclient");
//        srb.addUrlMappings("/sealsetup.exe");
//        return srb;
}
