package com.px.enums;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/5/31
 * created with IntelliJ IDEA.
 */
public enum BusinessResourceEnum implements CodeMsgEnumInterface<String,String>{

    ITEM("ITEM","项目"),

    CUSTOMER("CUSTOMER","客户"),

    BUSINESS_OPPORTUNITY("BUSINESS_OPPORTUNITY","商机")

    ;

    BusinessResourceEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;

    private String name;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return name;
    }
}
