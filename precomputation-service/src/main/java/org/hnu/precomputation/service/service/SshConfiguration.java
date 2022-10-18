package org.hnu.precomputation.service.service;

import lombok.Data;

@Data
public class SshConfiguration {
    private String host;
    private int    port;
    private String userName;
    private String password;

}
