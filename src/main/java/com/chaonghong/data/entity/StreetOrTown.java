package com.chaonghong.data.entity;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "boya_china_town_basic_info")
public class StreetOrTown
{
    @Id
    private String id;
    private String provience_name; // 省份名称
    private String city_name;// 市级名称
    private String area_name;// 区县名称
    private String town_name;// 街道或镇
    private String town_url;// 街道或镇的url
    private String administrative_code;// 行政代码
    private String area_acreage;// 区域面积
    private String has_detail = "0";// 是否爬取
    private String area_code; // 长途区号
    private String car_number;// 车牌号码
    private String person_density;// 人口密度
    private String administrative_level;// 行政级别
    private String area_population;// 区域人口
    private List<Map<String, Object>> refernce_url;// 关联网站

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getProvience_name()
    {
        return provience_name;
    }

    public void setProvience_name(String provience_name)
    {
        this.provience_name = provience_name;
    }

    public String getCity_name()
    {
        return city_name;
    }

    public void setCity_name(String city_name)
    {
        this.city_name = city_name;
    }

    public String getArea_name()
    {
        return area_name;
    }

    public void setArea_name(String area_name)
    {
        this.area_name = area_name;
    }

    public String getTown_name()
    {
        return town_name;
    }

    public void setTown_name(String town_name)
    {
        this.town_name = town_name;
    }

    public String getTown_url()
    {
        return town_url;
    }

    public void setTown_url(String town_url)
    {
        this.town_url = town_url;
    }

    public String getAdministrative_code()
    {
        return administrative_code;
    }

    public void setAdministrative_code(String administrative_code)
    {
        this.administrative_code = administrative_code;
    }

    public String getArea_acreage()
    {
        return area_acreage;
    }

    public void setArea_acreage(String area_acreage)
    {
        this.area_acreage = area_acreage;
    }

    public String getHas_detail()
    {
        return has_detail;
    }

    public void setHas_detail(String has_detail)
    {
        this.has_detail = has_detail;
    }

    public String getArea_code()
    {
        return area_code;
    }

    public void setArea_code(String area_code)
    {
        this.area_code = area_code;
    }

    public String getCar_number()
    {
        return car_number;
    }

    public void setCar_number(String car_number)
    {
        this.car_number = car_number;
    }

    public String getPerson_density()
    {
        return person_density;
    }

    public void setPerson_density(String person_density)
    {
        this.person_density = person_density;
    }

    public String getAdministrative_level()
    {
        return administrative_level;
    }

    public void setAdministrative_level(String administrative_level)
    {
        this.administrative_level = administrative_level;
    }

    public String getArea_population()
    {
        return area_population;
    }

    public void setArea_population(String area_population)
    {
        this.area_population = area_population;
    }

    public List<Map<String, Object>> getRefernce_url()
    {
        return refernce_url;
    }

    public void setRefernce_url(List<Map<String, Object>> refernce_url)
    {
        this.refernce_url = refernce_url;
    }

}
