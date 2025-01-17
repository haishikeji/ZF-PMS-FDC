package com.px.service;
import com.px.dto.OrderProductionsBag;
import com.px.dto.TaskRecordDTO;
import com.px.entity.PersonalProduction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 个贷产品 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-18
 */
public interface IPersonalProductionService extends IService<PersonalProduction> {

        Page<PersonalProduction> selectPage(Page page,PersonalProduction personalProduction);

        PersonalProduction detail(Long id);

        Boolean add(PersonalProduction personalProduction);

        Boolean update(PersonalProduction personalProduction);

        Boolean delete(Long id);

    Boolean genFile(Long personalId, Integer tag);

    Boolean upload(String filePath, MultipartFile file,Long targetId, Integer tag);

    Boolean removeFile(Long id);

    Boolean checkPass(TaskRecordDTO<PersonalProduction> taskRecordDTO);

    Boolean repertoryIn(TaskRecordDTO<PersonalProduction> taskRecordDTO);

    Boolean repertoryOut(TaskRecordDTO<PersonalProduction> taskRecordDTO);

    OrderProductionsBag productionBag(Long id);

    Boolean genFile(Long personalId);

}
