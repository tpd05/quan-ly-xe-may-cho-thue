package service;

import java.util.List;

import dao.DoiTacDAO;
import model.DoiTac;
import model.TaiKhoan;
import util.FilePath;
import util.IdGenerator;
import util.Validator;

public class DoiTacService {

    private final DoiTacDAO dao = new DoiTacDAO();
    private final TaiKhoanService taiKhoanService = new TaiKhoanService();

    /**
     * Tìm đối tác theo mã.
     */
    public DoiTac timTheoId(int maDoiTac) {
        if (!Validator.isPositive(maDoiTac)) {
            return null;
        }

        return dao.findById(maDoiTac);
    }

    /**
     * Lấy toàn bộ đối tác.
     */
    public List<DoiTac> timTatCa() {
        return dao.findAll();
    }

    /**
     * Tìm đối tác theo userID.
     */
    public DoiTac timTheoUserID(int userID) {
        if (!Validator.isPositive(userID)) {
            return null;
        }

        for (DoiTac doiTac : timTatCa()) {
            if (doiTac.getUserID() == userID) {
                return doiTac;
            }
        }

        return null;
    }

    /**
     * Kiểm tra userID đã là đối tác chưa.
     */
    public boolean tonTaiUserID(int userID) {
        return timTheoUserID(userID) != null;
    }

    /**
     * Thêm đối tác.
     */
    public boolean them(DoiTac doiTac) {

        if (Validator.isNull(doiTac)) {
            return false;
        }

        if (!Validator.isPositive(doiTac.getUserID())) {
            return false;
        }

        if (tonTaiUserID(doiTac.getUserID())) {
            return false;
        }
        
        TaiKhoan tk = taiKhoanService.timTheoId(doiTac.getUserID());

        if (tk == null) {
            return false;
        }

        if (tk.getRole() != TaiKhoan.Role.DOI_TAC) {
            return false;
        }

        doiTac.setMaDoiTac(IdGenerator.nextID(FilePath.DOI_TAC));

        return dao.insert(doiTac);
    }

    /**
     * Cập nhật thông tin đối tác.
     */
    public boolean sua(DoiTac doiTac) {

        if (Validator.isNull(doiTac)) {
            return false;
        }

        DoiTac old = timTheoId(doiTac.getMaDoiTac());

        if (Validator.isNull(old)) {
            return false;
        }

        if (!Validator.isPositive(doiTac.getUserID())) {
            return false;
        }

        // Không cho phép đổi userID sang tài khoản đã là đối tác khác
        if (old.getUserID() != doiTac.getUserID()) {

            if (tonTaiUserID(doiTac.getUserID())) {
                return false;
            }
        }

        return dao.update(doiTac);
    }

    /**
     * Xóa đối tác.
     */
    public boolean xoa(int maDoiTac) {

        if (timTheoId(maDoiTac) == null) {
            return false;
        }

        return dao.delete(maDoiTac);
    }

}