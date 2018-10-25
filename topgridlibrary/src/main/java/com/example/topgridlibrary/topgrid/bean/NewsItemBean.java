package com.example.topgridlibrary.topgrid.bean;

import java.util.List;

public class NewsItemBean {
    private List<NewsBean> items;

    public List<NewsBean> getItems() {
        return items;
    }

    public void setItems(List<NewsBean> items) {
        this.items = items;
    }

    public class NewsBean{
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
