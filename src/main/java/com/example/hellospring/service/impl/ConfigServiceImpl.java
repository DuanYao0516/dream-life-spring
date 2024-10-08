package com.example.hellospring.service.impl;

import com.example.hellospring.entity.Config;
import com.example.hellospring.mapper.ConfigMapper;
import com.example.hellospring.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    private ConfigMapper configMapper;

    public static final String websiteName = "DreamLife";
    public static final String websiteDescription = "DreamLife 是 SpringBoot3 + Thymeleaf + Mybatis建造的公共博客网站";
    public static final String websiteLogo = "/static/user/dist/img/logo.png";
    public static final String websiteIcon = "/static/user/dist/img/favicon.png";

    public static final String yourAvatar = "/static/user/dist/img/f.jpg";
    public static final String yourEmail = "3496655347@qq.com";
    public static final String yourName = "laozibabac";

    public static final String footerAbout = "your  blog. have fun.";
    public static final String footerICP = "冀ICP备66666666号-2";
    public static final String footerCopyRight = "@2024 laozibabac";
    public static final String footerPoweredBy = "DreamLife";
    public static final String footerPoweredByURL = "##";
    @Override
    public int updateConfig(String configName, String configValue) {
        Config blogConfig = configMapper.selectByPrimaryKey(configName);
        if (blogConfig != null) {
            blogConfig.setConfigValue(configValue);
            blogConfig.setUpdateAt(new Date());
            return configMapper.updateByPrimaryKeySelective(blogConfig);
        }
        return 0;
    }

    @Override
    public Map<String, String> getAllConfigs() {
        List<Config> blogConfigs = configMapper.selectAll();
        Map<String, String> configMap = blogConfigs.stream().collect(
                Collectors.toMap(Config::getConfigName, Config::getConfigValue));

        for (Map.Entry<String, String> config : configMap.entrySet()) {
            if ("websiteName".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(websiteName);
            }
            if ("websiteDescription".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(websiteDescription);
            }
            if ("websiteLogo".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(websiteLogo);
            }
            if ("websiteIcon".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(websiteIcon);
            }
            if ("yourAvatar".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(yourAvatar);
            }
            if ("yourEmail".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(yourEmail);
            }
            if ("yourName".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(yourName);
            }
            if ("footerAbout".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerAbout);
            }
            if ("footerICP".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerICP);
            }
            if ("footerCopyRight".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerCopyRight);
            }
            if ("footerPoweredBy".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerPoweredBy);
            }
            if ("footerPoweredByURL".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerPoweredByURL);
            }
        }
        return configMap;
    }
}
