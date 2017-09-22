package com.chaonghong.data.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.chaonghong.data.crawler.SchedulerCrawlerVillage;
import com.chaonghong.data.entity.StreetOrTown;
import com.chaonghong.data.service.SchedulerService;

import us.codecraft.webmagic.Spider;

@Service
public class SchedulerServiceImpl extends SchedulerServiceSuper implements SchedulerService
{
    @Autowired
    protected MongoTemplate orionMongoTemplate;

    private ExecutorService service = Executors.newFixedThreadPool(64);

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // @Override
    // public void process() throws Exception
    // {
    // // 查询数据库相应的数据
    // Query[] query = {new Query(Criteria.where("has_execute").is("0")).limit(1),
    // new Query(Criteria.where("has_execute").is("110")).limit(1) };
    //
    // List<Provience> list = getQueryResult(Provience.class, query);
    //
    // Assert.notEmpty(list, "没有找到符合条件的基本信息---");
    //
    // String url = null;
    // Spider spider = null;
    // for (Provience provience : list)
    // {
    // url = provience.getProvience_url();
    // spider = Spider.create(new SchedulerCrawlerCity(orionMongoTemplate)).addUrl(url).thread(1);
    // spider.run();
    // spider.stop();
    // }
    //
    // updateStateInfo(list, "has_execute");
    //
    // }

    // @Override
    // public void process() throws Exception
    // {
    // // 查询数据库相应的数据
    // Query[] query = {new Query(Criteria.where("has_detail").is("0")).limit(1),
    // new Query(Criteria.where("has_detail").is("110")).limit(1) };
    //
    // List<City> list = getQueryResult(City.class, false, query);
    //
    // Assert.notEmpty(list, "没有找到符合条件的基本信息---");
    //
    // String url = null;
    // Spider spider = null;
    // for (City city : list)
    // {
    // url = city.getCity_url();
    // spider =
    // Spider.create(new SchedulerCrawlerCityDetail(orionMongoTemplate, url)).addUrl(url).thread(1);
    // spider.run();
    // spider.stop();
    // }
    //
    // updateStateInfo(list, "has_detail");
    //
    // }

    // @Override
    // public void process() throws Exception
    // {
    // // 查询数据库相应的数据
    // Query[] query = {new Query(Criteria.where("has_detail").is("0")).limit(10) };
    //
    // List<Area> list = getQueryResult(Area.class, false, query);
    //
    // Assert.notEmpty(list, "没有找到符合条件的基本信息---");
    //
    // String url = null;
    // for (Area area : list)
    // {
    // url = area.getArea_url();
    // service.submit(new InnerClass(orionMongoTemplate, url));
    // }
    //
    // updateStateInfo(list, "has_detail");
    //
    // }
    @Override
    public void process() throws Exception
    {
        // 查询数据库相应的数据
        Query[] query = {new Query(Criteria.where("has_detail").is("0")).limit(10) };

        List<StreetOrTown> list = getQueryResult(StreetOrTown.class, false, query);

        Assert.notEmpty(list, "没有找到符合条件的基本信息---");

        String url = null;
        for (StreetOrTown streetOrTown : list)
        {
            url = streetOrTown.getTown_url();
            service.submit(new InnerClass(orionMongoTemplate, url));
        }

        updateStateInfo(list, "has_detail");

    }

    /**
     * 内部类开启线程处理业务及时返回调度系统
     * 
     * @author CSY
     *
     */
    public class InnerClass implements Callable<String>
    {
        private MongoTemplate orionMongoTemplate;
        private String url;

        public InnerClass(MongoTemplate orionMongoTemplate, String url)
        {
            this.orionMongoTemplate = orionMongoTemplate;
            this.url = url;
        }

        @Override
        public String call() throws Exception
        {
            try
            {
                Spider spider = null;
				
				
                spider =
                    Spider.create(new SchedulerCrawlerVillage(orionMongoTemplate, url)).addUrl(url).thread(1);
                spider.run();
                spider.stop();
            }
            catch (Exception e)
            {
                StringBuffer sb = new StringBuffer(128).append("=====>>  Catch Exception ")
                    .append("At ")
                    .append(format.format(new Date()))
                    .append(" ,The error massage is : ");

                if (null == e.getMessage())
                {
                    sb.append("NullPointerException");
                }
                else
                {
                    sb.append(e.getMessage());
                }
                System.out.println(sb.toString());
            }
            return null;
        }
    }

}
