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
import org.springframework.stereotype.Service;

import com.chaonghong.data.entity.Area;
import com.chaonghong.data.entity.City;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

@Service
public class SchedulerCrawlerCityDetail implements PageProcessor
{

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public SchedulerCrawlerCityDetail(MongoTemplate orionMongoTemplate, String url)
    {
        this.orionMongoTemplate = orionMongoTemplate;
        this.city_url = url;
    }

    public SchedulerCrawlerCityDetail()
    {
    }

    protected MongoTemplate orionMongoTemplate;
    private String city_name;
    private String provience_name;
    private String city_url;

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

        addCityFiled(doc);
        findArea(doc);
        // 找区域或者县城

    }

    private void findArea(Document doc)
    {
        Element table = doc.select("table[cellpadding=2]").first();
        Elements trs = table.select("tr");
        Element a = null;
        Area area = null;
        for (int i = 1; i < trs.size(); i++)
        {
            area = new Area();
            a = trs.get(i).select("td").get(0).select("a").first();
            area.setProvience_name(provience_name);
            area.setCity_name(city_name);
            area.setArea_name(a.text());
            area.setArea_url("http://www.tcmap.com.cn" + a.attr("href"));
            area.setAdministrative_code(trs.get(i).select("td").get(1).text());
            area.setArea_acreage(trs.get(i).select("td").get(2).text());

            orionMongoTemplate.save(area);
        }
    }

    /**
     * 补全city信息
     * 
     * @param doc
     */
    private void addCityFiled(Document doc)
    {
        // 补全详细信息
        Element table = doc.select("table[width=396px]").first();
        Elements trs = table.select("tr[bgcolor=#ffffff]");

        String str = null;
        String city_name = null;
        String provience_name = null;
        String car_number = null;
        String administrative_level = null;
        String person_density = null;
        String administrative_site = null;
        List<Map<String, Object>> list = null;
        for (Element element : trs)
        {
            str = element.select("td").first().text();
            if (str.indexOf("地名") != -1)
            {
                city_name = element.select("td").get(0).text().replace("地名", "").replace(":", "").trim();
                this.city_name = city_name;
                Element e = element.select("td").get(1);
                provience_name = e.select("a").text();
                this.provience_name = provience_name;
            }
            else if (str.indexOf("车牌号码") != -1)
            {
                Element e31 = element.select("td").get(0);
                car_number = e31.text();
                Element e32 = element.select("td").get(1);
                administrative_level = e32.text();

            }
            else if (str.indexOf("人口数量") != -1)
            {
                Element e42 = element.select("td").get(1);
                person_density = e42.text();

            }
            else if (str.indexOf("行政驻地") != -1)
            {

                Element e6 = element.select("td").get(0);
                administrative_site = e6.text();
            }
            else if (str.indexOf("网站") != -1)
            {
                Elements tag = element.select("a");
                list = new ArrayList<>();
                Map<String, Object> map = null;
                for (Element e : tag)
                {
                    map = new HashMap<>();
                    e.attr("href");
                    e.attr("text");
                    map.put(e.text(), e.attr("href"));
                    list.add(map);
                }

            }

        }

        City city =
            orionMongoTemplate.findOne(new Query(Criteria.where("city_url").is(city_url)), City.class);

        city.setAdministrative_level(administrative_level);
        city.setAdministrative_site(administrative_site);
        city.setCar_number(car_number);
        city.setPerson_density(person_density);
        city.setProvience_name(provience_name);
        city.setRefernce_url(list);
        orionMongoTemplate.save(city);
    }

}
