package com.taotao.search.bean;

import java.util.List;

public class SearchResult {
    
    private List<?> list;
    
    private Long total;
    
    public SearchResult(){
        
    }

    public SearchResult(List<?> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
    
    

}
