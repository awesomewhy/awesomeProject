package com.dark.online.repository;

import com.dark.online.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
