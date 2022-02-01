package com.nttdata.service.impl;

import com.netflix.discovery.converters.Auto;
import com.nttdata.model.YankiAccount;
import com.nttdata.repository.YankiAccountRepository;
import com.nttdata.service.YankiAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class YankiAccountServiceImpl implements YankiAccountService {

    @Autowired
    WebClient webClientAccount;

    @Autowired
    YankiAccountRepository yankiAccountRepository;

    @Override
    public Mono<YankiAccount> createAccount(YankiAccount yankiAccount){
        return  yankiAccountRepository.save(yankiAccount);
    }

    @Override
    public Mono<YankiAccount> getAccount(String phone){
        return yankiAccountRepository.findYankiAccountByNationalId(phone);
    }

    @Override
    public Mono<YankiAccount> updateAccount(YankiAccount yankiAccount){
        return yankiAccountRepository.save(yankiAccount);
    }

    @Override
    public Mono<Void> deleteAccount(String id){
        return yankiAccountRepository.deleteByNationalId(id);
    }

    @Override
    public Flux<YankiAccount> transaction(String origin, String destiny, double amount) {
        Mono<YankiAccount> originAcc = getAccount(origin).filter(e -> {if (e.getAmount()>amount) return true; else return false;} )
                .switchIfEmpty(Mono.empty()).flatMap(e -> {
                    e.setAmount(e.getAmount()-amount);
                    return this.updateAccount(e);
                });
        Mono<YankiAccount> destinyAcc = this.getAccount(destiny).switchIfEmpty(Mono.empty())
                .flatMap(e -> {
                e.setAmount(e.getAmount()+amount);
                return this.updateAccount(e);
                }).switchIfEmpty(Mono.empty());

        return Flux.concat(originAcc, destinyAcc);
    }
}
