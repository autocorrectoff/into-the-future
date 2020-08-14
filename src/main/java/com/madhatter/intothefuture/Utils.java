package com.madhatter.intothefuture;

import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static <T> List<List<T>> splitToChunks(List<T> content, int chunkCount) {
        if (content.size() < chunkCount) {
            throw new RuntimeException("Cannot split into " + chunkCount + " chunks");
        }
        List<List<T>> chunkList = new ArrayList<>();
        int chunkSize = (content.size() + chunkCount -1) / chunkCount;
        for (int i = 0; i < content.size(); i += chunkSize) {
            int end = i + chunkSize;
            if(end > content.size()) {
                end = content.size();
            }
            chunkList.add(content.subList(i, end));
        }
        return chunkList;
    }
}
