package com.px.dto.history;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class HisMajorProduction {

    private String id;
    private String tId;
    private String pId;
    private String type;
    private String ownerName;
    private String ownerNo ;
    private String locations ;
    private BigDecimal useArea;
    private BigDecimal lastYear;
    private BigDecimal plotRate;
    private String landUsage;
    private String useType;
    private String masterName;
    private String surveyName;
    private String description;
    private BigDecimal price;
    private BigDecimal approvedPrice;
    private String measure;
    private LocalDate surveyDate;
    private Integer printQuantity;
    private String houseName;
    private String landOwnerName;
    private String landNo;
    private String buildYear;
    private BigDecimal buildArea;
    private BigDecimal landArea;
    private String planUsage;
    private String realUsage;
    private String surveyAddress;
    private String code;



    private String pType;
    private String reportNo;
    private String signEmpNames;
    private String name;
    private String principal;
    private String principalAddress;
    private String principalMobile;
    private String propertyName;
    private Date priceDate;
    private BigDecimal area;
    private BigDecimal money;
    private BigDecimal xPrice;
    private String specDesc;
    private Date archiveTime;
    private BigDecimal standardPrice;
    private Date confirmTime;
    private Date yjsInTime;
    private Date yjsOutTime;
    private Date bgInTime;
    private Date bgOutTime;
    private String repo;
    private Boolean deliveryState;

}
