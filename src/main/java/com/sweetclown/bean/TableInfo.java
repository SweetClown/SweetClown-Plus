package com.sweetclown.bean;

import com.sweetclown.bean.FieldInfo;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableInfo {

    /**
     * 資料庫表名（例如：user_order_info）
     */
    private String tableName;

    /**
     * 生成的 Java Bean 類別名，通常採用大駝峰命名（例如：UserOrderInfo）
     */
    private String beanName;

    /**
     * 生成的 Java Bean 變數/參數名稱，通常採用小駝峰命名（例如：userOrderInfo）
     */
    private String beanParamName;

    /**
     * 資料表的註釋/備解（例如：使用者訂單詳情表）
     */
    private String comment;

    /**
     * 該資料表所包含的所有欄位資訊列表
     */
    private List<FieldInfo> fieldList;

    private List<FieldInfo> fieldExtendList;

    /**
     * 資料表的索引/主鍵映射關係
     * Key: 索引名稱（如 PRIMARY, idx_user_id）<br>
     * Value: 該索引對應的欄位資訊列表（支援複合索引）
     */
    private Map<String, List<FieldInfo>> keyIndexMap = new LinkedHashMap<>();

    /**
     * 標記欄位中是否包含 Date 類型（通常映射為 java.util.Date 或 java.time.LocalDate），用於模板判斷是否需要 import 對應套件
     */
    private Boolean haveDate;

    /**
     * 標記欄位中是否包含 DateTime 類型（通常映射為 java.util.Date 或 java.time.LocalDateTime），用於模板判斷是否需要 import 對應套件
     */
    private Boolean haveDateTime;

    /**
     * 標記欄位中是否包含 BigDecimal 類型（例如金錢、高精度浮點數），用於模板判斷是否需要 import java.math.BigDecimal
     */
    private Boolean haveBigDecimal;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanParamName() {
        return beanParamName;
    }

    public void setBeanParamName(String beanParamName) {
        this.beanParamName = beanParamName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<FieldInfo> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldInfo> fieldList) {
        this.fieldList = fieldList;
    }

    public Map<String, List<FieldInfo>> getKeyIndexMap() {
        return keyIndexMap;
    }

    public void setKeyIndexMap(Map<String, List<FieldInfo>> keyIndexMap) {
        this.keyIndexMap = keyIndexMap;
    }

    public Boolean getHaveDate() {
        return haveDate;
    }

    public void setHaveDate(Boolean haveDate) {
        this.haveDate = haveDate;
    }

    public Boolean getHaveDateTime() {
        return haveDateTime;
    }

    public void setHaveDateTime(Boolean haveDateTime) {
        this.haveDateTime = haveDateTime;
    }

    public Boolean getHaveBigDecimal() {
        return haveBigDecimal;
    }

    public void setHaveBigDecimal(Boolean haveBigDecimal) {
        this.haveBigDecimal = haveBigDecimal;
    }

    public List<FieldInfo> getFieldExtendList() {
        return fieldExtendList;
    }

    public void setFieldExtendList(List<FieldInfo> fieldExtendList) {
        this.fieldExtendList = fieldExtendList;
    }
}
