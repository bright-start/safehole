package com.safe.demo.web.service.impl;

import com.safe.demo.web.mapper.WordTemplateMapper;
import com.safe.demo.web.pojo.WordTemplate;
import com.safe.demo.web.pojo.WordTemplateExample;
import com.safe.demo.web.service.WordManagerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: white_xiaosheng
 * @Description: TODO
 * @CreateTime: 2021/3/4 8:57 下午
 * @Version 1.0
 */
@Service
public class WordManagerServiceImpl implements WordManagerService {

    @Resource
    private WordTemplateMapper wordTemplateMapper;

    @Override
    public WordTemplate wordDateMake(int word_template_id) {

        WordTemplateExample wordTemplateExample = new WordTemplateExample();
        WordTemplateExample.Criteria criteria = wordTemplateExample.createCriteria();
        criteria.andWordTemplateIdEqualTo(word_template_id);

        List<WordTemplate> wordTemplates = wordTemplateMapper.selectByExample(wordTemplateExample);

        List<WordTemplate> wordTemplates1 = wordTemplateMapper.selectByExampleWithBLOBs(wordTemplateExample);

        WordTemplate wordTemplate = new WordTemplate();

        if(wordTemplates != null && wordTemplates.size() > 0){
            WordTemplate wordTemplate1 = wordTemplates.get(0);
            wordTemplate.setWordTemplateId(wordTemplate1.getWordTemplateId());
            wordTemplate.setWordName(wordTemplate1.getWordName());
        }
        if(wordTemplates1 != null && wordTemplates1.size() > 0){
            WordTemplate wordTemplate2 = wordTemplates1.get(0);
            wordTemplate.setTemplateDate(wordTemplate2.getTemplateDate());
        }
        return wordTemplate;
    }

    @Override
    public int makeData(WordTemplate wordTemplate) {
        return wordTemplateMapper.insertSelective(wordTemplate);
    }
}
