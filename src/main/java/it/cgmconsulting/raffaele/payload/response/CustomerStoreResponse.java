package it.cgmconsulting.raffaele.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CustomerStoreResponse {

   // store_name, total_customers

    private String storeName;
    private long totalCustomers;

}
