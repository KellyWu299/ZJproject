package org.hnu.precomputation.web.controller;

import com.vesoft.nebula.client.graph.exception.IOErrorException;
import lombok.extern.slf4j.Slf4j;
import org.hnu.precomputation.common.model.Nebula.team;
import org.hnu.precomputation.common.model.Nebula.teamEdge;
import org.hnu.precomputation.common.model.api.NebulaResult;
import org.hnu.precomputation.service.service.NebulaGraphService;
import org.hnu.precomputation.service.service.NebulaTemplate;
import org.hnu.precomputation.service.service.SqlBuildUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
@Slf4j
@RestController
@RequestMapping("/NebulaTest")
public class NebulaController {
    @Resource
    NebulaTemplate nebulaTemplate;
    @Resource
    SqlBuildUtils sqlBuildUtils;

    @PostMapping  ("/addVertex")
    public Object addVertex(@RequestBody team t) throws  InvocationTargetException, NoSuchMethodException, IllegalAccessException {


        String ss = sqlBuildUtils.buildInsert(t);
        System.out.println(ss);
        NebulaResult nebulaResult = nebulaTemplate.executeObject(ss);
        return nebulaResult;
    }
    @GetMapping("/deleteVertex")
    public Object deleteVertex(String t)  {
        String ss = sqlBuildUtils.deleteVertex(t);
        System.out.println(ss);
        NebulaResult nebulaResult = nebulaTemplate.executeObject(ss);
        return nebulaResult;
    }
    @PostMapping("/deleteTag")
    public Object deleteTag(@RequestBody team t) throws  InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String ss = sqlBuildUtils.deleteTag(t);
        System.out.println(ss);
        NebulaResult nebulaResult = nebulaTemplate.executeObject(ss);
        return nebulaResult;
    }
    @PostMapping("/updateVertex")
    public Object updateVertex(@RequestBody team t) throws  InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String ss = sqlBuildUtils.updateVertex(t);
        System.out.println(ss);
        NebulaResult nebulaResult = nebulaTemplate.executeObject(ss);
        return nebulaResult;
    }
    @PostMapping("/addEdge")
    public Object addEdge(@RequestBody teamEdge te) throws  InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String ss = sqlBuildUtils.buildEdge(te);
        System.out.println(ss);
        NebulaResult nebulaResult = nebulaTemplate.executeObject(ss);
        return nebulaResult;
    }
    @GetMapping("/deleteEdge")
    public Object deleteEdge(String Ename,String leftid,String rightid)   {
        String ss = sqlBuildUtils.deleteEdge(Ename,leftid,rightid);
        System.out.println(ss);
        NebulaResult nebulaResult = nebulaTemplate.executeObject(ss);
        return nebulaResult;
    }
    @PostMapping("/updateEdge")
    public Object updateEdge(@RequestBody teamEdge te) throws  InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String ss = sqlBuildUtils.updateEdge(te);
        System.out.println(ss);
        NebulaResult nebulaResult = nebulaTemplate.executeObject(ss);
        return nebulaResult;
    }
    @GetMapping("/queryVertex")
    public Object queryVertex(String vName,String Vid) throws  InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String ss = sqlBuildUtils.queryVertex(vName,Vid);
        System.out.println(ss);
        NebulaResult<team> teamNebulaResult = nebulaTemplate.queryObject(ss, team.class);

        return teamNebulaResult;
    }
    @GetMapping("/queryEdge")
    public Object queryEdge(String Name,String lid,String rid) throws  InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String ss = sqlBuildUtils.queryEgde(Name,lid,rid);
        System.out.println(ss);
        NebulaResult<team> teamNebulaResult = nebulaTemplate.queryObject(ss, team.class);

        return teamNebulaResult;
    }


}
