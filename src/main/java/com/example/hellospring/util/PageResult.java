package com.example.hellospring.util;

import java.io.Serializable;
import java.util.List;

public class PageResult implements Serializable {
    private int totalCount;
    private int pageSize;
    private int totalPage;
    private int currPage;
    private List<?> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public PageResult(int totalCount, int pageSize, int currPage, List<?> list) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.list = list;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize); // 计算总页数
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "totalCount=" + totalCount +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", currPage=" + currPage +
                ", list=" + list +
                '}';
    }
}
