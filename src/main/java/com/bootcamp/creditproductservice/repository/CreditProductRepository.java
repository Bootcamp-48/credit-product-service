package com.bootcamp.creditproductservice.repository;

import com.bootcamp.creditproductservice.entity.CreditProduct;
import com.bootcamp.creditproductservice.entity.CreditProductType;
import io.reactivex.rxjava3.core.Flowable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CreditProductRepository extends ReactiveMongoRepository<CreditProduct, String> {
    Flowable<CreditProduct> findByCustomerId(String customerId);

    Flowable<CreditProduct> findByCustomerIdAndType(String customerId, CreditProductType creditProductType);
}