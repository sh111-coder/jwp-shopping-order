package cart.member.application;

import java.util.Optional;

import cart.member.domain.Member;
import cart.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Optional<Member> findByMemberAuthInfo(final String email) {
        return memberRepository.findByEmail(email);
    }
}
