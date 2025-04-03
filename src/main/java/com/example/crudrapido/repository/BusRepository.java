package com.example.crudrapido.repository;

import com.example.crudrapido.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus, Long> {
}
