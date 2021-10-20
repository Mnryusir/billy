package com.yuzhi.crm.workbench.service.Impl;

import com.yuzhi.crm.exception.AjaxRequestException;
import com.yuzhi.crm.exception.TranditionRequestException;
import com.yuzhi.crm.utils.UUIDUtil;
import com.yuzhi.crm.workbench.dao.ActivityDao;
import com.yuzhi.crm.workbench.domain.Activity;
import com.yuzhi.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
    @Override
    public List<Activity> findActivityList() {
        return activityDao.findActivityList();
    }

    @Override
    public List<Activity> findActivityList(Integer pageNo, Integer pageSize) {
        return activityDao.findActivityListByPage(pageNo,pageSize);
    }

    @Override
    public Long findTotalCount() {
        return activityDao.findTotalCount();
    }

    @Override
    public List<Activity> findActivityListCondition(int pageNoIndex, Integer pageSize, String activityName, String owner, String startDate, String endDate) {
        return activityDao.findActivityListCondition(pageNoIndex,pageSize,activityName,owner,startDate,endDate);
    }

    @Override
    public Long findTotalCountCondition(String activityName, String owner, String startDate, String endDate) {
        return activityDao.findTotalCountCondition(activityName,owner,startDate,endDate);
    }

    @Override
    public void saveActivity(Activity activity, String createBy, String creatTime) throws AjaxRequestException {

        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(creatTime);
        activity.setCreateBy(createBy);
        int count=activityDao.save(activity);
        if (count<=0){
            throw new AjaxRequestException("新增失败");

        }

    }

    @Override
    public Activity findById(String id) {
        System.out.println("=========");
        System.out.println(id);
        return activityDao.findById(id);

    }

    @Override
    public void updateById(Activity activity) throws AjaxRequestException {
        int count=activityDao.updateById(activity);
        if (count<=0){
            throw new AjaxRequestException("更新失败");

        }
    }

    @Override
    public void saveActivityList(List<Activity> arrayList) throws TranditionRequestException {
        int count=activityDao.saveActivityList(arrayList);
        if (count<0){
            throw new TranditionRequestException("批量导入失败");
        }
    }
}
