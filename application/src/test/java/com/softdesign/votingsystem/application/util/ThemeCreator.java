package com.softdesign.votingsystem.application.util;

import com.softdesign.business.data.ThemeData;
import com.softdesign.business.domain.Theme;
import com.softdesign.business.response.ThemeResponse;

import java.time.LocalDateTime;

public class ThemeCreator {
    public static Theme createDataTheme() {
        Theme theme = new Theme();
        theme.setCreatedAt(LocalDateTime.now());
        theme.setId("606a4c8b75adf05a9009ae35");
        theme.setCreatedAt(LocalDateTime.now());
        theme.setQuestion("teste");
        return theme;
    }

    public static ThemeResponse createDataThemeResponse() {
        ThemeResponse themeResponse = new ThemeResponse();
        themeResponse.setCreatedAt(LocalDateTime.now());
        themeResponse.setId("606a4c8b75adf05a9009ae35");
        themeResponse.setCreatedAt(LocalDateTime.now());
        themeResponse.setQuestion("teste");
        return themeResponse;
    }

    public static ThemeData createDataThemeData() {
        ThemeData themeData = new ThemeData();
        themeData.setQuestion("teste");
        return themeData;
    }
}
