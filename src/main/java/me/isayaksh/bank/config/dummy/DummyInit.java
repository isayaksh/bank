package me.isayaksh.bank.config.dummy;

import me.isayaksh.bank.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DummyInit extends DummyObject{

    @Bean
    @Profile("dev")
    CommandLineRunner init(MemberRepository memberRepository) {
        return args -> {
            memberRepository.save(newMember("ssar", "쌀"));
        };
    }

}
