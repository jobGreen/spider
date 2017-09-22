package com.chaonghong.data.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.chaonghong.data.entity.StreetOrTown;
import com.chaonghong.data.entity.Village;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class SchedulerCrawlerVillage implements PageProcessor
{
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
    protected MongoTemplate orionMongoTemplate;
    private String city_name;
    private String provience_name;
    private String area_name;
    private String town_url;
    private String town_name;

    @Override
    public Site getSite()
    {

        return site;
    }

    public SchedulerCrawlerVillage()
    {
    }

    public SchedulerCrawlerVillage(MongoTemplate orionMongoTemplate, String url)
    {
        this.orionMongoTemplate = orionMongoTemplate;
        this.town_url = url;
    }

    @Override
    public void process(Page page)
    {
        String dom = page.getHtml().get();
        Document doc = Jsoup.parse(dom);
        addTownField(doc);
        findVillage(doc);
    }

    private void addTownField(Document doc)
    {
        Element table = doc.select("table[cellpadding=1]").first();
        Elements trs = table.select("tr[bgcolor=#ffffff]");

        String administrative_code = null;
        String area_acreage = null;
        String area_code = null;
        String car_number = null;
        String person_density = null;
        String administrative_level = null;
        String area_population = null;
        List<Map<String, Object>> refernce_url = null;

        String str = null;
        for (Element element : trs)
        {
            str = element.select("td").first().text();

            if (str.indexOf("行政代码") != -1)
            {
                administrative_code = element.select("td").get(0).text();
            }
            else if (str.indexOf("长途区号") != -1 || str.indexOf("电话") != -1)
            {
                area_code = element.select("td").get(0).text();
            }
            else if (str.indexOf("车牌号码") != -1)
            {
                car_number = element.select("td").get(0).text();
                administrative_level = element.select("td").get(1).text();
            }
            else if (str.indexOf("人口数量") != -1)
            {
                area_population = element.select("td").get(0).text();
                person_density = element.select("td").get(1).text();
            }
            else if (str.indexOf("面积") != -1)
            {
                area_acreage = element.select("td").get(0).text();
            }
            else if (str.indexOf("相关网站") != -1)
            {
                Elements tag = element.select("a");
                refernce_url = new ArrayList<>();
                Map<String, Object> map = null;
                for (Element e : tag)
                {
                    map = new HashMap<>();
                    e.attr("href");
                    e.attr("text");
                    map.put(e.text(), e.attr("href"));
                    refernce_url.add(map);
                }
            }
        }

        StreetOrTown town = orionMongoTemplate.findOne(new Query(Criteria.where("town_url").is(town_url)),
            StreetOrTown.class);

        this.provience_name = town.getProvience_name();
        this.city_name = town.getCity_name();
        this.area_name = town.getArea_name();
        this.town_name = town.getTown_name();

        town.setArea_acreage(area_acreage);
        town.setAdministrative_code(administrative_code);
        town.setArea_code(area_code);
        town.setAdministrative_level(administrative_level);
        town.setArea_population(area_population);
        town.setRefernce_url(refernce_url);
        town.setCar_number(car_number);
        town.setPerson_density(person_density);
        orionMongoTemplate.save(town);
    }

    private void findVillage(Document doc)
    {

        Element div = doc.select("div.f12").first();

        Village village = null;
        if (null != div)
        {
            if (div.text().indexOf("纠错") == -1)
            {

                String html = div.html();
                String[] temp = html.split("<br>");
                for (String string : temp)
                {
                    if (string.indexOf("<a>") == -1)
                    {
                        village = new Village();
                        for (String str : string.split(" "))
                        {
                            if (str.length() > 3)
                            {
                                if (str.matches("[0-9]+"))
                                {
                                    village.setAdministrative_code(str);
                                }
                                else
                                {
                                    village.setVillage_name(str);
                                }
                            }
                        }
                        village.setProvience_name(provience_name);
                        village.setCity_name(city_name);
                        village.setArea_name(area_name);
                        village.setTown_name(town_name);

                        orionMongoTemplate.save(village);

                    }
                }
            }
        }
    }

    public static void main(String[] args)
    {
        Spider sipder = null;
        sipder = Spider.create(new SchedulerCrawlerVillage())
            .addUrl("http://www.tcmap.com.cn/sichuan/luojiangxian_wananzhen.html")
            .thread(1);
        sipder.run();
        sipder.stop();

    }

}
