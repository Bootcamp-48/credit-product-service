package com.bootcamp.creditproductservice.expose;

import com.bootcamp.creditproductservice.entity.CreditProduct;
import com.bootcamp.creditproductservice.service.CreditProductService;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/credit-products")
@Api(tags = "Credit Products", description = "Operations related to credit products")
public class CreditProductController {

    private final CreditProductService creditService;

    @Autowired
    public CreditProductController(CreditProductService creditService) {
        this.creditService = creditService;
    }
    @ApiOperation(value = "Create a new credit product", response = CreditProduct.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Credit product created successfully", response = CreditProduct.class),
            @ApiResponse(code = 400, message = "Invalid input data")
    })
    @PostMapping
    public Single<ResponseEntity<CreditProduct>> createCreditProduct(
            @ApiParam(value = "Credit product data to create", required = true)
            @RequestBody CreditProduct creditProduct) {
        return creditService.createCreditProduct(creditProduct)
                .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(product))
                .onErrorResumeNext(error -> Single.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
    }

    @ApiOperation(value = "Get a credit product by ID", response = CreditProduct.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Credit product found", response = CreditProduct.class),
            @ApiResponse(code = 404, message = "Credit product not found")
    })
    @GetMapping("/{id}")
    public  Maybe<ResponseEntity<CreditProduct>> getCreditProductById(
            @ApiParam(value = "ID of the credit product to retrieve", required = true)
            @PathVariable String id) {
        return creditService.getCreditProductById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Maybe.just(ResponseEntity.notFound().build()));
    }

    @ApiOperation(value = "Update a credit product by ID", response = CreditProduct.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Credit product updated successfully", response = CreditProduct.class),
            @ApiResponse(code = 400, message = "Invalid input data"),
            @ApiResponse(code = 404, message = "Credit product not found")
    })
    @PutMapping("/{id}")
    public  Single<ResponseEntity<CreditProduct>> updateCreditProduct(
            @ApiParam(value = "ID of the credit product to update", required = true)
            @PathVariable String id,
            @ApiParam(value = "Updated credit product data", required = true)
            @RequestBody CreditProduct creditProduct) {
        return creditService.updateCreditProduct(id, creditProduct)
                .map(updatedProduct -> ResponseEntity.ok(updatedProduct))
                .onErrorResumeNext(error -> Single.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @ApiOperation(value = "Delete a credit product by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Credit product deleted successfully"),
            @ApiResponse(code = 404, message = "Credit product not found")
    })
    @DeleteMapping("/{id}")
    public  Single<ResponseEntity<Void>> deleteCreditProductById(
            @ApiParam(value = "ID of the credit product to delete", required = true)
            @PathVariable String id) {
        return creditService.deleteCreditProduct(id)
                .toSingleDefault(ResponseEntity.noContent().<Void>build())
                .onErrorResumeNext(error -> Single.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @ApiOperation(value = "Check if a customer has a credit card")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Check successful", response = Boolean.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/hasCreditCard/{customerId}")
    public Mono<ResponseEntity<Boolean>> checkHasCreditCard(
            @ApiParam(value = "ID of the customer to check", required = true)
            @PathVariable String customerId) {
        return creditService.checkHasCreditCard(customerId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }
}
