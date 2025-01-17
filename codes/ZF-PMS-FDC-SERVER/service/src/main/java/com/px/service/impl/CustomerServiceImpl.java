package com.px.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.px.common.BaseEntity;
import com.px.dto.CustomerDTO;
import com.px.entity.Customer;
import com.px.entity.Districts;
import com.px.mapper.CustomerMapper;
import com.px.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.service.IDistrictsService;
import com.px.utils.LoginContext;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
 * 客户表 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-01-10
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService , InitializingBean {


    @Autowired
    private IDistrictsService districtsService;

    private Map<Long,String> districtsMap;

    @Autowired
    private CustomerMapper customerMapper;
    private static final List<String> customerExcelTitles = new ArrayList();

    static {
        customerExcelTitles.add("客户名字");
        customerExcelTitles.add("市(**市)");
        customerExcelTitles.add("区/县(**县)");
        customerExcelTitles.add("部门");
        customerExcelTitles.add("职位");
        customerExcelTitles.add("科室");
        customerExcelTitles.add("联系电话");
        customerExcelTitles.add("微信号");
        customerExcelTitles.add("QQ号");
        customerExcelTitles.add("客户等级");
        customerExcelTitles.add("客户地址");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<CustomerDTO> selectPage(Page page,CustomerDTO customer,String keyword){
        Page<CustomerDTO> result = customerMapper.getPage(page,customer,keyword);
        //Page result = this.page(page, new QueryWrapper<Customer>(customer).in(CollectionUtils.isNotEmpty(customer.getUserIds()),Customer::getUserId,customer.getUserIds()));
        List<CustomerDTO> xRecords = new ArrayList<>();
        List<CustomerDTO> records = result.getRecords();
        records.stream().forEach(x->{
            CustomerDTO dto = new CustomerDTO();
            BeanUtils.copyProperties(x,dto);
            if (StrUtil.isNotEmpty(x.getCity())){
                List<Long> citys = JSON.parseArray(x.getCity(), Long.class);
                dto.setFirstCity(districtsMap.get(citys.get(1)));
                dto.setSecCity(districtsMap.get(citys.get(2)));
            }
            xRecords.add(dto);
        });
        result.setRecords(xRecords);
        return result;
    }


    @Override
    public CustomerDTO detail(Long id){
        Customer byId = this.getById(id);
        CustomerDTO dto = new CustomerDTO();
        BeanUtils.copyProperties(byId,dto);
        dto.setCitys(JSON.parseArray(byId.getCity(),Long.class));
        return dto;
    }

    @Override
    public Boolean add(CustomerDTO customer){
        customer.setUserId(LoginContext.getUserId());
        List<Long> citys = customer.getCitys();
        if (CollectionUtils.isNotEmpty(citys)){
            customer.setCity(JSON.toJSONString(citys));
        }
        return  this.save(customer);
    }

    public Boolean add(Customer customer){
        customer.setUserId(LoginContext.getUserId());
        customer.setCity(districtsService.getDistIds(customer.getCity(),customer.getCounty()));
        return  this.save(customer);
    }
    @Override
    public Boolean update(CustomerDTO customer){
        List<Long> citys = customer.getCitys();
        if (CollectionUtils.isNotEmpty(citys)){
            customer.setCity(JSON.toJSONString(citys));
        }
        return  this.updateById(customer);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public XSSFWorkbook generateImportTemplate() {
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建固定列的表头
        createExcelTitle(wb);
        return wb;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importExcel(MultipartFile file) {
        ExcelUtil.importExcel(Customer.class, new ExcelUtil.ImportListener<Customer>() {
            @Override
            public Boolean handleOneRecord(Customer customer) {
                return add(customer);
            }

            @Override
            public void afterImport(ExcelUtil.BatchImportResult batchImportResult) {

            }
        }, file, BatchTaskTypeEnum.DEFAULT, ExcelUtil.BatchImportStrategy.ROLL_BACK);
    }

    @Override
    public List<CustomerDTO> getList(CustomerDTO customer) {
        List<CustomerDTO> list = customerMapper.getList(customer);
        list.stream().forEach(x->{
            CustomerDTO dto = new CustomerDTO();
            BeanUtils.copyProperties(x,dto);
            if (StrUtil.isNotEmpty(x.getCity())){
                List<Long> citys = JSON.parseArray(x.getCity(), Long.class);
                x.setFirstCity(districtsMap.get(citys.get(1)));
                x.setSecCity(districtsMap.get(citys.get(2)));
            }
        });
        return list;
    }

    @Override
    public Boolean changeManager(CustomerDTO customer) {
        return this.update(new LambdaUpdateWrapper<Customer>().eq(BaseEntity::getId,customer.getId()).set(Customer::getUserId,customer.getUserId()));
    }


    private void createExcelTitle(XSSFWorkbook wb) {
        Sheet sheet = wb.createSheet("客户信息导入模板");
        Row row = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        Cell cell = null;
        for (int i = 0; i < customerExcelTitles.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(customerExcelTitles.get(i));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        districtsMap = districtsService.list(new LambdaQueryWrapper<Districts>().select(Districts::getId, Districts::getExtName))
                .stream().collect(Collectors.toMap(Districts::getId, Districts::getExtName));
    }
}
