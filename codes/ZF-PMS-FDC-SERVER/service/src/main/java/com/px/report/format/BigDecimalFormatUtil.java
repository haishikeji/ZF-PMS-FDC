package com.px.report.format;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/9/18
 * created with IntelliJ IDEA.
 */
public class BigDecimalFormatUtil {

    public static BigDecimal divideResult_Multiply100Scale2HalfUp(BigDecimal param1,BigDecimal param2){
        return param1.divide(param2,5, RoundingMode.HALF_UP).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).setScale(2,RoundingMode.HALF_UP);
    }

    public static BigDecimal format_Divide10Scale0HalfUpMultiply10(BigDecimal param){
       return param.divide(BigDecimal.TEN,0,RoundingMode.HALF_UP).multiply(BigDecimal.TEN);
    }
}
