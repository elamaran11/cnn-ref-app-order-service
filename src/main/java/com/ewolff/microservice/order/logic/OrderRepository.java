package com.ewolff.microservice.order.logic;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "order", path = "order")
interface OrderRepository extends MongoRepository<Order, String> {

}
