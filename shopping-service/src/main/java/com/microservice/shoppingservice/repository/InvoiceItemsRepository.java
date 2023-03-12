package com.microservice.shoppingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.shoppingservice.entity.InvoiceItem;

public interface InvoiceItemsRepository extends JpaRepository<InvoiceItem, Long>{

}
