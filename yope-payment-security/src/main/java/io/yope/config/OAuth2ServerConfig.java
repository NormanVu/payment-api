package io.yope.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import io.yope.config.jwt.ServiceJwtTokenStore;

/**
 * Reference: http://projects.spring.io/spring-security-oauth/docs/oauth2.html
 * 
 * @author Gianluigi
 */
@ComponentScan
@EnableResourceServer
@Import({SecurityConfig.class})
@Order(value=2)
@Primary
public class OAuth2ServerConfig {
	
	public static final String WEB_APP_CLIENT = "yope-client";
	public static final String IPHONE_CLIENT = "iphone";
	public static final String ANDROID_CLIENT = "android";

	private static final String API_RESOURCE = "API";

	@Configuration
	@EnableAuthorizationServer
	@Primary
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

		@Autowired private AuthenticationManager authenticationManager;

		private final static Integer accessTokenValiditySeconds = 300;
		private final static Integer refreshTokenValiditySeconds = 300;
		private final static Integer webappOAuthExpiresInSeconds = 500; 
		
		// OAuth2 security configurer
		@Override
		public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			oauthServer
				.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
				.checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
		}
		
		// OAuth2 endpoint configurer
		@Override
		public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints
				.authenticationManager(authenticationManager)
				.tokenStore(tokenStore())
				.accessTokenConverter(accessTokenConverter());
		}
		
		@Bean
		public JwtAccessTokenConverter accessTokenConverter() {
			return new JwtAccessTokenConverter();
		}
				
		@Bean
		public TokenStore tokenStore() {
			return new ServiceJwtTokenStore();
		}

		@Override
		public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
		 	clients.inMemory()
		        .withClient(WEB_APP_CLIENT)
		        .resourceIds(API_RESOURCE)
		            .authorizedGrantTypes("password", "authorization_code", "refresh_token")
		            .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
		            .scopes("read", "write", "trust", "update")
		            .accessTokenValiditySeconds(webappOAuthExpiresInSeconds) // globalConfigs.advancedProps.integer("webAPP.oauth.token.access.expiresInSeconds");
		            .refreshTokenValiditySeconds(refreshTokenValiditySeconds)
		            .secret("secret")
		     .and()
		        .withClient(IPHONE_CLIENT)
		        .resourceIds(API_RESOURCE)
		            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
		            .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
		            .scopes("read", "write", "trust", "update")
		            .accessTokenValiditySeconds(accessTokenValiditySeconds) 
		            .refreshTokenValiditySeconds(refreshTokenValiditySeconds)
		            .secret("secret")
		     .and()
		        .withClient(ANDROID_CLIENT)
		        .resourceIds(API_RESOURCE)
		            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
		            .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
		            .scopes("read", "write", "trust", "update")
		            .accessTokenValiditySeconds(accessTokenValiditySeconds) 
		            .refreshTokenValiditySeconds(refreshTokenValiditySeconds) 
		            .secret("secret");
		}
	}
}



