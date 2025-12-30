package com.odc.aws_learning.auth.base.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class PageData<T> {
    private List<T> items;
    private long totalElements;

    public static <S> PageData<S> of(List<S> items, long totalElements) {
        return new PageData<>(items, totalElements);
    }

    public static <S> PageData<S> fromPage(Page<S> page) {
        return of(page.getContent(), page.getTotalElements());
    }
}
