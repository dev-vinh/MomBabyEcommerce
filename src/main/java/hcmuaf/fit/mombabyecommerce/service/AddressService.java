package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.AddressDao;
import hcmuaf.fit.mombabyecommerce.model.Address;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;

import java.util.List;

@RegisterBeanMapper(Address.class)
public class AddressService {
    public AddressService(Jdbi jdbi) {
        this.addressDao = jdbi.onDemand(AddressDao.class);
    }

    AddressDao addressDao;

    public Address findDefautlByUserId(Integer id) {
        return addressDao.getAddressDefaultByUserId(id);
    }

    public int addAddress(Address address) {
        if (address.getIsDefault() == null) {
            address.setIsDefault(false);
        }
        return addressDao.addAddress(address);
    }
    public List<Address> findByUserId(Integer userId) {
        return addressDao.getAddressByUserId(userId);
    }
}
