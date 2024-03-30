package com.sau.project2.ImageUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ImageStorageStrategyFactory {


    private final Map<String, ImageStorageStrategy> strategiesMap;


    public ImageStorageStrategyFactory(Map<String, ImageStorageStrategy> strategiesMap) {
        this.strategiesMap = strategiesMap;
    }


    @Value("${spring.profiles.active}")
    private String activeProfile;

    public ImageStorageStrategy getImageStorageStrategy() {
        if (activeProfile.equals("EC2")) {
            return strategiesMap.get("s3StorageUtil");
        } else {
            return strategiesMap.get("localStorageUtil");
        }
    }
}
