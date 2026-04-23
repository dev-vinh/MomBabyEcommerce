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

    @SqlQuery(value = "SELECT * " +
            "FROM address as a " +
            "WHERE a.userId = :userId " )
    List<Address> getAddressByUserId(@Bind("userId") Integer userId);


    @SqlQuery(value = "SELECT *" +
            " FROM address" +
            " WHERE id = :addressId;")
    Address getAddressById(@Bind("addressId") Integer addressId);

    @SqlUpdate("INSERT INTO address (userId, addressType, fullName, phoneNumber, street, city, state, country, isDefault) " +
            "VALUES (:userId, :addressType, :fullName, :phoneNumber, :street, :city, :state, :country, :isDefault)")
    @GetGeneratedKeys("id")
    int addAddress(@Bind("userId") Integer userId,
                   @Bind("addressType") String addressType,
                   @Bind("fullName") String fullName,
                   @Bind("phoneNumber") String phoneNumber,
                   @Bind("street") String street,
                   @Bind("city") String city,
                   @Bind("state") String state,
                   @Bind("country") String country,
                   @Bind("isDefault") Boolean isDefault);

    @SqlQuery(value = "SELECT *" +
            " FROM address" +
            " WHERE userId = :userId and isDefault =1;")
    Address getAddressDefaultByUserId(@Bind("userId") Integer userId);

    @SqlUpdate("UPDATE address " +
            " SET status=:status " +
            "WHERE id =:id ")
    Boolean updateStatus(@Bind("id") Integer id,@Bind("status") String status);

    @SqlUpdate("UPDATE address " +
            "SET isDefault = :defaultStatus " +
            "WHERE id = :id; ")
    Boolean updateDefaultById(@Bind("id") Integer id, @Bind("defaultStatus") boolean defaultStatus);

}
