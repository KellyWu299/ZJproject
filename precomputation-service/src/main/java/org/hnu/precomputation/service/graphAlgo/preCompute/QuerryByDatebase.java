package org.hnu.precomputation.service.graphAlgo.preCompute;

import org.hnu.precomputation.common.model.dataset.index;
import org.hnu.precomputation.service.dao.IndexDao;

import java.io.IOException;
import java.util.ArrayList;

import static org.hnu.precomputation.service.graphAlgo.preCompute.OperateStringAndArray.StringToDoubleArray;

public class QuerryByDatebase {


    //批量查询 查询整个文件
//    public void txtQuerry(String QuerryPath,String OSIndexPath) throws IOException {
//        BufferedReader br=new BufferedReader(new FileReader(QuerryPath));
//        BufferedWriter wr=new BufferedWriter(new FileWriter(OSIndexPath));
//        String s=null;
//        while((s=br.readLine())!=null)
//        {
//            String[] str=s.split(" ");
//            int a=Integer.parseInt(str[0]);
//            int b=Integer.parseInt(str[1]);
//            double c=singleQuery(a,b);
//            System.out.println(c);
//            wr.write(Double.toString(c));
//            wr.newLine();
//        }
//        wr.flush();
//        wr.close();
//    }


    public static void main(String[] args) throws IOException {
        Long StartTime=System.currentTimeMillis();
//        querry("D:\\IDEA_content\\ShortestDistanceTest_\\src\\ShortestDistanceTest\\IndexTest.txt","D:\\IDEA_content\\ShortestDistanceTest\\Paths\\Txt\\haha.txt",FilePath.CodeMap,"D:\\IDEA_content\\ShortestDistanceTest_\\src\\ShortestDistanceTest\\Index_result.txt");
        Long EndTime=System.currentTimeMillis();
        System.out.println(EndTime-StartTime);
    }

    //单个查询  查询某两个点
    public  double singleQuery(int v1, int v2,IndexDao indexDao,String tableName) throws IOException {
        //求LCA
        index index1= indexDao.selectById(tableName,v1);
        index index2=indexDao.selectById(tableName,v2);
        String code1 = index1.getCode();
        String code2 =index2.getCode();
        ArrayList<Double> V1_dis=StringToDoubleArray(index1.getDis());
        ArrayList<Double> V2_dis=StringToDoubleArray(index2.getDis());
        int len = Math.min(code1.length(), code2.length());
        StringBuilder common_prefix = new StringBuilder();
        for (int i = 0; i < len;i++) {
            if (code1.charAt(i) == code2.charAt(i)) {
                common_prefix.append(code1.charAt(i));
            } else {
                break;
            }
        }
        String cp_ = common_prefix.toString();
        String cp= "";
        while(true){
            if(cp_.charAt(cp_.length()-1)!=')'){
                cp_=cp_.substring(0,cp_.length()-1);
            }
            else{
                cp=cp_;
                break;
            }
        }
        index indexLca=indexDao.selectByIdCode(tableName,cp);
        ArrayList<Double> LcaDis=StringToDoubleArray(indexLca.getDis());
        double d = Integer.MAX_VALUE;
        for (int i=0; i< LcaDis.size();i++) {
            d = Math.min(d, V1_dis.get(i) + V2_dis.get(i));
        }
        return d;
    }
}
