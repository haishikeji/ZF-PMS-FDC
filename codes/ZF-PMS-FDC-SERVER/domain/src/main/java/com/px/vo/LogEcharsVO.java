package com.px.vo;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2022/12/22
 * created with IntelliJ IDEA.
 */
@Data
public class LogEcharsVO {

    //legend
    private List<String> itemNames = new ArrayList<>();

    //xAxis
    private  Axis xAsis = new Axis();

    //series
    private List<EcharsItem> items = new ArrayList();


    @Data
    public static class EcharsItem{
        private String name;

        private String type = "line";

        private String stack = "Total";

        private List<Integer> data = new ArrayList<>();
    }
    @Data
    public static class Axis{
        private String type = "category";

        private Boolean boundaryGap = false;

        private List<LocalDate> data = new ArrayList<>();
    }
}
