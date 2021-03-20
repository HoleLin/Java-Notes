package com.holelin.redis.service;

import org.springframework.stereotype.Service;
/**
* @Description:
* @Author:         HoleLin
* @CreateDate:     2020/11/18 15:23
* @UpdateUser:     HoleLin
* @UpdateDate:     2020/11/18 15:23
* @UpdateRemark:   修改内容

* @Version:        1.0
*/

public interface RedisLockService {
    /**
     * 开始任务
     * @param key 键
     * @return
     */
    String startTask(String key);
    

}
