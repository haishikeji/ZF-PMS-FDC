//package rich;
//
//import com.px.report.calculation.cityPrice.bo.BusinessCenterBaseBO;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.lang.reflect.Field;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.px.report.html.TableHTML.TABLE;
//import static com.px.report.html.TableHTML.TR;
//
///**
// * 类说明：
// *
// * @author: wucl
// * @since: 2023/9/14
// * created with IntelliJ IDEA.
// */
//public class DataToHtmlTest {
//
//    public static void main(String[] args) {
//       //toHtml();
//        BusinessCenterBaseBO build = BusinessCenterBaseBO.builder().no(1).name("世纪摩尔购物中心").acreage(BigDecimal.TEN).categories("服装，购物")
//                .shopNum(BigDecimal.TEN).shoppingNum(BigDecimal.TEN).rent(BigDecimal.TEN).turnover(BigDecimal.TEN).scope("文通路上段2323号").build();
//
//        List<Field> fields = Arrays.asList(BusinessCenterBaseBO.class.getDeclaredFields());
//        fields.stream().forEach(x->{
//            try {
//                x.setAccessible(true);
//                Object o = x.get(build);
//                System.out.println(o);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//
//        });
//    }
//
//    public static void toHtml(){
//        String str = TABLE;
//        Document table = Jsoup.parseBodyFragment(str);
//        Elements select = table.select(".tableHead");
//        List<String> label = new ArrayList<>();
//        label.add("序号");
//        label.add("商服名称");
//        label.add("商服中心范围");
//        label.add("经营种类");
//        label.add("占地面积");
//        label.add("店铺数量");
//        label.add("购物人次");
//        label.add("营业额");
//        label.add("平均年租金");
//        for (String s : label){
//            String tr = "<td style=\"background-color:rgb(215,215,215); border-color: rgb(0, 0, 0); border-width: 1px; border-style: solid;\" width=\"auto\" align=\"center\">\n" +s+
//                    "\t\t\t</td>";
//            select.append(tr);
//        }
//        List<BusinessCenterBaseBO> list = new ArrayList<>();
//        list.add(BusinessCenterBaseBO.builder().no(1).name("世纪摩尔购物中心").acreage(BigDecimal.TEN).categories("服装，购物")
//                .shopNum(BigDecimal.TEN).shoppingNum(BigDecimal.TEN).rent(BigDecimal.TEN).turnover(BigDecimal.TEN).scope("文通路上段2323号").build());
//        Elements body = table.select(".tbody");
//        for (BusinessCenterBaseBO b : list){
//            body.append(TR);
//            Element tr = table.select("tr").last();
//            tr.append("<td style=\\\"border-color: rgb(0, 0, 0); border-width: 1px; border-style: solid;\\\" \" +\n" +
//                    "            \"                   width=\\\"auto\\\" align=\\\"center\\\">\\\\n\\\" \" +\n" + b.getNo() +
//                    "            \"                    \\\"\\\\t\\\\t\\\\t</td>");
//            tr.append("<td style=\\\"border-color: rgb(0, 0, 0); border-width: 1px; border-style: solid;\\\" \" +\n" +
//                    "            \"                   width=\\\"auto\\\" align=\\\"center\\\">\\\\n\\\" \" +\n" + b.getName() +
//                    "            \"                    \\\"\\\\t\\\\t\\\\t</td>");
//            tr.append("<td style=\\\"border-color: rgb(0, 0, 0); border-width: 1px; border-style: solid;\\\" \" +\n" +
//                    "            \"                   width=\\\"auto\\\" align=\\\"center\\\">\\\\n\\\" \" +\n" + b.getAcreage() +
//                    "            \"                    \\\"\\\\t\\\\t\\\\t</td>");
//            tr.append("<td style=\\\"border-color: rgb(0, 0, 0); border-width: 1px; border-style: solid;\\\" \" +\n" +
//                    "            \"                   width=\\\"auto\\\" align=\\\"center\\\">\\\\n\\\" \" +\n" + b.getCategories() +
//                    "            \"                    \\\"\\\\t\\\\t\\\\t</td>");
//            tr.append("<td style=\\\"border-color: rgb(0, 0, 0); border-width: 1px; border-style: solid;\\\" \" +\n" +
//                    "            \"                   width=\\\"auto\\\" align=\\\"center\\\">\\\\n\\\" \" +\n" + b.getShopNum() +
//                    "            \"                    \\\"\\\\t\\\\t\\\\t</td>");
//            tr.append("<td style=\\\"border-color: rgb(0, 0, 0); border-width: 1px; border-style: solid;\\\" \" +\n" +
//                    "            \"                   width=\\\"auto\\\" align=\\\"center\\\">\\\\n\\\" \" +\n" + b.getShoppingNum() +
//                    "            \"                    \\\"\\\\t\\\\t\\\\t</td>");
//            tr.append("<td style=\\\"border-color: rgb(0, 0, 0); border-width: 1px; border-style: solid;\\\" \" +\n" +
//                    "            \"                   width=\\\"auto\\\" align=\\\"center\\\">\\\\n\\\" \" +\n" + b.getRent() +
//                    "            \"                    \\\"\\\\t\\\\t\\\\t</td>");
//            tr.append("<td style=\\\"border-color: rgb(0, 0, 0); border-width: 1px; border-style: solid;\\\" \" +\n" +
//                    "            \"                   width=\\\"auto\\\" align=\\\"center\\\">\\\\n\\\" \" +\n" + b.getTurnover() +
//                    "            \"                    \\\"\\\\t\\\\t\\\\t</td>");
//            tr.append("<td style=\\\"border-color: rgb(0, 0, 0); border-width: 1px; border-style: solid;\\\" \" +\n" +
//                    "            \"                   width=\\\"auto\\\" align=\\\"center\\\">\\\\n\\\" \" +\n" + b.getScope() +
//                    "            \"                    \\\"\\\\t\\\\t\\\\t</td>");
//        }
//        String html = table.body().html();
//        String s = table.outerHtml();
//        System.out.println(html);
//    }
//
//
//}
