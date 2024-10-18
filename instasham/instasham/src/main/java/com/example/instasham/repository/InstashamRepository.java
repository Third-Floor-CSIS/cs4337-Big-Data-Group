package com.example.instasham.repository;

/* We create a repository for our service for encapsulating storage, retrieval and search */

import com.example.instasham.entity.Instasham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/* We pass in the entity model and the data type of it's Id */
@Repository
public interface InstashamRepository extends JpaRepository<Instasham,Integer> {
}
/* Jpa is an extension of Repository and contains the full API CrudRepository and PagingAndSortingRepository
   This way we can do CRUD operations, Pagination, and Sorting */
