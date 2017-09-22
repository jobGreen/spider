package com.chaonghong.data.crawler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.chaonghong.data.entity.Provience;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

@Component
public class SchedulerCrawler implements PageProcessor
{
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Autowired
    protected MongoTemplate orionMongoTemplate;

    @Override
    public Site getSite()
    {

        return site;
    }

    @Override
    public void process(Page page)
    {
        String dom = page.getHtml().$("[bgcolor=cccccc]").get();
        Html html = new Html(dom);
        List<String> list = html.$("tr").all();

        for (int i = 1; i < list.size(); i++)
        {
            // html = new Html(string);

            System.out.println(list.get(i));
            System.out.println("----------------");
        }
    }

    // @Override
    public void processs(Page page)
    {

        List<String> list = page.getHtml().$("a.blue").all();
        for (String string : list)
        {
            if (-1 != string.indexOf("href=\"/") && -1 != string.indexOf("blue\">"))
            {
                String url = string.substring(string.indexOf("href=\"/") + 7, string.indexOf("/\""));
                String name = string.substring(string.indexOf("blue\">") + 6, string.indexOf("</a>"));
                Provience p = new Provience();
                p.setProvience_name(name);
                p.setProvience_url("http://www.tcmap.com.cn/" + url);
                orionMongoTemplate.save(p);
            }
            // System.out.println("provience is :" + string);
        }
        page.putField("省份", page.getHtml().$("a.blue", "href"));
        // page.putField("城市", page.getHtml().$("span.J_City", "text").get());
        // page.putField("链接", page.getHtml().$("a.cover-bg seo-anchor-text", "href").get());
        // String html = page.getHtml().$("span.J_City", "text").get();
        // System.out.println(html);
    }

    public static void main(String[] args)
    {
        Spider spider =
            Spider.create(new SchedulerCrawler()).addUrl("http://www.tcmap.com.cn/sichuan/").thread(1);
        spider.run();
        spider.stop();
    }
}
