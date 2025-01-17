package com.px.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.px.utils.OnlyOfficeFileUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/8/18
 * created with IntelliJ IDEA.
 */
@RestController
@RequestMapping("onlyoffice")
public class OnlyOfficeCallbackController {



    @PostMapping("/callback")
    public void handleCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 处理回调请求
        callBack(request,response);
    }

    /**
     * 处理在线编辑文档的逻辑
     * @param request
     * @param response
     * @throws IOException
     */
    private void callBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = null;
        JSONObject jsonObj = null;
        String originPath = request.getParameter("originPath");
        System.out.println("===originPath:" + originPath);
        System.out.println("===saveeditedfile------------");

        try {
            writer = response.getWriter();
            Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A");
            String body = scanner.hasNext() ? scanner.next() : "";
            jsonObj = JSON.parseObject(body);
            System.out.println(jsonObj);
            System.out.println("===saveeditedfile:" + jsonObj.get("status"));
                /*
                    0 - no document with the key identifier could be found,
                    1 - document is being edited,
                    2 - document is ready for saving,
                    3 - document saving error has occurred,
                    4 - document is closed with no changes,
                    6 - document is being edited, but the current document state is saved,
                    7 - error has occurred while force saving the document.
                 * */
            if ((int) jsonObj.get("status") == 2) {
                String key = (String) jsonObj.get("key");
                //通过 key获取到文件在服务器上原始保存路经
                OnlyOfficeFileUtil.callBackSaveDocument(jsonObj,originPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
         * status = 1，我们给onlyoffice的服务返回{"error":"0"}的信息，这样onlyoffice会认为回调接口是没问题的，这样就可以在线编辑文档了，否则的话会弹出窗口说明
         * 在线编辑还没有关闭，前端有人下载文档时，强制保存最新内容  当status 是6时说明有人在编辑时下载文档
         * */
        System.out.println(jsonObj.get("status"));
        if ((int) jsonObj.get("status") == 6) {
            String key = (String) jsonObj.get("key");
            //通过 key获取到文件在服务器上原始保存路经
           OnlyOfficeFileUtil.callBackSaveDocument(jsonObj,originPath);
            writer.write("{\"error\":1}");
        } else {
            writer.write("{\"error\":0}");
        }
    }


}
