package com.microservice.shoppingservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.shoppingservice.client.CustomerClient;
import com.microservice.shoppingservice.client.ProductClient;
import com.microservice.shoppingservice.entity.Invoice;
import com.microservice.shoppingservice.entity.InvoiceItem;
import com.microservice.shoppingservice.model.Customer;
import com.microservice.shoppingservice.model.Product;
import com.microservice.shoppingservice.repository.InvoiceItemsRepository;
import com.microservice.shoppingservice.repository.InvoiceRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceItemsRepository invoiceItemsRepository;

    @Autowired
    CustomerClient customerClient;

   @Autowired
   ProductClient productClient;

    @Override
    public List<Invoice> findInvoiceAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        Invoice invoiceDb = invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
        if (invoiceDb != null) {
            return invoiceDb;
        }
        invoice.setState("CREATED");
        invoiceDb = invoiceRepository.save(invoice);
        invoiceDb.getItems().forEach(invoiceItem -> {
            productClient.updateStockProduct(invoiceItem.getProductId(), invoiceItem.getQuantity() * -1);
        });

        return invoiceDb;
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) {
        Invoice invoiceDB = getInvoice(invoice.getId());
        if (invoiceDB == null) {
            return null;
        }
        invoiceDB.setCustomerId(invoice.getCustomerId());
        invoiceDB.setDescription(invoice.getDescription());
        invoiceDB.setNumberInvoice(invoice.getNumberInvoice());
        invoiceDB.getItems().clear();
        invoiceDB.setItems(invoice.getItems());
        return invoiceRepository.save(invoiceDB);
    }

    @Override
    public Invoice deleteInvoice(Invoice invoice) {
        Invoice invoiceDb = getInvoice(invoice.getId());
        if (invoiceDb == null) {
            return null;
        }

        invoiceDb.setState("DELETED");
        return invoiceRepository.save(invoiceDb);
    }

    @Override
    public Invoice getInvoice(Long id) {

        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (null != invoice) {
            Customer customer = customerClient.getCustomer(invoice.getCustomerId()).getBody();
            invoice.setCustomer(customer);
            List<InvoiceItem> listItem = invoice.getItems().stream().map(invoiceItem -> {
                Product product = productClient.getProduct(invoiceItem.getProductId()).getBody();
                invoiceItem.setProduct(product);
                return invoiceItem;
            }).collect(Collectors.toList());
            invoice.setItems(listItem);
        }
        return invoice;
    }

}
