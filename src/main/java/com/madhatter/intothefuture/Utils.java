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
        int chunkSize = content.size() / chunkCount;
        List<List<T>> chunkList = ListUtils.partition(content, chunkSize);
        if(chunkList.size() > chunkCount) {
            chunkList = new ArrayList<>(chunkList);
            List<T> penultimateItem = chunkList.remove(chunkCount);
            List<T> ultimateItem = chunkList.remove(chunkCount - 1);
            List<T> joinedItems = Stream.concat(penultimateItem.stream(), ultimateItem.stream()).collect(Collectors.toList());
            chunkList.add(joinedItems);
        }
        return chunkList;
    }
}
