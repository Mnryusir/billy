package com.yuzhi.crm.settings.web.controller;

import com.yuzhi.crm.exception.AjaxRequestException;
import com.yuzhi.crm.exception.TranditionRequestException;
import com.yuzhi.crm.settings.domain.DictionaryType;
import com.yuzhi.crm.settings.domain.DictionaryValue;
import com.yuzhi.crm.settings.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/settings/dictionary")
public class DictionaryController {
    @Autowired
    private DictionaryService dictronaryService;

    @RequestMapping(value = "/toIndex.do")
    public String toIndex(){

        return "/settings/dictionary/index";
    }
    @RequestMapping(value = "/type/toIndex.do")
    public String toTypeIndex(Model model){
        //将字典类型的列表数据查询出来
        //返回的一堆数据用列表类型接收
        //DictionaryType是实体类
        List<DictionaryType> dictionaryTypeList=dictronaryService.findAllTypeList();
        if (dictionaryTypeList!=null){
            model.addAttribute("dictionaryTypeList",dictionaryTypeList);
        }
        return "/settings/dictionary/type/index";
    }
    @RequestMapping(value = "/value/toIndex.do")
    public String toValueIndex(){

        return "/settings/dictionary/value/index";
    }
    @RequestMapping(value = "/type/toSave.do")
    public String toTypeSave(){
        return "/settings/dictionary/type/save";
    }
    //settings/dictionary/type/findDictionaryTypeByCode.do
    @RequestMapping(value = "/type/findDictionaryTypeByCode.do")
    @ResponseBody
    public Map<String,Object> findDictionaryTypeByCode(String code){
        Map<String, Object> resultMap = new HashMap<>();
        DictionaryType dictionaryType=dictronaryService.findDictionaryTypeById(code);
        if (dictionaryType!=null){
            resultMap.put("code",10001);
            resultMap.put("msg","code重复，禁止插入");

//            System.out.println("禁止");
        }else {
            resultMap.put("code",10000);
            resultMap.put("msg","允许插入");
//            System.out.println("允许");
        }
        return resultMap;
    }
    @RequestMapping(value = "/type/save.do")
    @ResponseBody
    public Map<String,Object> saveDictionaryType(DictionaryType dictionaryType) throws AjaxRequestException {
        Map<String, Object> resultMap = new HashMap<>();
        dictronaryService.saveDictionaryType(dictionaryType);
        resultMap.put("code",10000);
        resultMap.put("msg","插入成功");
//        System.out.println(resultMap.get("code"));
        return resultMap;
    }
    @RequestMapping(value = "/type/toEdit.do")
    public String toEdit(String code,Model model) throws AjaxRequestException {
        DictionaryType dictionaryType=dictronaryService.findDictionaryTypeById(code);
        if (dictionaryType==null){
            throw new AjaxRequestException("查询异常");
        }
        model.addAttribute(dictionaryType);
            return "/settings/dictionary/type/edit";

    }
    @RequestMapping(value = "/type/edit.do")
    @ResponseBody
    public Map<String,Object> edit(DictionaryType dictionaryType) throws AjaxRequestException {
        HashMap<String, Object> resultMap = new HashMap<>();
        dictronaryService.updateDictionaryTypeByCode(dictionaryType);
        resultMap.put("code",10000);
        resultMap.put("msg","修改成功");
        return resultMap;
    }
/*    @ResponseBody
    @RequestMapping(value = "/type/deleteDicTypesByCodes.do")
    public Map<String,Object> deleteDicTypesByCodes(String[] codes) throws AjaxRequestException {
        System.out.println("================");
        System.out.println("传递过来的参数"+codes);
        System.out.println("================");
        dictronaryService.deleteDicTypesByCodes(codes);
        HashMap<String, Object> resultMap = new HashMap<>();

        resultMap.put("code",10000);
        resultMap.put("msg","删除成功");
        return resultMap;
    }*/
    /**
     * 根据codes批量删除操作
     * @param codes
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping("/type/deleteDicTypesByCodes.do")
    @ResponseBody
    public Map<String,Object> deleteDicTypesByCodes(String[] codes) throws AjaxRequestException {
        //批量删除操作
       /* System.out.println("=========");
        System.out.println("codes:"+codes);
        System.out.println("=========");*/
        dictronaryService.deleteDicTypesByCodes(codes);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("code",10000);
        resultMap.put("msg","删除成功");
        return resultMap;

    }
    @RequestMapping(value = "/value/findDictionaryValueList.do")
    @ResponseBody
    public Map<String,Object> findDictionaryValueList(){
        List<DictionaryValue> dictionaryValueList=dictronaryService.findDictionaryValueList();
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code",10000);
        resultMap.put("msg","查询成功");
        resultMap.put("data",dictionaryValueList);
        return  resultMap;
    }
    @RequestMapping(value = "/value/toSave.do")
    public String toValueSave(){
        return "/settings/dictionary/value/save";
    }
    @RequestMapping(value = "/type/findTypeList.do")
    @ResponseBody
    public Map<String,Object> findTypeList(){
        List<DictionaryType> dictionaryTypeList=dictronaryService.findAllTypeList();
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code",10000);
        resultMap.put("msg","查询成功");
        resultMap.put("data",dictionaryTypeList);
        return resultMap;
    }
    @RequestMapping(value = "/value/saveDictionaryValue.do")
    @ResponseBody
    public Map<String,Object> saveDictionaryValue(DictionaryValue dictionaryValue) throws AjaxRequestException {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code",10000);
        resultMap.put("msg","查询成功");

        dictronaryService.saveDictionaryValue(dictionaryValue);
        return resultMap;
    }
    @RequestMapping(value = "/value/toEdit.do")
    public String toTypeEdit(String id,Model model) throws TranditionRequestException {
        DictionaryValue dictionaryValue=dictronaryService.findDictionaryValueById(id);
        if (dictionaryValue==null){
            throw new TranditionRequestException("查询异常");
        }
        model.addAttribute("dictionaryValue",dictionaryValue);
        return "/settings/dictionary/value/edit";
    }
    @RequestMapping(value = "/value/updateDictionaryValue.do")
    @ResponseBody
    public Map<String,Object> updateDictionaryValue(DictionaryValue dictionaryValue) throws AjaxRequestException {
        dictronaryService.updateDictionaryValue(dictionaryValue);
        Map<String,Object> resultMap = new HashMap<>();

        resultMap.put("code",10000);
        resultMap.put("msg","修改成功");

        return resultMap;

    }

}
