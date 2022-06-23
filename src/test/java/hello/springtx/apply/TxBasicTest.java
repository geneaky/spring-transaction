package hello.springtx.apply;

import static org.assertj.core.api.Assertions.*;

import hello.springtx.apply.TxBasicTest.TxApplyBasicConfig.BasicService;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxBasicTest {

    @Autowired
    BasicService basicService;

    @Test
    public void proxyCheck() throws Exception {
        log.info("aop class={}", basicService.getClass());
        assertThat(AopUtils.isAopProxy(basicService)).isTrue();

    }

    @Test
    public void txTest() throws Exception {
        basicService.tx();
        basicService.nonTx();
    }

    @TestConfiguration
    static class TxApplyBasicConfig {
        @Bean
        BasicService basicService() {
            return new BasicService();
        }

        @Slf4j
        @Transactional
        static class BasicService {

            public void tx() {
                log.info("call tx");
                boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
                log.info("tx active={}", txActive);
            }

            public void nonTx() {
                log.info("call nonTx");
                boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
                log.info("tx active={}", txActive);
            }
        }
    }
}
