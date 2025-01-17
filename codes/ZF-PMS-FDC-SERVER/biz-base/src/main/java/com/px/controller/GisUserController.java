package com.px.controller;

import com.px.annotation.IgnoreAuth;
import com.px.annotation.OperLog;
import com.px.condition.GisLoginCondition;
import com.px.dto.LoginDTO;
import com.px.dto.UpdatePasswordDTO;
import com.px.enums.OperationTypeEnum;
import com.px.utils.ImageCode;
import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IGisUserService;
import com.px.entity.GisUser;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;
import com.px.common.PullDownModel;
import com.px.utils.ConvertUtil;
import com.px.utils.HttpKit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.px.constants.JwtConstants.REST_TOKEN;

/**
 * GIS用户
 *
 * @author 品讯科技
 * @since 2023-05-17
 */
@RestController
@RequestMapping("gisUser")
@Slf4j
public class GisUserController extends BaseController {
    @Autowired
    private IGisUserService gisUserService;

    @Autowired
    @Qualifier("verificationCodeCache")
    private Cache<String, String> cache;

    /**
    * GIS用户列表
    */
    @GetMapping("")
    public RestResponse<Page<GisUser>> page(GisUser gisUser, Page page){
        Page<GisUser> pages=gisUserService.selectPage(page,gisUser);
        return RestResponse.data(pages);
    }

    /**
     * GIS用户详情
     */
    @GetMapping("/{id}")
    public RestResponse<GisUser> detail(@PathVariable Long id){
        GisUser xGisUser =gisUserService.detail(id);
        return RestResponse.data(xGisUser);
     }

    /**
     * GIS用户新增
     */
    @PostMapping("")
    public RestResponse<Boolean> save(@RequestBody GisUser gisUser) {
        Boolean ret = gisUserService.add(gisUser);
        return RestResponse.data(ret);
    }

    /**
     * GIS用户更新
     */
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody GisUser gisUser) {
        Boolean ret = gisUserService.update(gisUser);
        return RestResponse.data(ret);
    }

    /**
     * GIS用户删除
     */
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = gisUserService.delete(id);
        return RestResponse.data(ret);
    }


    /**
     * GIS用户下拉列表
     */
    @GetMapping("/simpleAll")
    public RestResponse<List<PullDownModel>> simpleAll(){
        LambdaQueryWrapper<GisUser> lambdaQueryWrapper = new LambdaQueryWrapper<GisUser>()
        .select(GisUser::getId,GisUser::getName)
        .eq(GisUser::getDeleted,false);
        List<GisUser> list= gisUserService.list(lambdaQueryWrapper);
        List<PullDownModel> ret=ConvertUtil.copyList(list, PullDownModel.class);
        return RestResponse.data(ret);
    }

    /**
     * 生成登录验证码
     * @return
     */
    @IgnoreAuth
    @GetMapping("/verification")
    public void genVerificationCode(HttpServletResponse response,String nonceId) {
        ImageCode imageCode = ImageCode.getInstance();
        //获取验证码内容
        String code = imageCode.getCode();
        //存放到缓存
        cache.put(nonceId,code);
        //获取图片
        ByteArrayInputStream image = imageCode.getImage();
        //设置内容类型
        response.setContentType("image/jpg");
        byte[] bytes = new byte[1024];
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            while (image.read(bytes) != -1) {
                outputStream.write(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     *
     * @param gisLoginCondition
     * @return
     */
    @PostMapping("/login")
    @OperLog(title = "GIS登录", businessType = OperationTypeEnum.LOGIN)
    public RestResponse<LoginDTO> login(@RequestBody @Valid GisLoginCondition gisLoginCondition) {
        LoginDTO loginDTO = gisUserService.login(gisLoginCondition);
        return RestResponse.data(loginDTO);
    }

    /**
     * 根据token获取基本信息
     *
     * @return
     */
    @GetMapping("/baseInfo")
    public RestResponse<GisUser> baseInfo() {
        String token = HttpKit.getHeaderValue(REST_TOKEN);
        GisUser gisUser = gisUserService.baseInfo(token);
        return RestResponse.data(gisUser);
    }

    /**
     * 心跳监测
     * @return
     */
    @PostMapping("/keepAlive")
    public RestResponse<Boolean> isAlive(){
        String token = HttpKit.getHeaderValue(REST_TOKEN);
        Boolean alive = gisUserService.isAlive(token);
        return RestResponse.data(alive);
    }

    /**
     * 修改密码
     * @param passwordDTO
     * @return
     */
    @OperLog(title = "修改密码",businessType = OperationTypeEnum.SET)
    @PutMapping("/updatePassword")
    public RestResponse<Boolean> updatePassword(@RequestBody UpdatePasswordDTO passwordDTO){
        String token = HttpKit.getHeaderValue(REST_TOKEN);
        Boolean result = gisUserService.updatePassword(passwordDTO,token);
        return RestResponse.data(result);
    }

}

