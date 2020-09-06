package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FooBar;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FooBar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FooBarRepository extends JpaRepository<FooBar, Long> {
}
