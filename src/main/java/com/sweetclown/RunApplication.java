package com.sweetclown;

import com.sweetclown.bean.TableInfo;
import com.sweetclown.builder.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class RunApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class, args);
        List<TableInfo> tableInfoList = BuildTable.getTables();

        BuildBase.execute();

        for (TableInfo tableInfo : tableInfoList) {
            BuildPo.execute(tableInfo);
            BuildQuery.execute(tableInfo);
            BuildMapper.execute(tableInfo);
            BuildMapperXML.execute(tableInfo);
        }
    }
}
