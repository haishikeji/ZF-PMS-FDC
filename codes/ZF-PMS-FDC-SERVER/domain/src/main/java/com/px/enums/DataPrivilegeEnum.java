package com.px.enums;

/**
 * 是否管理员
 */
public enum DataPrivilegeEnum implements CodeMsgEnumInterface<String,String> {
    YES("是",null),
    NO("否",null),
    ;

    private String type;
    private String msg;

    DataPrivilegeEnum(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return type;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
