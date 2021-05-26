package com.aadata.crawler.model;

import com.aadata.crawler.MediaDepartmentCrawler;

import java.util.List;

public class SymptomDetail {
    private String id;
    // 介绍
    private String jieshao;
    // 介绍关键字
    private List<Symptom> jieshaoKeyword;
    // 病因
    private String yuanyin;
    // 病因关键字
    private List<Symptom> yuanyinKeyword;
    // 预防
    private String yufang;
    // 检查
    private String jiancha;
    // 检查关键字
    private List<Symptom> jianchaKeyword;
    // 鉴别诊断
    private String zhenduan;
    // 鉴别诊断关键字
    private List<Symptom> zhenduanKeyword;
    // 宜吃食物
    private String goodFood;
    // 宜吃食物列表和原因
    private String goodFoods;
    // 忌吃食物
    private String badFood;
    // 忌吃食物
    private String badFoods;
    // 相关症状
    private List<Symptom> aboutSymptom;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJieshao() {
        return jieshao;
    }

    public void setJieshao(String jieshao) {
        this.jieshao = jieshao;
    }

    public List<Symptom> getJieshaoKeyword() {
        return jieshaoKeyword;
    }

    public void setJieshaoKeyword(List<Symptom> jieshaoKeyword) {
        this.jieshaoKeyword = jieshaoKeyword;
    }

    public String getYuanyin() {
        return yuanyin;
    }

    public void setYuanyin(String yuanyin) {
        this.yuanyin = yuanyin;
    }

    public List<Symptom> getYuanyinKeyword() {
        return yuanyinKeyword;
    }

    public void setYuanyinKeyword(List<Symptom> yuanyinKeyword) {
        this.yuanyinKeyword = yuanyinKeyword;
    }

    public String getYufang() {
        return yufang;
    }

    public void setYufang(String yufang) {
        this.yufang = yufang;
    }

    public String getJiancha() {
        return jiancha;
    }

    public void setJiancha(String jiancha) {
        this.jiancha = jiancha;
    }

    public List<Symptom> getJianchaKeyword() {
        return jianchaKeyword;
    }

    public void setJianchaKeyword(List<Symptom> jianchaKeyword) {
        this.jianchaKeyword = jianchaKeyword;
    }

    public String getZhenduan() {
        return zhenduan;
    }

    public void setZhenduan(String zhenduan) {
        this.zhenduan = zhenduan;
    }

    public List<Symptom> getZhenduanKeyword() {
        return zhenduanKeyword;
    }

    public void setZhenduanKeyword(List<Symptom> zhenduanKeyword) {
        this.zhenduanKeyword = zhenduanKeyword;
    }

    public String getGoodFood() {
        return goodFood;
    }

    public void setGoodFood(String goodFood) {
        this.goodFood = goodFood;
    }

    public String getGoodFoods() {
        return goodFoods;
    }

    public void setGoodFoods(String goodFoods) {
        this.goodFoods = goodFoods;
    }

    public String getBadFood() {
        return badFood;
    }

    public void setBadFood(String badFood) {
        this.badFood = badFood;
    }

    public List<Symptom> getAboutSymptom() {
        return aboutSymptom;
    }

    public void setAboutSymptom(List<Symptom> aboutSymptom) {
        this.aboutSymptom = aboutSymptom;
    }

    public String getBadFoods() {
        return badFoods;
    }

    public void setBadFoods(String badFoods) {
        this.badFoods = badFoods;
    }
}
