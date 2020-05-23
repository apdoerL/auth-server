package org.apdoer.auth.server.tokenstore;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;

import java.util.Date;

/**
 * @author Li
 * @version 1.0
 * @date 2020/5/22 15:45
 */
public class ExtRedisTokenStore extends RedisTokenStore {

    private String prifix = "";
    private final int reflushTokenValidSeconds;
    private final int accessTokenValidSeconds;

    private static final String ACCESS = "access";
    private static final String AUTH_TO_ACCESS = "auth_to_access";
    private static final String AUTH = "auth:";
    private static final String ACCESS_TO_REFRESH = "access_to_refresh";
    private static final String REFRESH_TO_ACCESS = "refresh_to_access";
    private static final String CLIENT_ID_TO_ACCESS = "client_id_to_access";
    private static final String UNAME_TO_ACCESS = "uname_to_access:";

    private final RedisConnectionFactory redisConnectionFactory;

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    private RedisTokenStoreSerializationStrategy storeSerializationStrategy = new JdkSerializationStrategy();

    public ExtRedisTokenStore(RedisConnectionFactory redisConnectionFactory, int accessTokenValidSeconds, int reflushTokenValidSeconds) {
        super(redisConnectionFactory);
        this.redisConnectionFactory = redisConnectionFactory;
        this.accessTokenValidSeconds = accessTokenValidSeconds;
        this.reflushTokenValidSeconds = reflushTokenValidSeconds;
    }


    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        byte[] serializedAccessToken = serialize(token);
        byte[] serializedAuth = serialize(authentication);
        byte[] accessKey = serializeKey(ACCESS + token.getValue());
        byte[] authKey = serializeKey(AUTH + token.getValue());
        byte[] authToAccessKey = serializeKey(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication));
        byte[] approvalkey = serializeKey(UNAME_TO_ACCESS + getApprovalKey(authentication));
        byte[] clientId = serializeKey(CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId());

        RedisConnection connection = getConnection();
        try {
            connection.openPipeline();
            connection.set(accessKey, serializedAccessToken);
            connection.set(authKey, serializedAuth);
            connection.set(authToAccessKey, serializedAccessToken);
            if (!authentication.isClientOnly()) {
                connection.rPush(approvalkey, serializedAccessToken);
            }
            connection.rPush(clientId, serializedAccessToken);
            if (token.getExpiration() != null) {
                int seconds = this.accessTokenValidSeconds;
                if (token instanceof DefaultOAuth2AccessToken) {
                    ((DefaultOAuth2AccessToken) token).setExpiration(new Date(System.currentTimeMillis() + seconds * 1000));
                }
                connection.expire(accessKey, seconds);
                connection.expire(authKey, seconds);
                connection.expire(authToAccessKey, seconds);
                connection.expire(clientId, seconds);
                connection.expire(approvalkey, seconds);
            }
            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if (refreshToken != null && refreshToken.getValue() != null) {
                byte[] refresh = serialize(token.getRefreshToken().getValue());
                byte[] auth = serialize(token.getValue());
                byte[] refreshToAccessKey = serializeKey(REFRESH_TO_ACCESS + token.getRefreshToken().getValue());
                connection.set(refreshToAccessKey, auth);
                byte[] accessToRefreshKey = serializeKey(ACCESS_TO_REFRESH + token.getValue());
                connection.set(accessToRefreshKey, refresh);
                if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
                    ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
                    Date expiration = expiringRefreshToken.getExpiration();
                    if (expiration != null) {
//						int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L)
//								.intValue();
                        int seconds = this.reflushTokenValidSeconds;
                        connection.expire(refreshToAccessKey, seconds);
                        connection.expire(accessToRefreshKey, seconds);
                    }
                }
            }
            connection.closePipeline();
        } finally {
            connection.close();
        }

    }


    private RedisConnection getConnection() {
        return this.redisConnectionFactory.getConnection();
    }

    private byte[] serialize(Object object) {
        return this.storeSerializationStrategy.serialize(object);
    }

    private byte[] serializeKey(String object) {
        return serialize(prifix + object);
    }

    private byte[] serialize(String string) {
        return storeSerializationStrategy.serialize(string);
    }

    private static String getApprovalKey(OAuth2Authentication authentication) {
        String userName = authentication.getUserAuthentication() == null ? ""
                : authentication.getUserAuthentication().getName();
        return getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
    }

    private static String getApprovalKey(String clientId, String userName) {
        return clientId + (userName == null ? "" : ":" + userName);
    }
}
