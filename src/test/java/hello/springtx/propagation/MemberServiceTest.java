package hello.springtx.propagation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LogRepository logRepository;

    /**
     * memberService @Transactional: OFF
     * memberRepository @Transactional: ON
     * logRepository @Transactional: ON
     * @throws Exception
     */
    @Test
    public void outerTxOff_success() throws Exception {
        String username = "outerTxOff_success";

        memberService.joinV1(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService @Transactional: OFF
     * memberRepository @Transactional: ON
     * logRepository @Transactional: ON Exception
     * @throws Exception
     */
    @Test
    public void outerTxOff_fail() throws Exception {
        String username = "로그예외_outerTxOff_fail";

        assertThatThrownBy(() ->
            memberService.joinV1(username)
        ).isInstanceOf(RuntimeException.class);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * memberService @Transactional: ON
     * memberRepository @Transactional: OFF
     * logRepository @Transactional: OFF
     * @throws Exception
     */
    @Test
    public void singleTx() throws Exception {
        String username = "outerTxOff_success";

        memberService.joinV1(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService @Transactional: ON
     * memberRepository @Transactional: ON
     * logRepository @Transactional: ON
     * @throws Exception
     */
    @Test
    public void outerTxOn_success() throws Exception {
        String username = "outerTxOn_success";

        memberService.joinV1(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService @Transactional: ON
     * memberRepository @Transactional: ON
     * logRepository @Transactional: ON Exception
     * @throws Exception
     */
    @Test
    public void outerTxOn_fail() throws Exception {
        String username = "로그예외_outerTxOn_fail";

        assertThatThrownBy(() ->
            memberService.joinV1(username)
        ).isInstanceOf(RuntimeException.class);

        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * memberService @Transactional: ON
     * memberRepository @Transactional: ON
     * logRepository @Transactional: ON Exception
     * @throws Exception
     */
    @Test
    public void recoverException_fail() throws Exception {
        String username = "로그예외_recoverException_fail";

        assertThatThrownBy(() ->
            memberService.joinV2(username)
        ).isInstanceOf(UnexpectedRollbackException.class);

        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * memberService @Transactional: ON
     * memberRepository @Transactional: ON
     * logRepository @Transactional: ON(REQUIRES_NEW) Exception
     * @throws Exception
     */
    @Test
    public void recoverException_success() throws Exception {
        String username = "로그예외_recoverException_success";

        memberService.joinV2(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }

}