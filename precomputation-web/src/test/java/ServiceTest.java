import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.service.graphAlgo.util.GraphUtil;
import org.hnu.precomputation.service.service.DatasetService;
import org.hnu.precomputation.service.service.GraphComputeService;
import org.hnu.precomputation.service.service.JanusGraphService;
import org.hnu.precomputation.service.Impl.Pair;
import org.hnu.precomputation.service.web.PrecomputationApplication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = PrecomputationApplication.class)

public class ServiceTest {
    @Autowired(required = false)
    private GraphComputeService graphComputeService;
    @Autowired
    private JanusGraphService janusGraphService;
    @Autowired
    private DatasetService datasetService;

    @Test
    public void test() {
        long id = 135;
        Dataset dataset = datasetService.queryDataset(id);  //根据id获取图元数据
        ArrayList<Pair> g =  janusGraphService.getGraph(dataset.getJanusIdFileName());  //获取图数据集
        Map<Object, List<Object>> mMap = GraphUtil.gFormat(g);  //格式转换
        System.out.println(mMap);
    }
}
