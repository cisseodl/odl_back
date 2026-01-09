package com.odc.aws_learning.auth.base.response;

// import lombok.AllArgsConstructor; // Removed
// import lombok.Data; // Removed
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects; // Added for equals/hashCode

// @Data // Removed
// @AllArgsConstructor // Removed
public class PageData<T> {
    private List<T> items;
    private long totalElements;

    public PageData() {
    }

    public PageData(List<T> items, long totalElements) {
        this.items = items;
        this.totalElements = totalElements;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public static <S> PageData<S> of(List<S> items, long totalElements) {
        return new PageData<>(items, totalElements);
    }

    public static <S> PageData<S> fromPage(Page<S> page) {
        return of(page.getContent(), page.getTotalElements());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageData<?> pageData = (PageData<?>) o;
        return totalElements == pageData.totalElements && Objects.equals(items, pageData.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, totalElements);
    }

    @Override
    public String toString() {
        return "PageData{" +
               "items=" + items +
               ", totalElements=" + totalElements +
               '}';
    }
}
