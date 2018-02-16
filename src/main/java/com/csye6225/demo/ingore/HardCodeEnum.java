package com.csye6225.demo.ingore;

public enum HardCodeEnum {
     DATASOURCE_URL("jdbc:mysql://csye6225-fall2017.cuyuii1ixvbr.us-east-1.rds.amazonaws.com:3306/csye6225"),
     BUCKET_NAME("csye6225bucketjyt0817-cloudformation.com");

    private String value;

    HardCodeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
