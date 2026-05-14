package com.smartclinic.util;

import java.util.List;

public class PageSlice<T> {
    private final List<T> items;
    private final int currentPage;
    private final int totalPages;
    private final int totalItems;
    private final int pageSize;

    public PageSlice(List<T> items, int currentPage, int totalPages, int totalItems, int pageSize) {
        this.items = items;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.pageSize = pageSize;
    }

    public List<T> getItems() {
        return items;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getPageSize() {
        return pageSize;
    }

    public boolean isHasPrevious() {
        return currentPage > 1;
    }

    public boolean isHasNext() {
        return currentPage < totalPages;
    }

    public int getPreviousPage() {
        return Math.max(1, currentPage - 1);
    }

    public int getNextPage() {
        return Math.min(totalPages, currentPage + 1);
    }
}
