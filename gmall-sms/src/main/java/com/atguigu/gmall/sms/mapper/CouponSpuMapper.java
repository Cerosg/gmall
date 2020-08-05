package com.atguigu.gmall.sms.mapper;

import com.atguigu.gmall.sms.entity.CouponSpuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券与产品关联
 * 
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 20:51:21
 */
@Mapper
public interface CouponSpuMapper extends BaseMapper<CouponSpuEntity> {
	
}
