package org.example.mcvlab3.repository;

import org.example.mcvlab3.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByPostalCodeAndCity(String postalCode, String city);
}
