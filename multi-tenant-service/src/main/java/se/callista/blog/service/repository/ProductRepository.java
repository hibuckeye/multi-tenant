package se.callista.blog.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.callista.blog.service.domain.entity.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

}