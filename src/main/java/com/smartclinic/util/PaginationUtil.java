package com.smartclinic.util;

import java.util.Collections;
import java.util.List;

public final class PaginationUtil {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 50;

    private PaginationUtil() {
    }

    public static <T> PageSlice<T> paginate(List<T> source, Integer requestedPage, Integer requestedSize) {
        List<T> safeSource = source == null ? Collections.emptyList() : source;
        int pageSize = requestedSize == null || requestedSize <= 0
                ? DEFAULT_PAGE_SIZE
                : Math.min(requestedSize, MAX_PAGE_SIZE);
        int totalItems = safeSource.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / pageSize));
        int currentPage = requestedPage == null || requestedPage <= 0
                ? 1
                : Math.min(requestedPage, totalPages);
        int fromIndex = Math.min((currentPage - 1) * pageSize, totalItems);
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        return new PageSlice<>(safeSource.subList(fromIndex, toIndex), currentPage, totalPages, totalItems, pageSize);
    }
}
