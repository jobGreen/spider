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

import com.chaonghong.data.entity.Area;
import com.chaonghong.data.entity.StreetOrTown;
import com.chaonghong.data.entity.Village;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class SchedulerCrawlerAreaDetail implements PageProcessor
{
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
    protected MongoTemplate orionMongoTemplate;
    private String city_name;
    private String provience_name;
    private String area_url;
    private String area_name;

    @Override
    public Site getSite()
    {

        return site;
    }

    public SchedulerCrawlerAreaDetail()
    {
    }

    public SchedulerCrawlerAreaDetail(MongoTemplate orionMongoTemplate, String url)
    {
        this.orionMongoTemplate = orionMongoTemplate;
        this.area_url = url;
    }

    @Override
    public void process(Page page)
    {
        String dom = page.getHtml().get();
        Document doc = Jsoup.parse(dom);
        addAreaField(doc);
        findStreet(doc);
    }

    private void addAreaField(Document doc)
    {
        Element table = doc.select("table[cellpadding=1]").first();
        Elements trs = table.select("tr[bgcolor=#ffffff]");

        String area_number = null;
        String car_number = null;
        String person_density = null;
        String administrative_site = null;
        String administrative_level = null;
        String area_population = null;
        List<Map<String, Object>> refernce_url = null;

        String str = null;
        for (Element element : trs)
        {
            str = element.select("td").first().text();

            if (str.indexOf("长途区号") != -1 || str.indexOf("电话") != -1)
            {
                area_number = element.select("td").get(0).text();
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
            else if (str.indexOf("行政驻地") != -1 || str.indexOf("办事处地") != -1)
            {
                administrative_site = element.select("td").get(0).text();
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

        Area area =
            orionMongoTemplate.findOne(new Query(Criteria.where("area_url").is(area_url)), Area.class);
        this.provience_name = area.getProvience_name();
        this.city_name = area.getCity_name();
        this.area_name = area.getArea_name();
        area.setAdministrative_level(administrative_level);
        area.setAdministrative_site(administrative_site);
        area.setArea_population(area_population);
        area.setRefernce_url(refernce_url);
        area.setCar_number(car_number);
        area.setPerson_density(person_density);
        area.setArea_number(area_number);
        orionMongoTemplate.save(area);
    }

    private void findStreet(Document doc)
    {

        Element table = doc.select("table[cellpadding=2]").first();
        if (null != table)
        {
            Elements trs = table.select("tr");
            Element a = null;
            StreetOrTown area = null;
            for (int i = 1; i < trs.size(); i++)
            {
                area = new StreetOrTown();
                // 获取第一个td中的信息
                if (null != trs.get(i).select("td[align=center]"))
                {
                    a = trs.get(i).select("td[align=center]").select("a").first();
                    area.setTown_name(a.text());
                    area.setTown_url("http://www.tcmap.com.cn" + a.attr("href"));
                }

                area.setProvience_name(provience_name);
                area.setCity_name(city_name);
                area.setArea_name(area_name);

                orionMongoTemplate.save(area);
            }
        }
        else
        {

            Element div = doc.select("div.f12").first();

            Village village = null;
            if (null != div)
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
                        village.setTown_name("直属居委会");

                        orionMongoTemplate.save(village);

                    }
                }

            }
        }
    }

    public static void main(String[] args)
    {
        Spider sipder = null;
        sipder = Spider.create(new SchedulerCrawlerAreaDetail())
            .addUrl("http://www.tcmap.com.cn/beijing/dongchengqu_donghuamenjiedao.html")
            .thread(1);
        sipder.run();
        sipder.stop();

    }
}
