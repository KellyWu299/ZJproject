package org.hnu.precomputation.service.graphAlgo.preCompute;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OperateTxt {
    static void WriteIndex(String Path,H2H_Index h2H_index) throws IOException {
        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(Path));
        String s=null;
        for (Integer id:h2H_index.v_map.keySet()
             ) {
            s=id+" "+h2H_index.node_map.get(id).code+" "+ArrayToListForInt(h2H_index.node_map.get(id).pos)+" "+ArrayToListForDouble(h2H_index.node_map.get(id).dis);
            bufferedWriter.write(s);
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    static Map<Integer, entity_Index> ReadIndex(String Path) throws IOException {
        BufferedReader bufferedReader=new BufferedReader(new FileReader(Path));
        Map<Integer, entity_Index> map=new HashMap<>();
        String s=null;
        Integer id;

        while((s = bufferedReader.readLine())!=null){
            String[] date=s.split(" ");
            id=Integer.valueOf(date[0]);
            entity_Index index =new entity_Index();
            index.code=date[1];
            index.pos=StringToIntArray(date[2]);
            index.dis=StringToDoubleArray(date[3]);
            map.put(id,index);
        }
        return map;
    }

    static Map<String,Integer> ReadCodeMap(String Path) throws IOException {
        BufferedReader bufferedReader=new BufferedReader(new FileReader(Path));
        Map<String,Integer> map=new HashMap<>();
        String s=null;
        String code;
        int id;
        while((s = bufferedReader.readLine())!=null){
            String[] date=s.split(" ");
            code=date[0];
            id=Integer.parseInt(date[1]);
            map.put(code,id);
        }
        return map;
    }

    public static void main(String[] args) throws IOException {
        Long StartTime=System.currentTimeMillis();
        Map<Integer, entity_Index> map=ReadIndex("D:\\IDEA_content\\ShortestDistanceTest\\Paths\\Txt\\haha.txt");
        Long EndTime=System.currentTimeMillis();
        System.out.println(EndTime-StartTime);
    }

    static String ArrayToListForInt(ArrayList<Integer> data){
        StringBuilder s= new StringBuilder("a");
        for (Integer d:data
             ) {
            s.append(d).append(",");
        }
        String result=s.substring(1,s.length()-1);
        return result;
    }
    static String ArrayToListForDouble(ArrayList<Double> data){
        StringBuilder s=new StringBuilder("a");
        for (Double d:data
        ) {
            s.append(d).append(",");
        }
        String result=s.substring(1,s.length()-1);
        return result;
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
