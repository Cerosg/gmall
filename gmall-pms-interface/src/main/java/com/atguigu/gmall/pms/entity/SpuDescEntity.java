package com.atguigu.gmall.pms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * spu信息介绍
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 19:34:59
 */
@Data
@AllArgsConstructor
@TableName("pms_spu_desc")
public class SpuDescEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(type = IdType.INPUT)
    private Long spuId;
    /**
     * 商品介绍
     */
    private String decript;
}
