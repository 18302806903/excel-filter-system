package org.spring.springboot.dao;

import org.apache.ibatis.annotations.*;
import org.spring.springboot.domain.City;
import org.spring.springboot.domain.FilterObject;

import java.util.List;

@Mapper
public interface FilterObjectDao {


    @Select("select * from filter_object where id=#{id}")
    @Results({
            @Result(id=true,property="id",column="id"),
            @Result(property="name",column="name"),
            @Result(property="desc",column="desc"),
            @Result(property="filterDetailList",column="id",javaType= List.class, many=@Many(select="org.spring.springboot.dao.FilterDetailDao.getFilterDetailsByObjectId"))
    })
    FilterObject getFilterObjectById(int id);

    @Select("select * from filter_object")
    @Results({
            @Result(id=true,property="id",column="id"),
            @Result(property="name",column="name"),
            @Result(property="desc",column="desc"),
            @Result(property="filterDetailList",column="id",javaType= List.class, many=@Many(select="org.spring.springboot.dao.FilterDetailDao.getFilterDetailsByObjectId"))
    })
    List<FilterObject> getAllFilterObject();
}
