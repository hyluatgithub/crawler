package com.aadata.crawler.model;

import cn.hutool.json.JSONObject;

public class Symptom {
    private String id;
    private String title;
    private String href;
    // 类型 1 症状  2 疾病  3 检查
    private int type;

    public Symptom(String id, String title, String href, int type) {
        this.id = id;
        this.title = title;
        this.href = href;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("id", id);
        jsonObject.putOpt("title", title);
        jsonObject.putOpt("href", href);
        jsonObject.putOpt("type", type);
        return jsonObject.toString();
//            return "编码：" + id + ", 症状：" + title + ", 链接：" + href + ", 类型：" + (type == 1 ? "症状" : "疾病");
    }
}
