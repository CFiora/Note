package com.fiora.note2.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SalaryUtil {

    // 统一返回按年计算的工资
    // 月工资 * 12
    // 日工资 * 360
    public static Object[] getIntervalAndSalaryRange(String salaryStr) {
        Object[] result = new Object[3];
        // xxx.xx(-xxx.xxx)?[kKwW千万元]?/[天日月年]
        String[] arr = salaryStr.split("/");

        String interval = arr[1];
        String salaryInterval = null;
        int salaryIntervalInt;
        switch (interval) {
            case "日": salaryInterval = "日"; salaryIntervalInt=360; break;
            case "天": salaryInterval = "日"; salaryIntervalInt=360; break;
            case "年": salaryInterval = "年"; salaryIntervalInt=1; break;
            default: salaryInterval = "月"; salaryIntervalInt=12; break;
        }
        result[0] = salaryInterval;

        String salary = arr[0];
        int salaryUnit;
        switch(salary.substring(salary.length()-1)) {
            case "千": salaryUnit = 1000; break;
            case "k": salaryUnit = 1000; break;
            case "K": salaryUnit = 1000; break;
            case "万": salaryUnit = 10000; break;
            case "w": salaryUnit = 10000; break;
            case "W": salaryUnit = 10000; break;
            default: salaryUnit = 1; break;
        }

        String minAndMax = salary.split("[千万kKwW元]")[0];
        if (minAndMax.indexOf("-") != -1) {
            String min = minAndMax.split("-")[0];
            String max = minAndMax.split("-")[1];
            result[1] = BigDecimal.valueOf(Double.valueOf(min)).multiply(BigDecimal.valueOf(salaryIntervalInt)).multiply(BigDecimal.valueOf(salaryUnit)).intValue()+"";
            result[2] = BigDecimal.valueOf(Double.valueOf(max)).multiply(BigDecimal.valueOf(salaryIntervalInt)).multiply(BigDecimal.valueOf(salaryUnit)).intValue()+"";
        } else {
            result[1] = BigDecimal.valueOf(Double.valueOf(minAndMax)).multiply(BigDecimal.valueOf(salaryIntervalInt)).multiply(BigDecimal.valueOf(salaryUnit)).intValue()+"";
            result[2] = result[1];
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> salaries = new ArrayList<String>(){{
            add("500/天");
            add("200-300/日");
            add("5000-6000/月");
            add("15-18千/月");
            add("1.3-1.6万/月");
            add("30-40万/年");
            add("1.3-1.6W/月");
        }};
        salaries.stream().forEach(salaryStr -> {
            Object[] result = getIntervalAndSalaryRange(salaryStr);
            System.out.print(salaryStr + " -> ");
            for(Object object: result) {
                System.out.print(object+"\t");
            }
            System.out.println();
        });
    }
}
