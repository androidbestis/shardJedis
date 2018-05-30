package com.gupo.dao;

import com.gupo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserDao {
	User selectByPrimaryKey(Integer id);
}
