package com.bootcamp.creditproductservice.service;

import com.bootcamp.creditproductservice.entity.CreditProduct;
import org.springframework.stereotype.Service;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import reactor.core.publisher.Mono;

@Service
public interface CreditProductService {

    Single<CreditProduct> createCreditProduct(CreditProduct creditProduct);

    Maybe<CreditProduct> getCreditProductById(String id);

    Observable<CreditProduct> getAllCreditProductsByCustomerId(String customerId);

    Single<CreditProduct> updateCreditProduct(String id, CreditProduct creditProduct);

   Completable deleteCreditProduct(String id);


    Mono<Boolean> checkHasCreditCard(String customerId);

}
