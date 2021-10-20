package com.yuzhi.crm.settings.dao;

import com.yuzhi.crm.settings.domain.DictionaryType;

import java.util.List;

public interface DictionaryTypeDao {
    List<DictionaryType> findAll();

    DictionaryType findDictionaryTypeById(String code);


    int save(DictionaryType dictionaryType);


    int updateByCode(DictionaryType dictionaryType);

    int deleteByCodes(String[] codes);
}
