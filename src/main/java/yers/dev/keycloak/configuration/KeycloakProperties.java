package yers.dev.keycloak.configuration;// src/main/java/yers/dev/keycloak/config/KeycloakProperties.java

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String authServerUrl;
    private String realm;
    private String clientId;
    private String clientSecret;
    private String adminClientId;
    private String adminClientSecret;

    // геттеры/сеттеры
    public String getAuthServerUrl() { return authServerUrl; }
    public void setAuthServerUrl(String authServerUrl) { this.authServerUrl = authServerUrl; }
    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getClientSecret() { return clientSecret; }
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }
    public String getAdminClientId() { return adminClientId; }
    public void setAdminClientId(String adminClientId) { this.adminClientId = adminClientId; }
    public String getAdminClientSecret() { return adminClientSecret; }
    public void setAdminClientSecret(String adminClientSecret) { this.adminClientSecret = adminClientSecret; }
}
