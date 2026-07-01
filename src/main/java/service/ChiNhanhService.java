package service;

import java.util.ArrayList;
import java.util.List;

import dao.ChiNhanhDAO;
import model.ChiNhanh;
import model.DoiTac;
import util.FilePath;
import util.IdGenerator;
import util.Validator;

public class ChiNhanhService {

    private final ChiNhanhDAO dao = new ChiNhanhDAO();
    private final DoiTacService doiTacService = new DoiTacService();

    /**
     * Tìm chi nhánh theo mã.
     */
    public ChiNhanh timTheoId(int maChiNhanh) {

        if (!Validator.isPositive(maChiNhanh)) {
            return null;
        }

        return dao.findById(maChiNhanh);
    }

    /**
     * Lấy toàn bộ chi nhánh.
     */
    public List<ChiNhanh> timTatCa() {
        return dao.findAll();
    }

    /**
     * Lấy danh sách chi nhánh của một đối tác.
     */
    public List<ChiNhanh> timTheoMaDoiTac(int maDoiTac) {

        if (!Validator.isPositive(maDoiTac)) {
            return List.of();
        }

        List<ChiNhanh> ketQua = new ArrayList<>();

        for (ChiNhanh cn : timTatCa()) {
            if (cn.getMaDoiTac() == maDoiTac) {
                ketQua.add(cn);
            }
        }

        return ketQua;
    }

    /**
     * Thêm chi nhánh.
     */
    public boolean them(ChiNhanh cn) {

        if (Validator.isNull(cn)) {
            return false;
        }

        if (!Validator.isPositive(cn.getMaDoiTac())) {
            return false;
        }

        if (!Validator.isTenChiNhanh(cn.getTenChiNhanh())) {
            return false;
        }

        if (!Validator.isDiaDiem(cn.getDiaDiem())) {
            return false;
        }

        DoiTac doiTac = doiTacService.timTheoId(cn.getMaDoiTac());

        if (Validator.isNull(doiTac)) {
            return false;
        }

        cn.setMaChiNhanh(IdGenerator.nextID(FilePath.CHI_NHANH));

        return dao.insert(cn);
    }

    /**
     * Cập nhật chi nhánh.
     */
    public boolean sua(ChiNhanh cn) {

        if (Validator.isNull(cn)) {
            return false;
        }

        ChiNhanh old = timTheoId(cn.getMaChiNhanh());

        if (Validator.isNull(old)) {
            return false;
        }

        if (!Validator.isPositive(cn.getMaDoiTac())) {
            return false;
        }

        if (!Validator.isTenChiNhanh(cn.getTenChiNhanh())) {
            return false;
        }

        if (Validator.isDiaDiem(cn.getDiaDiem())) {
            return false;
        }

        if (old.getMaDoiTac() != cn.getMaDoiTac()) {

            if (Validator.isNull(doiTacService.timTheoId(cn.getMaDoiTac()))) {
                return false;
            }
        }

        return dao.update(cn);
    }

    /**
     * Xóa chi nhánh.
     */
    public boolean xoa(int maChiNhanh) {

        if (Validator.isNull(timTheoId(maChiNhanh))) {
            return false;
        }

        return dao.delete(maChiNhanh);
    }
}