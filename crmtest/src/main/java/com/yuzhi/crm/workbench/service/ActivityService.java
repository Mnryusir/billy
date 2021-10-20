package com.yuzhi.crm.workbench.service;

import com.yuzhi.crm.exception.AjaxRequestException;
import com.yuzhi.crm.exception.TranditionRequestException;
import com.yuzhi.crm.workbench.domain.Activity;

import java.util.List;

public interface ActivityService {
    //Deprecated注解表示当前方法已过期（未分页方法已过期）
    @Deprecated
    List<Activity> findActivityList();

    List<Activity> findActivityList(Integer pageNo, Integer pageSize);

    Long findTotalCount();

    List<Activity> findActivityListCondition(int pageNoIndex, Integer pageSize, String activityName, String owner, String startDate, String endDate);

    Long findTotalCountCondition(String activityName, String owner, String startDate, String endDate);

    void saveActivity(Activity activity, String createBy, String creatTime) throws AjaxRequestException;

    Activity findById(String id);

    void updateById(Activity activity) throws AjaxRequestException;

    void saveActivityList(List<Activity> arrayList) throws TranditionRequestException;
}
