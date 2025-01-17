package com.px.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 类说明：证件
 *
 * @author: wucl
 * @since: 2024/3/14
 * created with IntelliJ IDEA.
 */
public enum CertificateEnum {

    HOUSE_CERTIFICATE("HOUSE_CERTIFICATE","房产证",new HashMap<Integer,String>(){{
        put(1,"classpath:docs/personal/house_statement.docx");
        put(2,"classpath:docs/personal/house_technic_report.docx");
        put(3,"classpath:docs/personal/house_final_report.docx");
    }}),
    LAND_CERTIFICATE("LAND_CERTIFICATE","国土证",new HashMap<Integer,String>(){{
        put(1,"classpath:docs/personal/land_statement.docx");
        put(2,"classpath:docs/personal/land_technic_report.docx");
        put(3,"classpath:docs/personal/land_final_report.docx");
    }}),
    IMMOVABLE_CERTIFICATE("IMMOVABLE_CERTIFICATE","不动产权证",new HashMap<Integer,String>(){{
        put(1,"classpath:docs/personal/immovable_statement.docx");
        put(2,"classpath:docs/personal/immovable_technic_report.docx");
        put(3,"classpath:docs/personal/immovable_final_report.docx");
    }}),
    LEASE_CONTRACT("LEASE_CONTRACT","租赁合同",new HashMap<>()),
    IDENTITY_CARD("IDENTITY_CARD","身份证",new HashMap<>()),
    ;


    CertificateEnum(String code, String name,Map<Integer,String> templatePath) {
        this.code = code;
        this.name = name;
        this.templatePath = templatePath;
    }

    private String code;

    private String name;

    private Map<Integer,String> templatePath;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, String> getTemplatePath() {
        return templatePath;
    }
}
