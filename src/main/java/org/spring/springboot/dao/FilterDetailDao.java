package org.spring.springboot.dao;

import org.apache.ibatis.annotations.*;
import org.spring.springboot.domain.FilterDetail;
import org.spring.springboot.domain.FilterObject;

import java.util.List;

@Mapper
public interface FilterDetailDao {


    @Select("select * from filter_detail where object_id=#{objectId}")
    @Results({
            @Result(id=true,property="id",column="id"),
            @Result(property="name",column="name"),
            @Result(property="description",column="description"),
            @Result(property="filter",column="filter"),
            @Result(property="priority",column="priority"),
            @Result(property="objectId",column="object_id"),
//            @Result(property="filterObject",column="id",javaType= List.class, many=@Many(select="org.spring.springboot.domain.FilterDetail"))
    })
    List<FilterDetail> getFilterDetailsByObjectId(int objectId);

    @Select("select * from filter_detail where id=#{id}")
    @Results({
            @Result(id=true,property="id",column="id"),
            @Result(property="name",column="name"),
            @Result(property="description",column="description"),
            @Result(property="filter",column="filter"),
            @Result(property="priority",column="priority"),
            @Result(property="objectId",column="object_id"),
    })
    FilterDetail getFilterDetailsById(int id);

    @Select("select * from filter_detail")
    @Results({
            @Result(id=true,property="id",column="id"),
            @Result(property="name",column="name"),
            @Result(property="description",column="description"),
            @Result(property="filter",column="filter"),
            @Result(property="priority",column="priority"),
            @Result(property="objectId",column="object_id"),
    })
    List<FilterDetail> getAllFilterDetails();

    @Insert("insert into filter_detail(name, description, filter, priority, object_id) values(#{name},#{description},#{filter},#{priority},#{objectId})")
    int saveFilterDetail(FilterDetail filterDetail);

    @Delete("delete from filter_detail where id=#{id}")
    int removeFilterDetail(int id);
}
