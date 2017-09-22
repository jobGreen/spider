package com.chaonghong.data.entity;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "boya_china_citys_basic_info")
public class City
{
    @Id
    private String id;
    private String provience_name;
    private String city_name;
    private String city_url;
    private String city_population;
    private String city_acreage;
    private String city_code;
    private String city_area_number;
    private String has_detail = "0";
    private String car_number;
    private String person_density;
    private String administrative_site;
    private String administrative_level;
    private List<Map<String, Object>> refernce_url;

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

    public String getCity_url()
    {
        return city_url;
    }

    public void setCity_url(String city_url)
    {
        this.city_url = city_url;
    }

    public String getCity_population()
    {
        return city_population;
    }

    public void setCity_population(String city_population)
    {
        this.city_population = city_population;
    }

    public String getCity_acreage()
    {
        return city_acreage;
    }

    public void setCity_acreage(String city_acreage)
    {
        this.city_acreage = city_acreage;
    }

    public String getCity_code()
    {
        return city_code;
    }

    public void setCity_code(String city_code)
    {
        this.city_code = city_code;
    }

    public String getCity_area_number()
    {
        return city_area_number;
    }

    public void setCity_area_number(String city_area_number)
    {
        this.city_area_number = city_area_number;
    }

    public String getHas_detail()
    {
        return has_detail;
    }

    public void setHas_detail(String has_detail)
    {
        this.has_detail = has_detail;
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

    public String getAdministrative_site()
    {
        return administrative_site;
    }

    public void setAdministrative_site(String administrative_site)
    {
        this.administrative_site = administrative_site;
    }

    public List<Map<String, Object>> getRefernce_url()
    {
        return refernce_url;
    }

    public void setRefernce_url(List<Map<String, Object>> refernce_url)
    {
        this.refernce_url = refernce_url;
    }

    public String getAdministrative_level()
    {
        return administrative_level;
    }

    public void setAdministrative_level(String administrative_level)
    {
        this.administrative_level = administrative_level;
    }

}
