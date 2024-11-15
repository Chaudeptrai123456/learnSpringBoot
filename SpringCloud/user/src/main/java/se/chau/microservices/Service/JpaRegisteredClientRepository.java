//package se.chau.microservices.Service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.stereotype.Component;
//import se.chau.microservices.Entity.RegisteredClientEntity;
//
//@Component
//public class JpaRegisteredClientRepository implements RegisteredClientRepository {
//    @Autowired
//    private   RegisteredClientRepository jpaRepository;
//
//
//    @Override
//    public void save(RegisteredClient registeredClient) {
//        RegisteredClientEntity entity = new RegisteredClientEntity();
//        entity.setClientId(registeredClient.getClientId());
//        entity.setClientSecret(registeredClient.getClientSecret());
//        entity.setClientName(registeredClient.getClientName());
//        entity.setRedirectUri(registeredClient.getRedirectUris().iterator().next());
//        entity.setScopes(registeredClient.getScopes());
//        entity.setAuthorizationGrantType(registeredClient.getAuthorizationGrantTypes().iterator().next().getValue());
//        jpaRepository.save(entity);
//    }
//
//    @Override
//    public RegisteredClient findById(String id) {
//        return jpaRepository.findById(id);
//    }
//
//    @Override
//    public RegisteredClient findByClientId(String clientId) {
//        return jpaRepository.findByClientId(clientId);
//    }
//}
