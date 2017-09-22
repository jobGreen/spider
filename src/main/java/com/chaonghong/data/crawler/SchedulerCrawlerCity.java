package com.chaonghong.data.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.chaonghong.data.entity.City;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class SchedulerCrawlerCity implements PageProcessor
{
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public SchedulerCrawlerCity(MongoTemplate orionMongoTemplate)
    {
        this.orionMongoTemplate = orionMongoTemplate;
    }

    protected MongoTemplate orionMongoTemplate;

    @Override
    public Site getSite()
    {

        return site;
    }

    @Override
    public void process(Page page)
    {
        String dom = page.getHtml().get();
        Document doc = Jsoup.parse(dom);
        Elements docc = doc.select("table[bgcolor=cccccc]").select("tr[bgcolor!=eeeeee]");
        Element element = null;

        for (int i = 1; i < docc.size(); i++)
        {
            element = docc.get(i);
            City city = new City();

            city.setCity_url(
                "http://www.tcmap.com.cn" + element.select("td").get(0).select("a").first().attr("href"));
            city.setCity_acreage(element.select("td").get(2).text());
            city.setCity_area_number(element.select("td").get(4).text());
            city.setCity_code(element.select("td").get(3).text());
            city.setCity_population(element.select("td").get(1).text());
            city.setCity_name(element.select("strong").select("a").first().text());
            orionMongoTemplate.save(city);

        }

    }

}
