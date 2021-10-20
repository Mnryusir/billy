package com.yuzhi.crm.settings.service;

import com.yuzhi.crm.exception.AjaxRequestException;
import com.yuzhi.crm.settings.domain.DictionaryType;
import com.yuzhi.crm.settings.domain.DictionaryValue;

import java.util.List;

public interface DictionaryService {



    List<DictionaryType> findAllTypeList() ;

    DictionaryType findDictionaryTypeById(String code);

    void saveDictionaryType(DictionaryType dictionaryType) throws AjaxRequestException;

    void updateDictionaryTypeByCode(DictionaryType dictionaryType) throws AjaxRequestException;

    void deleteDicTypesByCodes(String[] codes) throws AjaxRequestException;

    List<DictionaryValue> findDictionaryValueList();

    void saveDictionaryValue(DictionaryValue dictionaryValue) throws AjaxRequestException;

    DictionaryValue findDictionaryValueById(String id);

    void updateDictionaryValue(DictionaryValue dictionaryValue) throws AjaxRequestException;
}
