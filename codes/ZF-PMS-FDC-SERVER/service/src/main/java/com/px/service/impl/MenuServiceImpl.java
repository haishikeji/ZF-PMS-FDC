package com.px.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.px.bo.MenuTreeBO;
import com.px.bo.PrivilegeTreeBO;
import com.px.entity.Menu;
import com.px.mapper.MenuMapper;
import com.px.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.utils.ConvertUtil;
import com.px.utils.Convertor;
import com.px.utils.TreeUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.utils.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import com.px.enums.BatchTaskTypeEnum;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2022-11-29
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    private static final List<String> menuExcelTitles= new ArrayList();

    static{
            menuExcelTitles.add("菜单编号");
            menuExcelTitles.add("菜单code");
            menuExcelTitles.add("菜单名称");
            menuExcelTitles.add("父级菜单id");
            menuExcelTitles.add("显示顺序");
            menuExcelTitles.add("菜单类型 菜单/按钮");
            menuExcelTitles.add("状态:1:正常 0:禁用");
            menuExcelTitles.add("pc端url");
            menuExcelTitles.add("pc端图标");
            menuExcelTitles.add("是否缓存页面");
    }


    @Override
    @SuppressWarnings("unchecked")
    public Page<Menu> selectPage(Page page,Menu menu){
        return this.page(page, new QueryWrapper<Menu>(menu));
    }


    @Override
    public Menu detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(Menu menu){
        menu.setMenuNo(UUID.randomUUID().toString());
        return  this.save(menu);
    }

    @Override
    public Boolean update(Menu menu){
        return  this.updateById(menu);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public XSSFWorkbook generateImportTemplate(){
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建固定列的表头
        createExcelTitle(wb);
        return wb;
    }

    private void createExcelTitle(XSSFWorkbook wb) {
        Sheet sheet = wb.createSheet("菜单导入模板");
        Row row = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        Cell cell = null;
        for (int i = 0; i < menuExcelTitles.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(menuExcelTitles.get(i));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importExcel(MultipartFile file){
        ExcelUtil.importExcel(Menu.class, new ExcelUtil.ImportListener<Menu>() {
            @Override
            public Boolean handleOneRecord(Menu menu) {
                return add(menu);
            }

            @Override
            public void afterImport(ExcelUtil.BatchImportResult batchImportResult) {

            }
        }, file, BatchTaskTypeEnum.DEFAULT,ExcelUtil.BatchImportStrategy.ROLL_BACK);
    }

    @Override
    public XSSFWorkbook exportExcel(Menu menu) {
        XSSFWorkbook wb = ExcelUtil.export(Menu.class,
        (page, menuReq) -> selectPage(page,menuReq),
        menu);
        return wb;
    }

    @Override
    public Collection<PrivilegeTreeBO> syncMenu(Collection<PrivilegeTreeBO> collection) {
        //获取有效的菜单
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<Menu>()
                .eq(Menu::getEnabled, true)
                .eq(Menu::getDeleted, false);
        List<Menu> menus = this.list(menuLambdaQueryWrapper);

        if(CollectionUtils.isEmpty(menus)){
            return Collections.EMPTY_LIST;
        }
        Map<Long,List<PrivilegeTreeBO>> map=collection.stream().collect(Collectors.groupingBy(PrivilegeTreeBO::getId));
        List<PrivilegeTreeBO> list = menus.stream().map(x->{
            PrivilegeTreeBO bo= ConvertUtil.copyBean(x, new Convertor<Menu, PrivilegeTreeBO>() {
                @Override
                public PrivilegeTreeBO convert(Menu menu) {
                    if (menu == null) {
                        return null;
                    }
                    PrivilegeTreeBO privilegeTreeBO = new PrivilegeTreeBO();
                    privilegeTreeBO.setMenuNo(menu.getMenuNo());
                    privilegeTreeBO.setMenuCode(menu.getMenuCode());
                    privilegeTreeBO.setName(menu.getName());
                    privilegeTreeBO.setParentId(menu.getParentId());
                    privilegeTreeBO.setOrderNum(menu.getOrderNum()!=null?menu.getOrderNum():0);
                    privilegeTreeBO.setMenuType(menu.getMenuType());
                    privilegeTreeBO.setPcUrl(menu.getPcUrl());
                    privilegeTreeBO.setPcIcon(menu.getPcIcon());
                    privilegeTreeBO.setId(menu.getId());
                    privilegeTreeBO.setCachedViews(menu.getCachedViews());
                    //从权限树中取是否勾选和数据权限
                    List<PrivilegeTreeBO> privilegeTreeBOList = map.get(menu.getId());
                    if(CollectionUtils.isNotEmpty(privilegeTreeBOList)){
                        PrivilegeTreeBO authBo = privilegeTreeBOList.stream().findFirst().get();
                        privilegeTreeBO.setChecked(authBo.getChecked());
                    }

                    return privilegeTreeBO;
                }
            });
            return bo;
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<MenuTreeBO> menuTree() {
        List<MenuTreeBO> results = null;
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<Menu>()
//                .eq(Menu::getEnabled, true)
                .eq(Menu::getDeleted, false);

        List<Menu> allMenus = this.list(menuLambdaQueryWrapper);
        if (CollectionUtils.isNotEmpty(allMenus)) {
            results = ConvertUtil.copyList(allMenus, menu -> {
                MenuTreeBO menuTreeBO = new MenuTreeBO();
                menuTreeBO.setMenuNo(menu.getMenuNo());
                menuTreeBO.setName(menu.getName());
                menuTreeBO.setParentId(menu.getParentId());
                menuTreeBO.setId(menu.getId());
                menuTreeBO.setMenuType(menu.getMenuType());
                menuTreeBO.setOrderNum(menu.getOrderNum() != null ? menu.getOrderNum() : 0);
                return menuTreeBO;
            });

            results = (List<MenuTreeBO>) TreeUtil.buildTree(results);
        }
        return results;
    }

    @Override
    public List<Menu> menuList() {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<Menu>()
                .eq(Menu::getEnabled, true)
                .eq(Menu::getDeleted, false);

        List<Menu> allMenus = this.list(menuLambdaQueryWrapper);
        return allMenus;
    }

}
