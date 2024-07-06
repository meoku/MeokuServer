package com.upgrade.meoku.mealmenu.data.dto;

import com.upgrade.meoku.mealmenu.data.entity.SubMenuDetailsItemBridge;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubMenuItemDTO {
    private Integer menuItemId;

    private Integer recentMenuDetailsId;
    private String recentMenuDetailsUrl;
    private String menuItemName;
    private Integer frequencyCnt;
    private Integer likesCnt;
    private Integer calories;

    private List<SubMenuDetailsItemBridgeDTO> subBridgeList = new ArrayList<>();
}
