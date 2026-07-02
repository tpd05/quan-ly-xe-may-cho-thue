package service.entity;

import java.util.ArrayList;
import java.util.List;

import dao.DonThueDAO;
import model.DonThue;
import model.TaiKhoan;
import util.FilePath;
import util.IdGenerator;
import util.Validator;

public class DonThueService {

    private final DonThueDAO dao = new DonThueDAO();
    private final TaiKhoanService taiKhoanService = new TaiKhoanService();

    /**
     * Tìm đơn thuê theo mã.
     */
    public DonThue timTheoId(int maDonThue) {

        if (!Validator.isPositive(maDonThue)) {
            return null;
        }

        return dao.findById(maDonThue);
    }

    /**
     * Lấy toàn bộ đơn thuê.
     */
    public List<DonThue> timTatCa() {
        return dao.findAll();
    }

    /**
     * Lấy danh sách đơn thuê của một tài khoản.
     */
    public List<DonThue> timTheoUserID(int userID) {

        if (!Validator.isPositive(userID)) {
            return null;
        }

        List<DonThue> res = new ArrayList<>();

        for (DonThue don : timTatCa()) {

            if (don.getUserID() == userID) {
                res.add(don);
            }
        }

        return res;
    }

    /**
     * Thêm đơn thuê.
     */
    public boolean them(DonThue don) {

        if (Validator.isNull(don)) {
            return false;
        }

        if (!Validator.isDiaDiem(don.getDiaChiNhanXe())) {
            return false;
        }

        if (!Validator.isTrangThaiDonThue(don.getTrangThai())) {
            return false;
        }

        if (Validator.isNull(don.getNgayDat())) {
            return false;
        }

        TaiKhoan tk = taiKhoanService.timTheoId(don.getUserID());

        if (Validator.isNull(tk)) {
            return false;
        }

        don.setMaDonThue(IdGenerator.nextID(FilePath.DON_THUE));

        return dao.insert(don);
    }

    /**
     * Cập nhật đơn thuê.
     */
    public boolean sua(DonThue don) {

        if (Validator.isNull(don)) {
            return false;
        }

        DonThue old = timTheoId(don.getMaDonThue());

        if (Validator.isNull(old)) {
            return false;
        }

        if (!Validator.isDiaDiem(don.getDiaChiNhanXe())) {
            return false;
        }

        if (!Validator.isTrangThaiDonThue(don.getTrangThai())) {
            return false;
        }

        if (Validator.isNull(don.getNgayDat())) {
            return false;
        }

        if (old.getUserID() != don.getUserID()) {

            if (Validator.isNull(taiKhoanService.timTheoId(don.getUserID()))) {
                return false;
            }
        }

        return dao.update(don);
    }

    /**
     * Xóa đơn thuê.
     */
    public boolean xoa(int maDonThue) {

        if (Validator.isNull(timTheoId(maDonThue))) {
            return false;
        }

        return dao.delete(maDonThue);
    }

}