package com.github.hugh.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.hugh.db.model.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}
