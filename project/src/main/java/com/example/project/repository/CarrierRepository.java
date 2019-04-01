package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.project.model.Carrier;

@SuppressWarnings("unused")
@Repository
public interface CarrierRepository extends JpaRepository<Carrier, Long>, JpaSpecificationExecutor<Carrier> {
	List<Carrier> findAllByCarrierid(long carrierid);
}
