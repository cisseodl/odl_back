package com.odc.aws_learning.auth.base.response.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
    private Utils() throws IllegalAccessException {
        throw new IllegalAccessException("can not instantiate this class");
    }

    public static <T> List<T> emptyList(int initialSize) {
        return new ArrayList<>(initialSize);
    }

    public static Date dateBasedOnCurrentTime(long timestampsToAdd) {
        return new Date(System.currentTimeMillis() + timestampsToAdd);
    }

    public static Date date(long timestamps) {
        return new Date(timestamps);
    }

    public static Date now() {
        return date(System.currentTimeMillis());
    }

    public static <T> List<T> emptyList() {
        return emptyList(0);
    }

    public static String uid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static <T> Map.Entry<String, T> dict(String key, T value) {
        return new AbstractMap.SimpleEntry(key, value);
    }


    public static boolean isGreaterThanZero(Long value) {
        return Objects.nonNull(value) && value > 0;
    }

    public static boolean isCorrectUid(String uid) {
        return Objects.nonNull(uid) && uid.length() == 32;
    }

    public static String dateFormat(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.FRANCE);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static Pageable getDefaultPageable(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "createdAt");
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
    }

    public static <E> List<E> removeDuplicates(List<E> list) {
        List<E> newList = new ArrayList<>();
        for (E element : list) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        return newList;
    }


}
