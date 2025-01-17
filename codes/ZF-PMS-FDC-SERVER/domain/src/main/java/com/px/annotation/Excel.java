package com.px.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义导出Excel数据注解
 *
 * @author 品讯科技
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {

    /**
     * 导出到Excel中的名字.
     */
    String name() default "";

    /**
     * 日期格式, 如: yyyy-MM-dd
     */
    String dateFormat() default "";


    /**
     * 读取内容转表达式 (如: 0=男,1=女,2=未知)
     */
    String readConverterExp() default "";

    /**
     * 导出类型（0数字 1字符串） 只针对有数据的行
     */
    ColumnType cellType() default ColumnType.STRING;

    /**
     * 导出时在excel中每个列的高度 单位为字符
     */
    double height() default 15;

    /**
     * 导出时在excel中每个列的宽 单位为字符
     */
    double width() default 20;

    /**
     * 文字后缀,如% 90 变成90%
     */
    String suffix() default "";

    /**
     * 当值为空时,字段的默认值
     */
    String defaultValue() default "";

    /**
     * 提示信息
     */
    String prompt() default "";

    /**
     * 设置只能选择不能输入的列内容.
     */
    String[] combo() default {};

    /**
     * 下拉框 多个逗号隔开即可
     *
     * @return
     */
    String combos() default "";

    /**
     * 必填 字体为黄色高亮 并会检查是否为空
     * 在导入时 如果字段为空 导入失败  给出具体提示信息
     * 注意！ 当整行所有的字段都为空的时候 不会提示 会跳过解析
     * 因为各种原因 excel 可能会有空行存在 但是用户根本不知道
     *
     * @return
     */
    boolean required() default false;


    /**
     * 是否导出数据,应对需求:有时我们需要导出一份模板,这是标题需要但内容需要用户手工填写.
     */
    boolean isExport() default true;

    /**
     * 动态渲染 值能在spring容器里找到 需要实现 ExcelColmunRender 接口
     *
     * @return
     */
    String exportCallBack() default "";

    /**
     * 类似身份证这类需要标记true 防止科学计数法导致溢出
     * 导出的模板种 设置 MAX_IMPORT_ROW 行 此列的格式
     * 下次使用此模板导入时 即可避免数字类型导致的错误
     *
     * @return
     */
    boolean isNumberString() default false;


    /**
     * 另一个类中的属性名称,支持多级获取,以小数点隔开
     */
    String targetAttr() default "";


    /**
     * 字段类型（0：导出导入；1：仅导出；2：仅导入）
     */
    Type type() default Type.ALL;

    /**
     * 动态数据  传入字典值
     * 暂未实现 有需求自行实现
     *
     * @return
     */
    @Deprecated
    String dynamic() default "";

    @Deprecated
    boolean special() default false;


    @Deprecated
    int specialSheet() default 0;

    public enum Type {
        ALL(0), EXPORT(1), IMPORT(2);
        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    public enum ColumnType {
        NUMERIC(0), STRING(1);
        private final int value;

        ColumnType(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}