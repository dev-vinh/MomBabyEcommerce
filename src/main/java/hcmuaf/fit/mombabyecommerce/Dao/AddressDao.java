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


    @SqlUpdate("INSERT INTO address (userId, addressType, fullName, phoneNumber, street, city, state,districtId,wardCode, country, isDefault) " +
            "VALUES (:userId, :addressType, :fullName, :phoneNumber, :street, :city, :state,:districtId,:wardCode,:country, :isDefault)")
    @GetGeneratedKeys("id")
    int addAddress(@BindBean Address address);

    @SqlQuery(value = "SELECT *" +
            " FROM address" +
            " WHERE userId = :userId and isDefault =1;")
    Address getAddressDefaultByUserId(@Bind("userId") Integer userId);

    @SqlUpdate("UPDATE address " +
            " SET status=:status " +
            "WHERE id =:id ")
    Boolean updateStatus(@Bind("id") Integer id,@Bind("status") String status);


    @SqlUpdate("DELETE FROM address WHERE id = :addressId AND userId = :userId")
    int deleteAddressByIdAndUserId(@Bind("addressId") Integer addressId,
                                   @Bind("userId") Integer userId);

    @SqlUpdate("UPDATE address " +
            "SET isDefault = :defaultStatus " +
            "WHERE id = :id; ")
    Boolean updateDefaultById(@Bind("id") Integer id, @Bind("defaultStatus") boolean defaultStatus);

    @SqlQuery("SELECT * FROM address WHERE id = :addressId AND userId = :userId")
    Address getAddressByIdAndUserId(@Bind("addressId") Integer addressId,
                                    @Bind("userId") Integer userId);

    @SqlUpdate("UPDATE address SET isDefault = 0 WHERE userId = :userId")
    int clearDefaultByUserId(@Bind("userId") Integer userId);

    @SqlUpdate("UPDATE address SET isDefault = :defaultStatus WHERE id = :addressId AND userId = :userId")
    int updateDefaultByIdAndUserId(@Bind("addressId") Integer addressId,
                                   @Bind("userId") Integer userId,
                                   @Bind("defaultStatus") boolean defaultStatus);

    @SqlUpdate("UPDATE address " +
            "SET addressType = :addressType, " +
            "fullName = :fullName, " +
            "phoneNumber = :phoneNumber, " +
            "street = :street, " +
            "city = :city, " +
            "state = :state, " +
            "districtId = :districtId, " +
            "wardCode = :wardCode, " +
            "country = :country " +
            "WHERE id = :id AND userId = :userId")
    int updateAddress(@BindBean Address address);
}
