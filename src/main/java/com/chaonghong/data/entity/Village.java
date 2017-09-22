package com.chaonghong.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "boya_china_village_basic_info")
public class Village
{
    @Id
    private String id;
    private String provience_name; // 省份名称
    private String city_name;// 市级名称
    private String area_name;// 区县名称
    private String town_name;// 街道或镇
    private String village_name;// 乡村名
    private String administrative_code;// 行政代码

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

    public String getVillage_name()
    {
        return village_name;
    }

    public void setVillage_name(String village_name)
    {
        this.village_name = village_name;
    }

    public String getAdministrative_code()
    {
        return administrative_code;
    }

    public void setAdministrative_code(String administrative_code)
    {
        this.administrative_code = administrative_code;
    }

}
