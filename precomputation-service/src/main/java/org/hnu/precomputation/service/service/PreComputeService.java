package org.hnu.precomputation.service.service;

import org.hnu.precomputation.service.graphAlgo.preCompute.H2H_Index;
import org.hnu.precomputation.service.graphAlgo.preCompute.FilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PreComputeService {
    private final static Logger logger = LoggerFactory.getLogger("PreComputeService");
    @Autowired
    private JanusGraphService janusGraphService;
//    JanusGraphService janusGraphService=new JanusGraphService();


    public void MakeIndex(String v,String e) throws IOException {
        H2H_Index h2H_index=new H2H_Index();
        h2H_index.generate_H2H_Index(janusGraphService.getGraph(v,e));
    }

    public String GetPath(){
        return FilePath.TxtPath;
    }
}
