package com.example.hellospring.mapper;

import com.example.hellospring.entity.Config;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ConfigMapper {
    List<Config> selectAll();

    Config selectByPrimaryKey(String configName);

    int updateByPrimaryKeySelective(Config record);
}
