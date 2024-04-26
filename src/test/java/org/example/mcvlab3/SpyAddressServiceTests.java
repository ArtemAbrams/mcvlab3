package org.example.mcvlab3;


import org.example.mcvlab3.model.Address;
import org.example.mcvlab3.model.Person;
import org.example.mcvlab3.repository.AddressRepository;
import org.example.mcvlab3.service.AddressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SpyAddressServiceTests {

    @Spy
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @Test
    void testSaveAddress() {
        Address address = new Address(1L, "123 Street", "City", "12345", new Person());
        doReturn(address).when(addressRepository).saveAndFlush(any(Address.class));

        addressService.save(address);

        verify(addressRepository).saveAndFlush(address);
    }

    @Test
    void testSaveAddressTwice() {
        Address address = new Address(1L, "123 Street", "City", "12345", new Person());
        addressService.saveAddressTwice(address);

        verify(addressRepository, times(2)).saveAndFlush(address);
    }
}
