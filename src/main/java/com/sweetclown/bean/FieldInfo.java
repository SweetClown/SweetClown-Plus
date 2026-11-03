package com.sweetclown.bean;

public class FieldInfo {

    /**
     * 資料庫中的原始欄位名稱（例如：user_id, create_time）
     */
    private String FieldName;

    /**
     * 生成的 Java Bean 屬性名稱，通常採用小駝峰命名（例如：userId, createTime）
     */
    private String propertyName;

    /**
     * 資料庫中的欄位資料型態（例如：varchar, int, datetime）
     */
    private String sqlType;

    /**
     * 映射後的 Java 資料型態（例如：String, Integer, Date）
     */
    private String javaType;

    /**
     * 欄位的註釋/表頭備解（例如：使用者唯一識別碼、建立時間）
     */
    private String comment;

    /**
     * 標記該欄位是否為資料庫自增鍵（Auto Increment）
     */
    private Boolean isAutoIncrement;


    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String fieldName) {
        FieldName = fieldName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }
}
