package com.safe.demo.web.service;

import com.safe.demo.web.pojo.WordTemplate;

/**
 * @Author: white_xiaosheng
 * @Description: TODO
 * @CreateTime: 2021/3/4 8:56 下午
 * @Version 1.0
 */
public interface WordManagerService {
    WordTemplate wordDateMake(int word_template_id);
    int makeData(WordTemplate wordTemplate);
}
