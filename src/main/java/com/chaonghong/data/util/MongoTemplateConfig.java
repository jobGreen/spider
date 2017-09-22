package com.chaonghong.data.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
public class MongoTemplateConfig
{
    @Value("${orion.host}")
    private String host;
    @Value("${orion.user}")
    private String user;
    @Value("${orion.password}")
    private String password;
    @Value("${orion.database}")
    private String database;
    @Value("${orion.port}")
    private int port;

    /**
     * 注入自定义的mongoTemplate
     * 
     * @return
     * @throws Exception
     */
    public @Bean MongoTemplate mongoTemplate() throws Exception
    {
        // mongodb options
        MongoClientOptions mco = new Builder().maxWaitTime(200000)
            .maxConnectionLifeTime(300000)
            .maxConnectionIdleTime(30000)
            .threadsAllowedToBlockForConnectionMultiplier(3)
            .connectionsPerHost(100)
            .build();

        // mongodb验证证书 用户名，密码
        MongoCredential cardential = MongoCredential.createCredential(user, database, password.toCharArray());
        List<MongoCredential> list = new ArrayList<>(2);
        list.add(cardential);

        // mongodb链接
        MongoClient mongoCilent = new MongoClient(new ServerAddress(host, port), list, mco);
        // mongodb 数据库工厂
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoCilent, database);
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);

        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        // 返回mongodb 模板

        MongoTemplate mongo = new MongoTemplate(mongoDbFactory, converter);
        return mongo;
    }
}
