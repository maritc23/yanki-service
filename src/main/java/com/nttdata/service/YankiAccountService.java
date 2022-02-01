package com.nttdata.service;


import com.nttdata.model.YankiAccount;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface YankiAccountService {
    Mono<YankiAccount> createAccount(YankiAccount yankiAccount);

    Mono<YankiAccount> getAccount(String nationalId);

    Mono<YankiAccount> updateAccount(YankiAccount yankiAccount);

    Mono<Void> deleteAccount(String phone);

    Flux<YankiAccount> transaction(String origin, String destiny, double amount);

}
