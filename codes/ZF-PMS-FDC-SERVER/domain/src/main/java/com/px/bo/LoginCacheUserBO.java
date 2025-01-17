package com.px.bo;

import com.px.dto.SimplePostModel;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class LoginCacheUserBO {

    private String account;

    private Long id;


    private String name;


    private List<SimplePostModel> postList;

    private Set<PrivilegeTreeBO> privilegeList;

    private List<SimpleParentModel> departmentList;


}
