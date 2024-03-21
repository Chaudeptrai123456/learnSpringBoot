package com.example.serverUser.Service.FindProduct;

import com.example.serverUser.Entity.Product;

import java.util.List;
import java.util.Set;

public interface findProduct {

    List<Product> findProductByName(Set<String> names);

}
