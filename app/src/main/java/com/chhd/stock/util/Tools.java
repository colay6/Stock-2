package com.chhd.stock.util;

import com.chhd.stock.entity.News;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by CWQ on 2016/7/17.
 */
public class Tools {

    public static List<News> getRandomNoRepeatList(List<News> newss) {
        List<News> banners = new ArrayList<>();
        while (banners.size() < 5) {
            News banner = newss.get(new Random().nextInt(10));
            if (!banners.contains(banner)) {
                banners.add(banner);
            }
        }
        return banners;
    }
}
