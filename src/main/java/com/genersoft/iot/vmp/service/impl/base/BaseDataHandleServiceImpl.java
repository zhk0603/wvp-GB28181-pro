package com.genersoft.iot.vmp.service.impl.base;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.genersoft.iot.vmp.conf.security.JwtUtils;
import com.genersoft.iot.vmp.storager.dao.UserMapper;
import com.genersoft.iot.vmp.storager.dao.dto.User;
import com.genersoft.iot.vmp.vmanager.bean.PageInfo;
import com.topevery.sdk.common.properties.RunBaseProperties;
import com.topevery.sdk.common.util.RsaUtil;
import com.topevery.sdk.sync.enums.OpenDataStateEnum;
import com.topevery.sdk.sync.query.BaseQuery;
import com.topevery.sdk.sync.vo.*;
import com.topevery.sdk.sync.web.DataPager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BaseDataHandleServiceImpl implements IBaseDataHandleService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public DataPager<OpenUserVO> getUsers(BaseQuery query, RunBaseProperties properties) {
        log.info("query 的数据为： {}， properties的数据为{}", query, properties);
        DataPager<OpenUserVO> res = new DataPager<>();
        List<User> users = userMapper.selectAll();
        if (CollectionUtils.isNotEmpty(users)){
            int pageIndex = query.getPageIndex();
            int pageSize = query.getPageSize();
            PageInfo<User> userPageInfo = new PageInfo<>(users);
            userPageInfo.startPage(pageIndex, pageSize);
            List<User> list = userPageInfo.getList();
            List<OpenUserVO> userList = new ArrayList<>();
            res.setTotal(users.size());
            res.setRecords(userList);


            list.forEach(e -> {
                OpenUserVO userVO = new OpenUserVO();
                String userId = String.valueOf(e.getId());
                userVO.setOpenId(userId);
                userVO.setUserId(userId);
                userVO.setUserName(e.getUsername());
                userVO.setLoginName(e.getUsername());
                userVO.setDeptId(e.getDept().getDeptUid());
                userVO.setDeptName(e.getDept().getDeptName());
                userVO.setMobile("12222222222");
                userVO.setEmail("");
                userVO.setSystemCode(properties.getSystemCode().get(0));
                userVO.setOrderNum(0);
                userVO.setState(OpenDataStateEnum.CREATE.getName());

                OpenDeptVO deptVO = new OpenDeptVO();
                deptVO.setDeptId(e.getDept().getDeptUid());
                deptVO.setDeptCode("DEPT_1001");
                deptVO.setDeptName(e.getDept().getDeptName());
                deptVO.setDeptPid(e.getDept().getDeptPid());
                deptVO.setDeptPname("深圳市");
                userVO.setDeptList(CollectionUtil.newArrayList(deptVO));
                OpenRoleVO roleVO = new OpenRoleVO();
                roleVO.setRoleId(String.valueOf(e.getRole().getId()));
                roleVO.setRoleName(e.getRole().getName());
                roleVO.setRoleCode("ROLE_1001");
                userVO.setRoleList(CollectionUtil.newArrayList(roleVO));
                userList.add(userVO);
            });
        }
        return res;
    }

    @Override
    public DataPager<OpenDeptVO> getDepts(BaseQuery query, RunBaseProperties properties) {
        return new DataPager<>();
    }

    @Override
    public DataPager<OpenMenuVO> getMenus(BaseQuery query, RunBaseProperties properties) {
        return new DataPager<>();
    }

    @Override
    public DataPager<OpenRoleVO> getRoles(BaseQuery query, RunBaseProperties properties) {
        return new DataPager<>();
    }

    @Override
    public DataPager<OpenUserInDeptVO> getUserInDepts(BaseQuery query, RunBaseProperties properties) {
        return new DataPager<>();
    }

    @Override
    public DataPager<OpenUserInRoleVO> getUserInRoles(BaseQuery query, RunBaseProperties properties) {
        return new DataPager<>();
    }

    @Override
    public DataPager<OpenUserInMenuVO> getUserInMenus(BaseQuery query, RunBaseProperties properties) {
        return new DataPager<>();
    }

    @Override
    public DataPager<OpenMenuInRoleVO> getMenuInRoles(BaseQuery query, RunBaseProperties properties) {
        return new DataPager<>();
    }

    @Override
    public OpenTokenVO loginByToken(String token, RunBaseProperties properties) {
        try {
            // 这里传输过来的token是使用私钥进行加密过的，所以在这里需要使用公钥进行解密
            String openToken = RsaUtil.decryptByPublicKey(token, properties.getRsaPublicKey());
            JWT jwt = JWTUtil.parseToken(openToken);

            String loginName = (String) jwt.getPayload("userName");
            if (StringUtils.isNotEmpty(loginName)) {
                OpenTokenVO tokenVO = new OpenTokenVO();
                User user = userMapper.getUserByUsername(loginName);
                if (user == null){
                    throw new RuntimeException("用户不存在!");
                }
                String token1 = JwtUtils.createToken(loginName);
                tokenVO.setAccess_token(token1);
                tokenVO.setExpires_in((int) (JwtUtils.expirationTime*60));
                tokenVO.setToken_type("");
                tokenVO.setRefresh_token(token1);
                tokenVO.setScope("password");
                return tokenVO;
            } else {
                throw new RuntimeException("token静默登录失败!");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
