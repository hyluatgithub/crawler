package com.aadata.crawler.model;

import java.util.List;

public class DiseaseDetail {
    private String id;
    /**
     * 疾病名称
     */
    private String name;
    /**
     * 疾病简介
     */
    private String desc;
    /**
     * 疾病病因
     */
    private String cause;
    /**
     * 预防措施
     */
    private String prevent;

    /**
     * 治疗周期
     */
    private String cureLastTime;

    /**
     * 治疗方式
     */
    private String cureWay;
    /**
     * 治愈概率
     */
    private String curedProb;
    /**
     * 疾病易感人群
     */
    private String easyGet;

    /**
     * 宜吃食物
     */
    private String dietGood;

    /**
     * 宜吃食物列表
     */
    private String goodFoods;

    /**
     * 忌吃食物
     */
    private String dietBad;

    /**
     * 忌吃食物
     */
    private String badFoods;

    /**
     * 症状
     *
     * @return
     */
    private List<Long> symptoms;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getPrevent() {
        return prevent;
    }

    public void setPrevent(String prevent) {
        this.prevent = prevent;
    }

    public String getCureLastTime() {
        return cureLastTime;
    }

    public void setCureLastTime(String cureLastTime) {
        this.cureLastTime = cureLastTime;
    }

    public String getCureWay() {
        return cureWay;
    }

    public void setCureWay(String cureWay) {
        this.cureWay = cureWay;
    }

    public String getCuredProb() {
        return curedProb;
    }

    public void setCuredProb(String curedProb) {
        this.curedProb = curedProb;
    }

    public String getEasyGet() {
        return easyGet;
    }

    public void setEasyGet(String easyGet) {
        this.easyGet = easyGet;
    }

    public String getDietGood() {
        return dietGood;
    }

    public void setDietGood(String dietGood) {
        this.dietGood = dietGood;
    }

    public String getGoodFoods() {
        return goodFoods;
    }

    public void setGoodFoods(String goodFoods) {
        this.goodFoods = goodFoods;
    }

    public String getDietBad() {
        return dietBad;
    }

    public void setDietBad(String dietBad) {
        this.dietBad = dietBad;
    }

    public String getBadFoods() {
        return badFoods;
    }

    public void setBadFoods(String badFoods) {
        this.badFoods = badFoods;
    }

    public List<Long> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<Long> symptoms) {
        this.symptoms = symptoms;
    }

    @Override
    public String toString() {
        return "DiseaseDetail{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", cause='" + cause + '\'' +
                ", prevent='" + prevent + '\'' +
                ", cureLastTime='" + cureLastTime + '\'' +
                ", cureWay='" + cureWay + '\'' +
                ", curedProb='" + curedProb + '\'' +
                ", easyGet='" + easyGet + '\'' +
                ", dietGood='" + dietGood + '\'' +
                ", goodFoods='" + goodFoods + '\'' +
                ", dietBad='" + dietBad + '\'' +
                ", badFoods='" + badFoods + '\'' +
                ", symptoms=" + symptoms +
                '}';
    }
}
