package cn.chennan.qqpetfight.activity.controller;

import cn.chennan.qqpetfight.activity.service.ActivityService;
import cn.chennan.qqpetfight.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cn
 * @date 2022-07-10 18:02
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/common/list/run")
    public Result<Void> commonList() {
        activityService.runList();
        return Result.success(null);
    }

    @GetMapping("/self/list/run")
    public Result<Void> selfList() {
        activityService.selfRunList();
        return Result.success(null);
    }

}
