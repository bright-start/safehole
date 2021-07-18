package com.safe.demo.hole.mapper;


import com.safe.demo.hole.pojo.UserM;

import java.util.List;

public interface SqlInjustionMapper {
    List<UserM> selectList(String table);

    List<UserM> selectList2(String name);

    List<UserM> selectList3(String name);

    List<UserM> selectListByIds(List<Integer> ids);
}
