package org.example.mcvlab3.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.mcvlab3.model.Address;
import org.example.mcvlab3.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class AddressService {

    private AddressRepository addressRepository;

    public void save(Address address) {
        addressRepository.saveAndFlush(address);
    }

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Address getById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

    public Address updateAddress(Long id, Address updatedAddress) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        existingAddress.setStreet(updatedAddress.getStreet());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setPostalCode(updatedAddress.getPostalCode());
        return addressRepository.saveAndFlush(existingAddress);
    }

    public List<Address> findAddressesByPostalCodeAndCity(String postalCode, String city) {
        return addressRepository.findByPostalCodeAndCity(postalCode, city);
    }

    public void saveAddressTwice(Address address) {
        saveAndCheck(address);
        saveAndCheck(address);
    }

    private void saveAndCheck(Address address) {
        addressRepository.saveAndFlush(address);
    }
}
