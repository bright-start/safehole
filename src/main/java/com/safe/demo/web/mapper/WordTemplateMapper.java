package com.safe.demo.web.mapper;

import com.safe.demo.web.pojo.WordTemplate;
import com.safe.demo.web.pojo.WordTemplateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WordTemplateMapper {
    int countByExample(WordTemplateExample example);

    int deleteByExample(WordTemplateExample example);

    int deleteByPrimaryKey(Integer wordTemplateId);

    int insert(WordTemplate record);

    int insertSelective(WordTemplate record);

    List<WordTemplate> selectByExampleWithBLOBs(WordTemplateExample example);

    List<WordTemplate> selectByExample(WordTemplateExample example);

    WordTemplate selectByPrimaryKey(Integer wordTemplateId);

    int updateByExampleSelective(@Param("record") WordTemplate record, @Param("example") WordTemplateExample example);

    int updateByExampleWithBLOBs(@Param("record") WordTemplate record, @Param("example") WordTemplateExample example);

    int updateByExample(@Param("record") WordTemplate record, @Param("example") WordTemplateExample example);

    int updateByPrimaryKeySelective(WordTemplate record);

    int updateByPrimaryKeyWithBLOBs(WordTemplate record);

    int updateByPrimaryKey(WordTemplate record);
}