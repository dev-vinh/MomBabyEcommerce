package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.VoucherDao;
import hcmuaf.fit.mombabyecommerce.model.Voucher;
import org.jdbi.v3.core.Jdbi;

public class VoucherService {
    private VoucherDao voucherDao;
    public VoucherService (Jdbi jdbi){
        this.voucherDao = jdbi.onDemand(VoucherDao.class);
    }

//tìm kiếm voucher theo code
    public Voucher findByCode(String code){
        return voucherDao.findByCode(code);
    }
    //kiểm tra hợp lệ
    public boolean isValid(Voucher voucher){
        if(voucher == null)
            return false;

        if(!voucher.getActive())
            return false;

        if(voucher.getQuantity() <= 0) return false;
        return true;
    }

}