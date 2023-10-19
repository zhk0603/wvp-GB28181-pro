package com.genersoft.iot.vmp.storager.dao;

import com.genersoft.iot.vmp.storager.dao.dto.Dept;
import com.genersoft.iot.vmp.storager.dao.dto.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DeptMapper {

    @Select("select * from wvp_user_dept WHERE id=#{id}")
    Dept selectById(int id);
}
