package com.yuzhi.crm.workbench.web.controller;

import com.yuzhi.crm.common.BaseResultEntity;
import com.yuzhi.crm.common.BaseResultPageEntity;
import com.yuzhi.crm.exception.AjaxRequestException;
import com.yuzhi.crm.exception.TranditionRequestException;
import com.yuzhi.crm.settings.domain.User;
import com.yuzhi.crm.utils.DateTimeUtil;
import com.yuzhi.crm.utils.UUIDUtil;
import com.yuzhi.crm.workbench.domain.Activity;
import com.yuzhi.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.swing.text.Utilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/workbench/activity")
public class ActivityController {
@Autowired
private ActivityService activityService;

    @RequestMapping(value = "/toIndex.do")
    public String toIndex(){

        return "/workbench/activity/index";
    }
    /*@RequestMapping(value = "/findActivityList.do")
    @ResponseBody
    public Map<String,Object> findActivityList(){
        List<Activity> activityList=activityService.findActivityList();
        HashMap<String, Object> resultMap = new HashMap<>();
        if(activityList != null) {
            resultMap.put("code", 10000);
            resultMap.put("msg", "查询成功");
            resultMap.put("data",activityList);
            return resultMap;
        }else {
            resultMap.put("code", 10001);
            resultMap.put("msg", "当前无市场活动数据");
            resultMap.put("data",null);
            return resultMap;
        }

    }*/
/*    @RequestMapping(value = "/findActivityList.do")
    @ResponseBody
    public BaseResultPageEntity<Activity> findActivityList(Integer pageNo, Integer pageSize){
        //计算分页查询的起始索引
        //第一页数据起始索引从0开始
        //起始索引=(pageNo-1)*pageSize  0  2  4  6  8  10
        int pageNoIndex=(pageNo-1)*pageSize;
        List<Activity> activityList=activityService.findActivityList(pageNoIndex,pageSize);
       // HashMap<String, Object> resultMap = new HashMap<>();
        if(activityList != null) {
            *//*resultMap.put("code", 10000);
            resultMap.put("msg", "查询成功");
            resultMap.put("data",activityList);
            return resultMap;*//*
            return BaseResultPageEntity.ok(10000,"查询成功",activityList);
        }else {
            *//*resultMap.put("code", 10001);
            resultMap.put("msg", "当前无市场活动数据");
            resultMap.put("data",null);
            return resultMap;*//*
            return BaseResultPageEntity.error("当前无市场活动数据");
        }

    }*/
//    @RequestMapping(value = "/findActivityList.do")
//    @ResponseBody
//    public BaseResultPageEntity<Activity> findActivityList(Integer pageNo, Integer pageSize){
//        //计算分页查询的起始索引
//        //第一页数据起始索引从0开始
//        //起始索引=(pageNo-1)*pageSize  0  2  4  6  8  10
//        int pageNoIndex=(pageNo-1)*pageSize;
//        List<Activity> activityList=activityService.findActivityList(pageNoIndex,pageSize);
//        //查询总记录数
//        Long totalCount=activityService.findTotalCount();
//        //根据总记录数计算出总页数
//        //根据当前数剧是否能整除，返回整除后数据或不能整除的数据+1的结果
//        Long totalPages = totalCount % pageSize == 0 ? totalCount / pageSize:totalCount / pageSize +1;
//        // HashMap<String, Object> resultMap = new HashMap<>();
//        if(activityList != null) {
//            /*resultMap.put("code", 10000);
//            resultMap.put("msg", "查询成功");
//            resultMap.put("data",activityList);
//            return resultMap;*/
//            return BaseResultPageEntity.ok
//                    (10000,"查询成功",activityList,
//                            pageNo,pageSize,totalPages,totalCount);
//        }else {
//            /*resultMap.put("code", 10001);
//            resultMap.put("msg", "当前无市场活动数据");
//            resultMap.put("data",null);
//            return resultMap;*/
//            return BaseResultPageEntity.error("当前无市场活动数据");
//        }
//}
    @RequestMapping(value = "/findActivityList.do")
    @ResponseBody
    public BaseResultPageEntity<Activity> findActivityList(Integer pageNo, Integer pageSize,String activityName,
                                                           String owner,
                                                           String startDate,
                                                           String endDate){
        //计算分页查询的起始索引
        //第一页数据起始索引从0开始
        //起始索引=(pageNo-1)*pageSize  0  2  4  6  8  10
        int pageNoIndex=(pageNo-1)*pageSize;
//        List<Activity> activityList=activityService.findActivityList(pageNoIndex,pageSize);
        //条件过滤查询
        List<Activity> activityList = activityService.findActivityListCondition(
                pageNoIndex,
                pageSize,
                activityName,
                owner,
                startDate,
                endDate);
        //查询总记录数
//        Long totalCount=activityService.findTotalCount();
        Long totalCount=activityService.findTotalCountCondition(
                activityName,
                owner,
                startDate,
                endDate);
        //根据总记录数计算出总页数
        //根据当前数剧是否能整除，返回整除后数据或不能整除的数据+1的结果
        Long totalPages = totalCount % pageSize == 0 ? totalCount / pageSize:totalCount / pageSize +1;
        // HashMap<String, Object> resultMap = new HashMap<>();
        if(activityList != null) {
            /*resultMap.put("code", 10000);
            resultMap.put("msg", "查询成功");
            resultMap.put("data",activityList);
            return resultMap;*/
            return BaseResultPageEntity.ok
                    (10000,"查询成功",activityList,
                            pageNo,pageSize,totalPages,totalCount);
        }else {
            /*resultMap.put("code", 10001);
            resultMap.put("msg", "当前无市场活动数据");
            resultMap.put("data",null);
            return resultMap;*/
            return BaseResultPageEntity.error("当前无市场活动数据");
        }
    }

    @RequestMapping(value = "/save.do")
    @ResponseBody
    public BaseResultPageEntity save(Activity activity,HttpSession session) throws AjaxRequestException {
        String createBy=((User)session.getAttribute("user")).getName();
        String creatTime= DateTimeUtil.getSysTime();
        System.out.println(createBy);
        System.out.println(creatTime);
        activityService.saveActivity(activity,createBy,creatTime);
        return BaseResultPageEntity.ok();
    }
    @RequestMapping(value = "/findById.do")
    @ResponseBody
    public BaseResultEntity<Activity> findById(String id) throws AjaxRequestException {
        Activity activity=activityService.findById(id);
        System.out.println("++++++++");
        System.out.println(id);
        if (activity==null){
            throw new AjaxRequestException("查询失败");
        }
        return BaseResultEntity.ok(10000,"查询成功",activity);
    }
    @RequestMapping(value = "/updateById.do")
    @ResponseBody
    public BaseResultEntity updateById(Activity activity,HttpSession session) throws AjaxRequestException {
        String editBy = ((User) session.getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        activity.setEditBy(editBy);
        activity.setEditTime(editTime);
        activityService.updateById(activity);
        return BaseResultEntity.ok();
    }
    @RequestMapping(value = "/importActivityFile.do")
    public String importActivityFile(MultipartFile activityFile,HttpSession session) throws TranditionRequestException, IOException {
            String filename=activityFile.getOriginalFilename();
            Integer index=filename.lastIndexOf(".")+1;
            String suffix=filename.substring(index);
        if (!suffix.equals("xls")&&!suffix.equals("xlsx")){
            throw new TranditionRequestException("文件上传错误，请重新上传");
        }
        String newFileName=DateTimeUtil.getSysTimeForUpload()+".xls";
        String path="D:\\shangchuan";
        activityFile.transferTo(new File(path+"/"+newFileName));
        //将内存文件转换为workbook对象
        InputStream in=new FileInputStream(new File(path+"/"+newFileName));
        //根据输入流向创建工作簿对象
        HSSFWorkbook workbook=new HSSFWorkbook(in);
        //根据工作薄对象获取第一页对象
        HSSFSheet sheet= workbook.getSheetAt(0);
        //根据第一页对象获取最后的行号
        int lastRowNum = sheet.getLastRowNum();
        List<Activity> arrayList=new ArrayList<>();
        String owner = ((User) session.getAttribute("user")).getId();

        String createBy = ((User) session.getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        for (int i = 1; i <=lastRowNum; i++) {
            //创建行对象，从第二行开始获取数据，第一行是表头无意义，因此i=1
            HSSFRow row=sheet.getRow(i);
            //获取行中的单元格数据，提供的数据共五个单元格
            String name=row.getCell(0).getStringCellValue();
            String startDate=row.getCell(1).getStringCellValue();
            String endDate=row.getCell(2).getStringCellValue();
            double cost=row.getCell(3).getNumericCellValue();
            String description=row.getCell(4).getStringCellValue();
           // System.out.println(name+" "+startDate+" "+endDate+" "+cost+" "+description);
            Activity activity=new Activity();
            activity.setName(name);
            activity.setStartDate(startDate);
            activity.setEndDate(endDate);
            activity.setCost(String.valueOf(cost));
            activity.setDescription(description);
            activity.setId(UUIDUtil.getUUID());
            activity.setOwner(owner);
            activity.setCreateBy(createBy);
            activity.setCreateTime(createTime);
        }
            activityService.saveActivityList(arrayList);
        return  "redirect:/workbench/activity/toIndex.do";
    }

 }
