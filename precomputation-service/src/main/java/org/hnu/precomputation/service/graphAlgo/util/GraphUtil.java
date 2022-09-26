package org.hnu.precomputation.service.graphAlgo.util;

import org.hnu.precomputation.service.service.Pair;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphUtil {

    public static Double round2DecimalDouble(Double d) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return Double.parseDouble(decimalFormat.format(d));
    }

    public static Map<Object, List<Object>> read_csv(String fileNameStr) {
        Map<Object, List<Object>> mMap = new HashMap<Object,List<Object>>();
        File file = new File(fileNameStr);
        StringBuffer sb = new StringBuffer();
        int cnt = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String tempStr = null;
            while ((tempStr = reader.readLine()) != null) {
                String [] a = tempStr.split(",");

                int id = Integer.parseInt(a[0]);
                int index = Integer.parseInt(a[1]);
//                System.out.println(id+" "+ index);
                if(mMap.get(id) != null ){
                    List indexList = mMap.get(id);
                    indexList.add(index);
                    mMap.replace(id,indexList);
                }else{
                    List indexList = new ArrayList<Integer>();
                    indexList.add(index);
                    mMap.put(id, indexList);
                }
            }
            reader.close();
            return mMap;

        } catch (FileNotFoundException e) {
            System.out.println("读取文件出错!!!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("文件IO流异常!!!");
            e.printStackTrace();
        }
        return null;
    }

    public static Map<Object, List<Object>> gFormat(ArrayList<Pair> g){
        Map<Object, List<Object>> mMap = new HashMap<Object,List<Object>>();
        try {
            for (Pair pair: g) {
                long id = pair.vertex1, index = pair.vertex2;
//                System.out.println(id+" "+ index);
                if(mMap.get(id) != null ){
                    List indexList = mMap.get(id);
                    indexList.add(index);
                    mMap.replace(id,indexList);
                }else{
                    List indexList = new ArrayList<Integer>();
                    indexList.add(index);
                    mMap.put(id, indexList);
                }
            }
        }catch (Exception e){
            System.out.println("format error !!!");
        }
        return mMap;
    }

    public static ArrayList<long[]> gFormatForEgo(ArrayList<Pair> g) {
        ArrayList<long[]> pairs = new ArrayList<long[]>();
        try {
            for (Pair pair : g) {
                long v1 = pair.vertex1;
                long v2 = pair.vertex2;
                pairs.add(new long[]{v1, v2});
            }
        } catch (Exception e){
            System.out.println("format error !!!");
        }
        return pairs;
    }

}