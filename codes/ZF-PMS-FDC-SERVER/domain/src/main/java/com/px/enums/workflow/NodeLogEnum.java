package com.px.enums.workflow;

import com.px.enums.CodeMsgEnumInterface;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/10/17
 * created with IntelliJ IDEA.
 */
public enum NodeLogEnum implements CodeMsgEnumInterface<String,String> {

    PASS("通过","PASS"),
    REVERSE("退回","REVERSE"),
    RESTART("重置","RESTART"),
    TERMINATE("终止","TERMINATE"),
    SKIP("跳过","SKIP")
    ;

    private String name;

    private String code;
    ;

    NodeLogEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return name;
    }
}
