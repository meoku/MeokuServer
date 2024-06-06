package com.upgrade.meoku.menuOrder;

import com.upgrade.meoku.exception.MeokuException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MeokuMenuOrderScheduledTasks {

    private final MeokuMealOrderService orderService;

    @Autowired
    public MeokuMenuOrderScheduledTasks(MeokuMealOrderService orderService) {
        this.orderService = orderService;
    }

    //@Scheduled(cron = "0 0 0 * * MON") // 매주 월요일 00시 00분 배식순서 변경
    public void executeWeeklyTask() throws MeokuException {
        orderService.saveWeeklyMealOrderDataByLatestData();
    }
}
