package com.keke.sanshui.portal.controller.wx;

import com.keke.crypot.aes.WXBizMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.UUID;

@Controller
@Slf4j
public class WxController {

    @RequestMapping("/")
    public void checkServer(HttpServletRequest request, HttpServletResponse response) {
        try {
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echoStr = request.getParameter("echostr");
            response.getWriter().print(echoStr);
        } catch (Exception e) {

        }
    }
}
