package org.hnu.precomputation.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class JanusGraphService {
    private final static Logger logger = LoggerFactory.getLogger("JanusGraphService");

    @PostConstruct
    private void init() {
        logger.info("JanusGraph init.");
    }
}
