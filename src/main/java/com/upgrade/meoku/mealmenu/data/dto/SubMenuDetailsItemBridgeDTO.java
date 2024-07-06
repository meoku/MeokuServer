package com.upgrade.meoku.mealmenu.data.dto;

import lombok.Data;

@Data
public class SubMenuDetailsItemBridgeDTO {
    private Integer bridgeId;

    private Integer menuDetailsId;
    private Integer menuItemId;
    private String menuItemName;

    private SubMenuDetailsDTO SubmenuDetails;
    private SubMenuItemDTO SubmenuItem;
}
