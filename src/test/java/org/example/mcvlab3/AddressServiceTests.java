package org.example.mcvlab3;

import org.example.mcvlab3.model.Address;
import org.example.mcvlab3.model.Person;
import org.example.mcvlab3.repository.AddressRepository;
import org.example.mcvlab3.service.AddressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AddressServiceTests {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @Test
    void testSaveAddress_ValidData() {
        Address validAddress = new Address(1L, "123 Street", "City", "12345", new Person());
        when(addressRepository.saveAndFlush(validAddress)).thenReturn(validAddress);
        addressService.save(validAddress);
        verify(addressRepository, times(1)).saveAndFlush(validAddress);
    }

    @Test
    void testFindAllAddresses() {
        when(addressRepository.findAll()).thenReturn(
                Arrays.asList(new Address(1L, "Street 1", "City", "12345", new Person()),
                        new Address(2L, "Street 2", "City", "67890", new Person())));
        List<Address> results = addressService.findAll();
        assertEquals(2, results.size());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    void testGetById_Found() {
        Long addressId = 1L;
        Address expectedAddress = new Address(addressId, "123 Street", "City", "12345", new Person());
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(expectedAddress));
        Address result = addressService.getById(addressId);
        assertEquals(expectedAddress, result);
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void testGetById_NotFound_ThrowsException() {
        Long addressId = 99L;
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> addressService.getById(addressId));
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void testDeleteById() {
        Long addressId = 1L;
        doNothing().when(addressRepository).deleteById(addressId);
        addressService.deleteById(addressId);
        verify(addressRepository, times(1)).deleteById(addressId);
    }

    @Test
    void testUpdateAddress_Success() {
        Address originalAddress = new Address(1L, "123 Old Street", "Old City", "12345", new Person());
        Address updatedInfo = new Address(1L, "456 New Street", "New City", "67890", new Person());
        when(addressRepository.findById(1L)).thenReturn(Optional.of(originalAddress));
        when(addressRepository.saveAndFlush(originalAddress)).thenReturn(updatedInfo);
        Address updatedAddress = addressService.updateAddress(1L, updatedInfo);
        assertNotNull(updatedAddress);
        assertEquals("456 New Street", updatedAddress.getStreet());
        verify(addressRepository, times(1)).saveAndFlush(originalAddress);
    }

    @Test
    void testFindAddressesByPostalCodeAndCity() {
        String postalCode = "12345";
        String city = "City";
        when(addressRepository.findByPostalCodeAndCity(postalCode, city))
                .thenReturn(Arrays.asList(new Address(1L, "123 Street", city, postalCode, new Person()),
                        new Address(2L, "456 Avenue", city, postalCode, new Person())));
        List<Address> results = addressService.findAddressesByPostalCodeAndCity(postalCode, city);
        assertEquals(2, results.size());
        verify(addressRepository, times(1)).findByPostalCodeAndCity(postalCode, city);
    }

    @Test
    void testSaveAddress_Twice() {
        Address address = new Address(1L, "123 Street", "City", "12345", new Person());
        when(addressRepository.saveAndFlush(address)).thenReturn(address);  // Return the same address to emulate saveAndFlush

        addressService.saveAddressTwice(address);

        verify(addressRepository, times(2)).saveAndFlush(address);
    }
}
