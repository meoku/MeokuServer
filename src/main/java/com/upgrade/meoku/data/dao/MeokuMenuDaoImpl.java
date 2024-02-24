package com.upgrade.meoku.data.dao;

import com.upgrade.meoku.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MeokuMenuDaoImpl {

    private MeokuDailyMenuRepository dailyMenuRepository;
    private MeokuDetailedMenuRepository detailedMenuRepository;
    private MeokuMenuDetailRepository menuDetailRepository;
    private MeokuMenuTagsRepository menuTagsRepository;
    private MeokuMenuAllergiesRepository menuAllergiesRepository;

    @Autowired
    public MeokuMenuDaoImpl(MeokuDailyMenuRepository dailyMenuRepository,
                            MeokuDetailedMenuRepository detailedMenuRepository,
                            MeokuMenuDetailRepository menuDetailRepository,
                            MeokuMenuTagsRepository menuTagsRepository,
                            MeokuMenuAllergiesRepository menuAllergiesRepository){
        this.dailyMenuRepository = dailyMenuRepository;
        this.detailedMenuRepository = detailedMenuRepository;
        this.menuDetailRepository = menuDetailRepository;
        this.menuTagsRepository = menuTagsRepository;
        this.menuAllergiesRepository = menuAllergiesRepository;
    }
}

