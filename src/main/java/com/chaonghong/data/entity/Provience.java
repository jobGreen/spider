package com.chaonghong.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "boya_china_provience")
public class Provience
{
    @Id
    private String id;
    private String provience_name;
    private String provience_url;
    private String has_execute = "0";

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

    public String getProvience_url()
    {
        return provience_url;
    }

    public void setProvience_url(String provience_url)
    {
        this.provience_url = provience_url;
    }

    public String getHas_execute()
    {
        return has_execute;
    }

    public void setHas_execute(String has_execute)
    {
        this.has_execute = has_execute;
    }

}
