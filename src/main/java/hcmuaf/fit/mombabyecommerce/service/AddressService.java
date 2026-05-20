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
    private final Jdbi jdbi;

    public AddressService(Jdbi jdbi) {
        this.jdbi = jdbi;
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
        return addressDao.addAddress(address);
    }
    public Address findDefautlByUserId(Integer id) {
        return addressDao.getAddressDefaultByUserId(id);
    }
    public Boolean updateStatus(Integer id, String status) {
        return addressDao.updateStatus(id, status);
    }

    public Boolean updateDefautlById(Integer id, boolean defaultStatus) {
        return addressDao.updateDefaultById(id, defaultStatus);
    }

    public boolean setDefaultAddress(Integer userId, Integer addressId) {
        return jdbi.inTransaction(handle -> {
            AddressDao dao = handle.attach(AddressDao.class);

            Address address = dao.getAddressByIdAndUserId(addressId, userId);
            if (address == null) {
                return false;
            }

            dao.clearDefaultByUserId(userId);
            return dao.updateDefaultByIdAndUserId(addressId, userId, true) > 0;
        });
    }
    public static void main(String[] args) {
        AddressService addressService = new AddressService(DBConnection.getJdbi());
        System.out.println(addressService.findByUserId(42));
    }

    public boolean deleteAddress(Integer userId, Integer addressId) {
        Address deletingAddress = addressDao.getAddressByIdAndUserId(addressId, userId);
        if (deletingAddress == null) {
            return false;
        }

        int affectedRows = addressDao.deleteAddressByIdAndUserId(addressId, userId);
        if (affectedRows <= 0) {
            return false;
        }

        if (Boolean.TRUE.equals(deletingAddress.getIsDefault())) {
            List<Address> remainingAddresses = addressDao.getAddressByUserId(userId);
            if (!remainingAddresses.isEmpty()) {
                Address nextDefault = remainingAddresses.get(0);
                addressDao.updateDefaultByIdAndUserId(nextDefault.getId(), userId, true);
            }
        }

        return true;
    }
    public boolean updateAddress(Integer userId, Address address) {
        Address currentAddress = addressDao.getAddressByIdAndUserId(address.getId(), userId);

        if (currentAddress == null) {
            return false;
        }

        address.setUserId(userId);

        if (address.getCountry() == null || address.getCountry().isBlank()) {
            address.setCountry("Việt Nam");
        }

        if (address.getAddressType() == null || address.getAddressType().isBlank()) {
            address.setAddressType("shipping");
        }

        return addressDao.updateAddress(address) > 0;
    }




}

