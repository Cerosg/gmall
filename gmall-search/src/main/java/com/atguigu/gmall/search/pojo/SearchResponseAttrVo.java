package com.atguigu.gmall.search.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月03日 08时33分
 */
@Data
public class SearchResponseAttrVo {
    private Long attrId;
    private String attrName;
    private List<String> attrValues;
}
