package com.bootcamp.creditproductservice.service.impl;

import com.bootcamp.creditproductservice.entity.CreditProduct;
import com.bootcamp.creditproductservice.entity.CreditProductType;
import com.bootcamp.creditproductservice.repository.CreditProductRepository;
import com.bootcamp.creditproductservice.service.CreditProductService;
import com.bootcamp.creditproductservice.service.exceptions.CreditProductNotFoundException;
import com.bootcamp.creditproductservice.service.exceptions.InvalidCreditProductException;
import com.bootcamp.creditproductservice.service.exceptions.MultipleCreditsNotAllowedException;
import com.bootcamp.creditproductservice.webclient.CustomerWebClient;
import com.bootcamp.creditproductservice.webclient.model.CustomerType;
import io.reactivex.rxjava3.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;


@Service
public class CreditProductServiceImpl implements CreditProductService {

    private final CreditProductRepository creditProductRepository;

    @Autowired
    private CustomerWebClient customerWebClient;

    @Autowired
    public CreditProductServiceImpl(CreditProductRepository creditProductRepository) {
        this.creditProductRepository = creditProductRepository;
    }



    @Override
    public Single<CreditProduct> createCreditProduct(CreditProduct creditProduct) {
        String customerId = creditProduct.getCustomerId();

        return Single.fromPublisher(customerWebClient.getCustomerById(customerId))
                .flatMap(customer -> {
                    if (CustomerType.PERSONAL.getValue().equals(customer.getType())
                            || CustomerType.VIP.getValue().equals(customer.getType())) {
                        if (creditProduct.getType() == CreditProductType.PERSONAL_CREDIT) {
                            return validateAndSavePersonalCredit(customerId, creditProduct);
                        } else {
                            return Single.error(new InvalidCreditProductException("Invalid credit product type"));
                        }
                    } else if (CustomerType.BUSINESS.getValue().equals(customer.getType())
                            || CustomerType.PYME.getValue().equals(customer.getType())) {
                        if (creditProduct.getType() == CreditProductType.BUSINESS_CREDIT
                                || creditProduct.getType() == CreditProductType.CREDIT_CARD) {
                            return validateAndSaveBusinessCredit(creditProduct);
                        } else {
                            return Single.error(new InvalidCreditProductException("Invalid credit product type"));
                        }
                    }
                    return Single.error(new InvalidCreditProductException("Invalid customer type"));
                });
    }

    @Override
    public Maybe<CreditProduct> getCreditProductById(String id) {
        return Maybe.fromPublisher(creditProductRepository.findById(id));
    }

    @Override
    public Observable<CreditProduct> getAllCreditProductsByCustomerId(String customerId) {
        return Observable.fromPublisher(creditProductRepository.findByCustomerId(customerId));
    }

    @Override
    public Single<CreditProduct> updateCreditProduct(String id, CreditProduct creditProduct) {
        return Single.fromPublisher(creditProductRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditProductNotFoundException(id)))
                .flatMap(existingProduct -> {
                    existingProduct.setAmount(creditProduct.getAmount());
                    return creditProductRepository.save(existingProduct);
                }));
    }

    @Override
    public Completable deleteCreditProduct(String id) {
        return Completable.fromPublisher(creditProductRepository.deleteById(id));
    }

    @Override
    public Mono<Boolean> checkHasCreditCard(String customerId) {
        return Mono.from(creditProductRepository.findByCustomerIdAndType(customerId, CreditProductType.CREDIT_CARD))
                .map(creditCard -> true)
                .defaultIfEmpty(false);
    }

    private Single<CreditProduct> validateAndSavePersonalCredit(String customerId, CreditProduct creditProduct) {
        return validatePersonalCredit(customerId)
                .flatMap(valid -> {
                    if (valid && !creditProduct.getType().equals(CreditProductType.BUSINESS_CREDIT)) {
                        return Single.fromPublisher(creditProductRepository.save(creditProduct));
                    } else {
                        return Single.error(new MultipleCreditsNotAllowedException("Invalid product"));
                    }
                });
    }

    private Single<CreditProduct> validateAndSaveBusinessCredit(CreditProduct creditProduct) {
        if (!creditProduct.getType().equals(CreditProductType.PERSONAL_CREDIT)) {
            return Single.fromPublisher(creditProductRepository.save(creditProduct));
        } else {
            return Single.error(new InvalidCreditProductException("Invalid product"));
        }
    }

    private Single<Boolean> validatePersonalCredit(String customerId) {
        return creditProductRepository.findByCustomerIdAndType(customerId, CreditProductType.PERSONAL_CREDIT)
                .toList()
                .map(List::isEmpty);
    }


}