package com.px.report.html;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/9/15
 * created with IntelliJ IDEA.
 */
public class TableHTML {

    public static String TABLE = "<table  " +
            " style= \"width:5.77in;border-collapse:collapse; border-spacing:\"0\"; empty-cells:show ;border=\"0\"; cellspacing=\"0\"; cellpadding=\"0\" \"> "+
            "<caption style=\" border-bottom-style:solid; border-bottom-color:#FFFFFF; " +
            "border-top-style:solid;border-top-color:#FFFFFF;" +
            "border-left-style:solid;border-left-color:#FFFFFF;" +
            "border-right-style:solid;border-right-color:#FFFFFF;text-align: left; vertical-align: top;\">" +
            "<span style=\"font-size:12px;\">表tableNumber</span>" +
            "</caption>" +
            "<tbody class=\"tbody\">" +
            "<tr class=\"tableHead\">" +
            "</tr>" +
            "</tbody>" +
            "</table>";

    public static String TR = "<tr>" +
            "                   </tr>";
    public static String TD_HEAD = "<th style=\"font-size:14px;border-top-style:solid; border-top-color:#000000; " +
            "border-left-style:solid; border-left-color:#000000;border-bottom-style:solid; border-bottom-color:#000000;border-right-style:solid; " +
            "border-right-color:#000000;background-color:#d7d7d7;padding-left:0.0035in; padding-right:0.0035in; padding-top:0in; padding-bottom:0in;" +
            "vertical-align:middle; width:auto \" >" +
                    "<p style=\"text-align: center; text-indent: 0em; line-height: normal; \" ><span style=\"text-align: -webkit-center; text-wrap: wrap;\">" +
                    "content" +
                    "</span></p>" +
                    "</th>";

    public static String TD = "<td valign=\"middle\" style=\"font-size:14px;border-top-style:solid; border-top-color:#000000; " +
            "border-left-style:solid; border-left-color:#000000;border-bottom-style:solid; border-bottom-color:#000000;" +
            "border-right-style:solid; border-right-color:#000000;background-color:transparent; padding-left:0.0035in; padding-right:0.0035in; padding-top:0in; padding-bottom:0in; " +
            "vertical-align:middle;  width:auto \" >" +
                    "<p style=\"text-align: center; text-indent: 0em; line-height: normal; \" ><span style=\"text-align: -webkit-center; text-wrap: wrap;\">" +
                    "content" +
                    "</span></p>" +
                    "</td>";

    public static final String TABLE_STYLE = "width:5.77in;border-collapse:collapse; border-spacing:0; empty-cells:show;";

    public static final String TD_STYLE = "font-size:14px;border-top-style:solid; border-top-color:#000000; " +
            "border-left-style:solid; border-left-color:#000000;border-bottom-style:solid; border-bottom-color:#000000;" +
            "border-right-style:solid; border-right-color:#000000;background-color:transparent; padding-left:0.0035in; padding-right:0.0035in; padding-top:0in; padding-bottom:0in; " +
            "vertical-align:middle;  width:auto";

    public static String P_SPAN = "<p style = \"text-align: center; text-indent: 0em; line-height: normal;\" ><span style=\"text-align: -webkit-center; text-wrap: wrap;\">content</span> </p> " ;

    public static final String SPAN_STYLE = "text-align: -webkit-center; text-wrap: wrap;";
}
