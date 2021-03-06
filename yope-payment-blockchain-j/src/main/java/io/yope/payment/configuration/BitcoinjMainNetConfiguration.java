package io.yope.payment.configuration;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("with-mainnet")
public class BitcoinjMainNetConfiguration {
    @Bean(name="mainNetParams")
    public NetworkParameters getMainNetParams() {
        return MainNetParams.get();
    }
}
