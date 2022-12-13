package org.hnu.precomputation.service.graphAlgo.preCompute;

import java.util.ArrayList;


public class OperateStringAndArray {

    public static String ArrayToStringForInt(ArrayList<Integer> data){
        StringBuilder s= new StringBuilder("a");
        for (Integer d:data
             ) {
            s.append(d).append(",");
        }
        return s.substring(1,s.length()-1);
    }
    public static String ArrayToStringForDouble(ArrayList<Double> data){
        StringBuilder s=new StringBuilder("a");
        for (Double d:data
        ) {
            s.append(d).append(",");
        }
        return s.substring(1,s.length()-1);
    }

    static ArrayList<Integer> StringToIntArray(String s){
        String[] date=s.split(",");
        ArrayList<Integer> array=new ArrayList<>();
        for (String d:date
             ) {
            array.add(Integer.valueOf(d));
        }
        return array;
    }

    static ArrayList<Double> StringToDoubleArray(String s){
        String[] date=s.split(",");
        ArrayList<Double> array=new ArrayList<>();
        for (String d:date
        ) {
            array.add(Double.valueOf(d));
        }
        return array;
    }

}
