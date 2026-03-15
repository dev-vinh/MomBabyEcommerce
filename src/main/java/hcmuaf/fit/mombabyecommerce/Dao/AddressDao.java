package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Address;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterConstructorMapper(Address.class)
public interface AddressDao {
    @SqlQuery(value = "SELECT *" +
            " FROM address" +
            " WHERE userId = :userId and isDefault =1;")
    Address getAddressDefaultByUserId(@Bind("userId") Integer userId);

    @SqlUpdate("INSERT INTO address (userId, addressType, fullName, phoneNumber, street, city, state, country, isDefault) " +
            "VALUES (:userId, :addressType, :fullName, :phoneNumber, :street, :city, :state, :country, :isDefault)")
    @GetGeneratedKeys("id")
    int addAddress(  @BindBean Address address);

    @SqlQuery(value = "SELECT * " +
            "FROM address as a " +
            "WHERE a.userId = :userId " )
    List<Address> getAddressByUserId(@Bind("userId") Integer userId);


}
