package com.atguigu.gmall.pms.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月07日 09时41分
 */
@Data
public class ItemGroupVo {
    private Long groupId;
    private String groupName;
    private List<AttrValueVo> attrs;
}
