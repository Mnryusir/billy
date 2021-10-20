package com.yuzhi.crm.workbench.dao;

import com.yuzhi.crm.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityDao {

    List<Activity> findActivityList();

    List<Activity> findActivityListByPage(@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    Long findTotalCount();

    List<Activity> findActivityListCondition( @Param("pageNo")int pageNoIndex,
                                              @Param("pageSize")Integer pageSize,
                                              @Param("activityName")String activityName,
                                              @Param("owner")String owner,
                                              @Param("startDate")String startDate,
                                              @Param("endDate")String endDate);

    Long findTotalCountCondition(@Param("activityName")String activityName,
                                 @Param("owner")String owner,
                                 @Param("startDate")String startDate,
                                 @Param("endDate")String endDate);

    int save(Activity activity);

    Activity findById(String id);

    int updateById(Activity activity);

    int saveActivityList(List<Activity> arrayList);
}
