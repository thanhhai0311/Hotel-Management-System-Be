package com.javaweb.repository;

import com.javaweb.model.entity.BillEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Integer> {
    Page<BillEntity> findByCustomer_Id(Integer customerId, Pageable pageable);

    List<BillEntity> findByCustomer_Id(Integer customerId, Sort sort);
}