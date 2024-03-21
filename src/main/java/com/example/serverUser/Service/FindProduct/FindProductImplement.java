package com.example.serverUser.Service.FindProduct;

import com.example.serverUser.Entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Service
public class FindProductImplement implements  findProduct{

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Product> findProductByName(Set<String> names) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> user = query.from(Product.class);

        Path<String> emailPath = user.get("nameProduct");

        List<Predicate> predicates = new ArrayList<>();
        for (String index : names) {
            predicates.add(cb.like(emailPath, index));
        }
        query.select(user)
                .where(cb.or(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query)
                .getResultList();
    }


}
