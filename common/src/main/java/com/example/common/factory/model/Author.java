package com.example.common.factory.model;

/**
 * 基础用户接口
 * Created by John on 2017/6/25.
 */

public interface Author {
    String getId();
    void setId(String id);
    String getName();
    void setName(String name);
    String getPortrait();
    void setPortrait(String portrait);
}
