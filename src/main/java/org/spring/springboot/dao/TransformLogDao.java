package org.spring.springboot.dao;

import org.apache.ibatis.annotations.*;
import org.spring.springboot.domain.FilterObject;
import org.spring.springboot.domain.TransformLog;

import java.util.List;

@Mapper
public interface TransformLogDao {


    @Select("select * from transform_log where excel_name=#{excelName}")
    @Results({
            @Result(id=true,property="id",column="id"),
            @Result(property="excelName",column="excel_name"),
            @Result(property="filter",column="filter"),
            @Result(property="replaceText",column="replace_text"),
            @Result(property="location",column="location"),
            @Result(property="textBefore",column="text_before"),
            @Result(property="textAfter",column="text_after"),
            @Result(property="success",column="success"),
    })
    List<TransformLog> getTransformLogByExcelName(String excelName);

//    @Select("select * from filter_object")
//    @Results({
//            @Result(id=true,property="id",column="id"),
//            @Result(property="name",column="name"),
//            @Result(property="description",column="description"),
//            @Result(property="filterDetailList",column="id",javaType= List.class, many=@Many(select="org.spring.springboot.dao.FilterDetailDao.getFilterDetailsByObjectId"))
//    })
//    List<FilterObject> getAllFilterObject();

    @Insert("insert into transform_log(excel_name, filter,replace_text, location, text_before, text_after, success) values(#{excelName},#{filter},#{replaceText},#{location},#{textBefore},#{textAfter},#{success})")
    int saveTransformLog(TransformLog transformLog);

//    @Delete("delete from filter_object where id=#{id}")
//    int removeFilterObject(int id);
}
