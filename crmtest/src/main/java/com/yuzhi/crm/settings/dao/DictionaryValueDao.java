package com.yuzhi.crm.settings.dao;

import com.yuzhi.crm.settings.domain.DictionaryValue;

import java.util.List;

public interface DictionaryValueDao {
    List<DictionaryValue> findAllValueList();

    int save(DictionaryValue dictionaryValue);

    DictionaryValue findById(String id);

    int updateById(DictionaryValue dictionaryValue);

    List<DictionaryValue> findDictionaryValueListByTypeCode(String code);
}
