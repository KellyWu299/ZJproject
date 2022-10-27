package org.hnu.precomputation.service.service;

import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;
import lombok.extern.slf4j.Slf4j;
import org.hnu.precomputation.common.model.api.NebulaConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.util.Arrays;
import java.util.List;

//初始化
@Slf4j
public class NebulaConfig {
    @Bean

    public NebulaPool nebulaPool() throws Exception {
        NebulaPool pool = new NebulaPool();
        NebulaPoolConfig nebulaPoolConfig = new NebulaPoolConfig();
        nebulaPoolConfig.setMaxConnSize(1000);
        List<HostAddress> addresses = Arrays.asList(new HostAddress("127.0.0.1", 9669));
        boolean init = pool.init(addresses, nebulaPoolConfig);
        if (!init) {
            throw new RuntimeException("NebulaGraph init err !");
        } else {
          log.info("NebulaGraph init Success ！");
        }
        return pool;
    }


    @Bean
    @Scope(scopeName = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session session(NebulaPool nebulaPool) {
        try {
            Session session = nebulaPool.getSession("root", "123456", false);
            session.execute(NebulaConstant.USE + "NebulaTest" + NebulaConstant.SEMICOLON);
            return session;
        } catch (Exception e) {
            log.error("get nebula session err , {} ", e.toString());
        }
        return null;
    }
}
