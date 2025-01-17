package com.px.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.px.common.BaseEntity;
import com.px.dto.TaskRecordDTO;
import com.px.entity.PersonalCalculate;
import com.px.entity.PersonalExample;
import com.px.entity.PersonalTarget;
import com.px.mapper.PersonalCalculateMapper;
import com.px.mapper.PersonalExampleMapper;
import com.px.service.IPersonalCalculateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.service.IPersonalExampleService;
import com.px.service.IPersonalTargetService;
import com.px.utils.PersonalUtils;
import com.px.vo.CompareExampleItemVO;
import com.px.vo.CompareExampleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 个贷可比实例算价表 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-01
 */
@Service
public class PersonalCalculateServiceImpl extends ServiceImpl<PersonalCalculateMapper, PersonalCalculate> implements IPersonalCalculateService {

    @Autowired
    private IPersonalExampleService personalExampleService;

    @Autowired
    private IPersonalCalculateService personalCalculateService;

    @Autowired
    private PersonalCalculateMapper personalCalculateMapper;

    @Autowired
    private PersonalExampleMapper personalExampleMapper;

    @Autowired
    private IPersonalTargetService personalTargetService;

    @Override
    @SuppressWarnings("unchecked")
    public Page<PersonalCalculate> selectPage(Page page,PersonalCalculate personalCalculate){
        return this.page(page, new QueryWrapper<PersonalCalculate>(personalCalculate));
    }


    @Override
    public PersonalCalculate detail(Long id){
        return this.getById(id);
    }


    @Override
    public Boolean update(PersonalCalculate personalCalculate){
        return  this.updateById(personalCalculate);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Transactional
    @Override
    public Boolean calculate(TaskRecordDTO<CompareExampleVO> taskRecordDTO) {
        CompareExampleVO compareExampleVO = taskRecordDTO.getTaskData();
        Long calculateId = compareExampleVO.getCalculateId();
        if (calculateId!=null){
            //删除历史计算记录
            personalCalculateMapper.realDelete(calculateId);
            personalExampleMapper.realDelete(compareExampleVO.getTargetId());
        }
        PersonalCalculate personalCalculate = BeanUtil.copyProperties(compareExampleVO, PersonalCalculate.class);
        this.save(personalCalculate);
        personalTargetService.update(new LambdaUpdateWrapper<PersonalTarget>().eq(BaseEntity::getId,compareExampleVO.getTargetId())
                .set(PersonalTarget::getAcreage,compareExampleVO.getAcreage()).set(PersonalTarget::getAmount,compareExampleVO.getAmount()).set(PersonalTarget::getPrice,compareExampleVO.getPrice()));
        List<PersonalExample> personalExamples = PersonalUtils.example2Entity(compareExampleVO.getTargetId(), personalCalculate.getId(), compareExampleVO.getAnalysisData());
        return personalExampleService.saveBatch(personalExamples);
    }

    @Override
    public CompareExampleVO calculateResult(Long targetId) {
        List<PersonalExample> examples = personalExampleService.list(new LambdaQueryWrapper<PersonalExample>().eq(PersonalExample::getTargetId, targetId).eq(BaseEntity::getDeleted, Boolean.FALSE));
        if (examples.size()!=4){
            return null;
        }else {
            PersonalCalculate calculateParams = personalCalculateService.getById(examples.get(0).getCalculateId());
            CompareExampleVO compareExampleVO = BeanUtil.copyProperties(calculateParams, CompareExampleVO.class);
            compareExampleVO.setTargetId(targetId);
            compareExampleVO.setCalculateId(examples.get(0).getCalculateId());
            List<CompareExampleItemVO> items = PersonalUtils.entity2Example(examples);
            compareExampleVO.setAnalysisData(items);
            return compareExampleVO;
        }
    }
}
