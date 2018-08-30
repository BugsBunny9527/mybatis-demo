package com.mybatis.mapper;

import com.mybatis.domain.Employee;

/**
 * Created by Luke Wang on 2018/8/30.
 *
 */
public interface EmployeeMapper {

    Employee getEmpById(Integer id);
}
