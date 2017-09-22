package com.chaonghong.data.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chaonghong.data.crawler.SchedulerCrawler;
import com.chaonghong.data.entity.BaseRes;
import com.chaonghong.data.service.SchedulerService;

import us.codecraft.webmagic.Spider;

@RestController
@RequestMapping("/rest/boya")
public class HouseInfoCollection
{

    @Autowired
    protected MongoTemplate orionMongoTemplate;

    private ExecutorService service = Executors.newFixedThreadPool(64);

    @Autowired
    SchedulerCrawler schedulerCrawler;

    @Autowired
    SchedulerService schedulerService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Logger logger = LoggerFactory.getLogger(HouseInfoCollection.class);

    @RequestMapping("/infos")
    public BaseRes getHouseInfos(HttpServletResponse response)
    {
        response.setHeader("content-disposition", "attachment;filename=" + "file.xlsx");

        BaseRes br = new BaseRes();
        br.setError(0);
        br.setMsg("SUCCESS");
        try
        {
            Spider spider = Spider.create(schedulerCrawler).addUrl("http://www.tcmap.com.cn/").thread(1);
            spider.run();
            spider.stop();
        }
        catch (Exception e)
        {
            logger.error(format.format(new Date()) + "  ====== {} ", e.getMessage());
            br.setError(1);
            br.setMsg(e.getMessage());
        }
        return br;
    }

    @RequestMapping("/citys")
    public BaseRes citys()
    {

        BaseRes br = new BaseRes();
        br.setError(0);
        br.setMsg("SUCCESS");
        try
        {

            service.submit(new InnerClass(schedulerService));

        }
        catch (Exception e)
        {
            logger.error(format.format(new Date()) + "  ====== {} ", e.getMessage());
            br.setError(1);
            br.setMsg(e.getMessage());
        }
        return br;
    }

    /**
     * 内部类开启线程处理业务及时返回调度系统
     * 
     * @author CSY
     *
     */
    public class InnerClass implements Callable<String>
    {
        private SchedulerService schedulerService;

        public InnerClass(SchedulerService schedulerService)
        {
            this.schedulerService = schedulerService;
        }

        @Override
        public String call() throws Exception
        {
            try
            {
                schedulerService.process();
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
