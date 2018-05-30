package com.gupo.service;

import com.alibaba.fastjson.JSON;
import com.gupo.config.DS;
import com.gupo.consts.DbAndCacheContants;
import com.gupo.dao.MoredataDao;
import com.gupo.dao.UserDao;
import com.gupo.entity.User;
import com.gupo.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * service层
 * Created by pure on 2018-05-06.
 */
@Service
public class MoredataService {
    @Autowired
    private MoredataDao moredataDao;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserDao userDao;

    //使用数据源1查询
    @DS("datasource1")
    public List<Map> getAllUser1(){
        return moredataDao.getAllUser();
    }
    //使用数据源2查询
    @DS("datasource2")
    public List<Map> getAllUser2(){
        return moredataDao.getAllUser();
    }

    //使用数据源1插入数据
    @DS("datasource1")
    public Long addUserGetID1(User user){
        return moredataDao.addUserGetID(user);
    }
    //使用数据源1插入数据
    @DS("datasource2")
    public Long addUserGetID2(User user){
        return moredataDao.addUserGetID(user);
    }

    //使用数据源1插入数据
    @DS("datasource1")
    public void addUser1(String name){
        moredataDao.addUser(name);
    }
    //使用数据源2插入数据
    @DS("datasource2")
    public void addUser2(String name){
        moredataDao.addUser(name);
    }

    /**
     * Shard Redis 测试
     */
    @DS("datasource1")
    public User getUser(Integer id){
      //第二次从缓存读取数据
      String userStr = redisUtil.get(DbAndCacheContants.USER_CACHE_PREFIX + id);
      if(StringUtils.isNotBlank(userStr)){
        return JSON.parseObject(userStr,User.class);
      }
      //第一次缓存没有数据,从数据库查询
      User user = userDao.selectByPrimaryKey(id);
      if(user != null){
        redisUtil.set(DbAndCacheContants.USER_CACHE_PREFIX + id , JSON.toJSONString(user));
      }
      return user;
    }
}