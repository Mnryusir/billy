package com.yuzhi.crm.settings.service.Imp;

import com.yuzhi.crm.exception.AjaxRequestException;
import com.yuzhi.crm.settings.dao.DictionaryTypeDao;
import com.yuzhi.crm.settings.dao.DictionaryValueDao;
import com.yuzhi.crm.settings.domain.DictionaryType;
import com.yuzhi.crm.settings.domain.DictionaryValue;
import com.yuzhi.crm.settings.service.DictionaryService;
import com.yuzhi.crm.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryTypeDao dictionaryTypeDao;
    @Autowired
    private DictionaryValueDao dictionaryValueDao;
    @Override
    public List<DictionaryType> findAllTypeList() {

        return dictionaryTypeDao.findAll();
    }

    @Override
    public DictionaryType findDictionaryTypeById(String code) {
        return dictionaryTypeDao.findDictionaryTypeById(code);
    }

    @Override
    public void saveDictionaryType(DictionaryType dictionaryType) throws AjaxRequestException {
        int count=dictionaryTypeDao.save(dictionaryType);
        if (count<=0){
        }
    }

    @Override
    public void updateDictionaryTypeByCode(DictionaryType dictionaryType) throws AjaxRequestException {
        int count=dictionaryTypeDao.updateByCode(dictionaryType);
        if (count<=0){
            throw new AjaxRequestException("修改失败");
        }
    }

    @Override
    public void deleteDicTypesByCodes(String[] codes) throws AjaxRequestException {
//有关联字典值的字典类型不能删，所以删除之前要判断是否关联
        List<String> nameList = new ArrayList<>();
        for (String code:codes){
        List<DictionaryValue> dictionaryValueList = dictionaryValueDao.findDictionaryValueListByTypeCode(code);
        if (dictionaryValueList!=null&&dictionaryValueList.size()>0){
               DictionaryType dictionaryType= dictionaryTypeDao.findDictionaryTypeById(code);
            String name = dictionaryType.getName();
            nameList.add(name);
        }
        }

        if(nameList.size() == 0){
            int count = dictionaryTypeDao.deleteByCodes(codes);
            if(count <= 0)
                throw new AjaxRequestException("批量删除失败");
        }else{
            //抛出异常或返回Map集合都可以
            //如果抛出异常,利用msg参数,不能够写提示信息了
            //写不能够删除的code的名称
            String names = "当前字典类型数据【";
            for (int i=0; i<nameList.size(); i++) {

                names += nameList.get(i);

                if(i < nameList.size() -1){
                    names += "、";
                }
            }
            names += "】包含关联关系,无法删除";
            throw new AjaxRequestException(names);
    }}

    @Override
    public List<DictionaryValue> findDictionaryValueList() {
        return dictionaryValueDao.findAllValueList();
    }

    @Override
    public void saveDictionaryValue(DictionaryValue dictionaryValue) throws AjaxRequestException {
        dictionaryValue.setId(UUIDUtil.getUUID());
        int count=dictionaryValueDao.save(dictionaryValue);
        if (count<=0){
            throw new AjaxRequestException("新增失败");
        }
    }

    @Override
    public DictionaryValue findDictionaryValueById(String id) {

        return dictionaryValueDao.findById(id);
    }

    @Override
    public void updateDictionaryValue(DictionaryValue dictionaryValue) throws AjaxRequestException {
        int count = dictionaryValueDao.updateById(dictionaryValue);

        if(count <= 0)
            throw new AjaxRequestException("修改失败");
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
        System.out.println(UUID.randomUUID().toString().length());

        System.out.println(UUID.randomUUID().toString().replaceAll("-",""));
        System.out.println(UUID.randomUUID().toString().replaceAll("-","").length());
    }
}
