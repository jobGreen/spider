package com.chaonghong.data.entity;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "boya_china_area_basic_info")
public class Area
{
    @Id
    private String id;
    private String provience_name;
    private String city_name;
    private String area_name;
    private String area_url;
    private String administrative_code;
    private String area_acreage;
    private String has_detail = "0";
    private String area_number;
    private String car_number;
    private String person_density;
    private String administrative_site;
    private String administrative_level;
    private String area_population;
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

    public String getArea_name()
    {
        return area_name;
    }

    public void setArea_name(String area_name)
    {
        this.area_name = area_name;
    }

    public String getArea_url()
    {
        return area_url;
    }

    public void setArea_url(String area_url)
    {
        this.area_url = area_url;
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

    public String getArea_number()
    {
        return area_number;
    }

    public void setArea_number(String area_number)
    {
        this.area_number = area_number;
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
