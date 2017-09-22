package com.chaonghong.data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class SchedulerServiceSuper
{

    @Autowired
    protected MongoTemplate orionMongoTemplate;

    protected <T extends Object> List<T> getQueryResult(Class<T> cla, Query... query)
    {
        return getQueryResult(cla, true, query);
    }

    /**
     * 查询数据库结果
     * 
     * @param query 查询表达式
     * @param t 泛型
     * @return
     */
    protected <T extends Object> List<T> getQueryResult(Class<T> cla, boolean is110, Query... query)
    {
        List<T> list = null;

        list = orionMongoTemplate.find(query[0], cla);

        if (is110 && 0 == list.size())
        {
            list = orionMongoTemplate.find(query[1], cla);
        }

        return list;
    }

    /**
     * 修改状态信息
     * 
     * @param list 需要修改状态的数据库对象
     * @param state 需要修改的状态码
     * @throws Exception
     */
    protected <T> void updateStateInfo(List<T> list, String state) throws Exception
    {
        StringBuffer sb = new StringBuffer(50);
        sb.append("get").append(state);
        sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));

        Object o = list.get(0).getClass().getMethod(sb.toString(), null).invoke(list.get(0), null);

        if ("0".equals(o.toString()))
            updateState(list, state, "110");
        else if ("110".equals(o.toString()))
            updateState(list, state, "-1");
    }

    /**
     * 修改状态为110
     * 
     * @param list 需要修改状态的数据库对象
     * @param state 需要修改的状态码
     * @throws Exception
     */
    protected <T> void updateState(List<T> list, String state, String value) throws Exception
    {
        String id = null;
        for (T t : list)
        {
            id = t.getClass().getMethod("getId", null).invoke(t, null).toString();
            // 修改状态
            orionMongoTemplate.updateMulti(new Query(Criteria.where("_id").is(id)),
                new Update().set(state, value),
                t.getClass());
        }
    }
}
