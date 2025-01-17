package com.px.enums;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2022/11/30
 * created with IntelliJ IDEA.
 */
public enum SexEnum implements CodeMsgEnumInterface<String,String>{

    MAN("男"),
    WOMAN("女")
    ;

    SexEnum(String sex) {
        this.sex = sex;
    }

    private String sex;

    public String getSex() {
        return sex;
    }

    @Override
    public String getCode() {
        return sex;
    }

    @Override
    public String getMsg() {
        return sex;
    }
}
