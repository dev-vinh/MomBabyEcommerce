package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.AddressDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Address;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;

import java.util.List;

@RegisterBeanMapper(Address.class)
public class AddressService {
    AddressDao addressDao;


    public AddressService(Jdbi jdbi) {
        this.addressDao = jdbi.onDemand(AddressDao.class);
    }

    public List<Address> findByUserId(Integer user_id) {
        return addressDao.getAddressByUserId(user_id);
    }

    public Address findById(Integer id) {
        return addressDao.getAddressById(id);
    }


    public int addAddress(Address address) {
        if (address.getDefault() == null) {
            address.setDefault(false);
        }
        return addressDao.addAddress(
                address.getUserId(),
                address.getAddressType(),
                address.getFullName(),
                address.getPhoneNumber(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getDefault()
        );
    }
    public Address findDefautlByUserId(Integer id) {
        return addressDao.getAddressDefaultByUserId(id);
    }
    public Boolean updateStatus(Integer id, String status) {
        return addressDao.updateStatus(id, status);
    }

    public static void main(String[] args) {
        AddressService addressService = new AddressService(DBConnection.getJdbi());
        System.out.println(addressService.findByUserId(42));
    }





}

