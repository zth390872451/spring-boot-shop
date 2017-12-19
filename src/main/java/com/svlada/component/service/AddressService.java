package com.svlada.component.service;

import com.svlada.entity.Address;
import com.svlada.component.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public void deleteAddress(@PathVariable("id") Long id, Long userId) {
        addressRepository.deleteOneByIdAndUserId(id, userId);
        Address defaultAddress = addressRepository.findOneByUserIdAndIsDefault(userId,true);
        if (defaultAddress == null) {//无默认地址，则按照ID降序,设置第一条记录作为默认地址
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            List<Address> all = addressRepository.findAll(sort);
            if (all != null && all.size() > 0) {
                Address address = all.get(0);
                address.setDefault(true);
                addressRepository.save(address);
            }
        }
    }

}
