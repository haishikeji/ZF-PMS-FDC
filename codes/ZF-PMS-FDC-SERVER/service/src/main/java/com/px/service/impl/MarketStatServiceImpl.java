package com.px.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.px.common.BaseEntity;
import com.px.dto.MarketStatDTO;
import com.px.entity.Team;
import com.px.entity.TeamMember;
import com.px.mapper.MarketStatMapper;
import com.px.service.IMarketStatService;
import com.px.service.ITeamMemberService;
import com.px.service.ITeamService;
import com.px.service.IUserService;
import com.px.utils.DateUtils;
import com.px.vo.MarketStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/1/13
 * created with IntelliJ IDEA.
 */
@Service
public class MarketStatServiceImpl implements IMarketStatService {

    @Autowired
    private MarketStatMapper marketStatMapper;

    @Autowired
    private ITeamService teamService;

    @Autowired
    private ITeamMemberService teamMemberService;

    @Autowired
    private IUserService userService;

    @Override
    public MarketStatVO marketStat(MarketStatDTO dto) {

        LocalDate lastMonth23 = null;
        if (dto.getMonth()==null || dto.getYear()==null){
            lastMonth23 = DateUtils.getLastMonth23(LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1);
        }else {
            lastMonth23 = DateUtils.getLastMonth23(dto.getYear(), dto.getMonth() - 1);
        }
        return marketStatMapper.marketStat(dto,lastMonth23);
    }

    @Override
    public BigDecimal currentMonthPayment(Long userId) {
        LocalDate lastMonth23 = DateUtils.getLastMonth23(LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1);
        BigDecimal currentMontAmount = marketStatMapper.currentMonthPayment(userId,lastMonth23);
        return currentMontAmount;
    }

    @Override
    public BigDecimal currentYearPayment(Long userId) {
        BigDecimal currentYearPayment = marketStatMapper.currentYearPayment(userId);
        return currentYearPayment;
    }

    @Override
    public BigDecimal currentYearTeamPayment(Long userId) {
        Set<Long> juniorUserId = new HashSet<>();
        Team team = teamService.getOne(new LambdaQueryWrapper<Team>().eq(Team::getSupervisorId, userId).eq(BaseEntity::getDeleted, Boolean.FALSE));
        if (team==null){

            juniorUserId = userService.getJuniorUserId(userId);
        }else {
            juniorUserId = teamMemberService.list(new LambdaQueryWrapper<TeamMember>()
                    .eq(TeamMember::getTeamId,team.getId()).eq(BaseEntity::getDeleted,Boolean.FALSE)).stream().map(TeamMember::getMemberId).collect(Collectors.toSet());
        }
        if (CollectionUtils.isNotEmpty(juniorUserId)){
            return marketStatMapper.currentYearTeamPayment(juniorUserId);
        }
        return BigDecimal.ZERO;
    }


}
