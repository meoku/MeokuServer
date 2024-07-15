package com.upgrade.meoku.mealmenu.data.dto;


import lombok.Data;
import java.sql.Timestamp;

@Data
public class SubMenuTagDTO {
    private Integer menuTagId;

    private Integer menuItemId;
    private SubMenuItemDTO subMenuItemDTO;
    private String menuTagName;
    private Timestamp tagEndDate;
}
