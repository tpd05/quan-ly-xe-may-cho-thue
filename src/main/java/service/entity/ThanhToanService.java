package service.entity;

import java.util.ArrayList;
import java.util.List;

import dao.ThanhToanDAO;
import model.DonThue;
import model.ThanhToan;
import util.FilePath;
import util.IdGenerator;
import util.Validator;

public class ThanhToanService {

    private final ThanhToanDAO dao = new ThanhToanDAO();
    private final DonThueService donThueService = new DonThueService();

    /**
     * Tìm thanh toán theo mã.
     */
    public ThanhToan timTheoId(int maThanhToan) {

        if (!Validator.isPositive(maThanhToan)) {
            return null;
        }

        return dao.findById(maThanhToan);
    }

    /**
     * Lấy toàn bộ thanh toán.
     */
    public List<ThanhToan> timTatCa() {
        return dao.findAll();
    }

    /**
     * Tìm thanh toán theo mã đơn thuê.
     */
    public ThanhToan timTheoDonThue(int maDonThue) {

        if (!Validator.isPositive(maDonThue)) {
            return null;
        }

        for (ThanhToan thanhToan : timTatCa()) {
            if (thanhToan.getMaDonThue() == maDonThue) {
                return thanhToan;
            }
        }

        return null;
    }

    /**
     * Lấy tất cả thanh toán theo trạng thái.
     */
    public List<ThanhToan> timTheoTrangThai(ThanhToan.TrangThai trangThai) {

        if (Validator.isNull(trangThai)) {
            return null;
        }

        List<ThanhToan> res = new ArrayList<>();

        for (ThanhToan thanhToan : timTatCa()) {
            if (thanhToan.getTrangThai() == trangThai) {
                res.add(thanhToan);
            }
        }

        return res;
    }

    /**
     * Thêm thanh toán.
     */
    public boolean them(ThanhToan thanhToan) {

        if (Validator.isNull(thanhToan)) {
            return false;
        }

        if (!Validator.isPositive(thanhToan.getSoTien())) {
            return false;
        }

        if (!Validator.isPhuongThucThanhToan(thanhToan.getPhuongThuc())) {
            return false;
        }

        if (Validator.isNull(thanhToan.getThoiGianTao())) {
            return false;
        }

        if (Validator.isNull(thanhToan.getTrangThai())) {
            return false;
        }

        DonThue donThue = donThueService.timTheoId(thanhToan.getMaDonThue());

        if (Validator.isNull(donThue)) {
            return false;
        }

        if (timTheoDonThue(thanhToan.getMaDonThue()) != null) {
            return false;
        }

        thanhToan.setMaThanhToan(IdGenerator.nextID(FilePath.THANH_TOAN));

        return dao.insert(thanhToan);
    }

    /**
     * Cập nhật thanh toán.
     */
    public boolean sua(ThanhToan thanhToan) {

        if (Validator.isNull(thanhToan)) {
            return false;
        }

        ThanhToan old = timTheoId(thanhToan.getMaThanhToan());

        if (Validator.isNull(old)) {
            return false;
        }

        if (!Validator.isPositive(thanhToan.getSoTien())) {
            return false;
        }

        if (!Validator.isPhuongThucThanhToan(thanhToan.getPhuongThuc())) {
            return false;
        }

        if (Validator.isNull(thanhToan.getThoiGianTao())) {
            return false;
        }

        if (Validator.isNull(thanhToan.getTrangThai())) {
            return false;
        }

        if (old.getMaDonThue() != thanhToan.getMaDonThue()) {

            if (Validator.isNull(donThueService.timTheoId(thanhToan.getMaDonThue()))) {
                return false;
            }

            if (timTheoDonThue(thanhToan.getMaDonThue()) != null) {
                return false;
            }
        }

        return dao.update(thanhToan);
    }

    /**
     * Xóa thanh toán.
     */
    public boolean xoa(int maThanhToan) {

        if (Validator.isNull(timTheoId(maThanhToan))) {
            return false;
        }

        return dao.delete(maThanhToan);
    }
}