package service;

import java.util.ArrayList;
import java.util.List;

import dao.XeMayDAO;
import model.ChiNhanh;
import model.XeMay;
import util.FilePath;
import util.IdGenerator;
import util.Validator;

public class XeMayService {

    private final XeMayDAO dao = new XeMayDAO();
    private final ChiNhanhService chiNhanhService = new ChiNhanhService();

    /**
     * Tìm xe theo mã.
     */
    public XeMay timTheoId(int maXe) {

        if (!Validator.isPositive(maXe)) {
            return null;
        }

        return dao.findById(maXe);
    }
    
    /**
     * Lấy toàn bộ xe.
     */
    public List<XeMay> timTatCa() {
        return dao.findAll();
    }
    
    /**
     * Lấy danh sách xe của một chi nhánh.
     */
    public List<XeMay> timTheoChiNhanh(int maChiNhanh) {

        if (!Validator.isPositive(maChiNhanh)) {
            return null;
        }

        List<XeMay> res = new ArrayList<>();

        for (XeMay xe : timTatCa()) {
            if (xe.getMaChiNhanh() == maChiNhanh) {
                res.add(xe);
            }
        }

        return res;
    }
    
    /**
     * Tìm xe theo biển số.
     */
    public XeMay timTheoBienSo(String bienSo) {

        if (!Validator.isBienSo(bienSo)) {
            return null;
        }

        for (XeMay xe : timTatCa()) {
            if (xe.getBienSo().equalsIgnoreCase(bienSo)) {
                return xe;
            }
        }

        return null;
    }
    
    /**
     * Kiểm tra biển số đã tồn tại.
     */
    public boolean tonTaiBienSo(String bienSo) {
        return timTheoBienSo(bienSo) != null;
    }
    
    /**
     * Thêm xe máy.
     */
    public boolean them(XeMay xe) {

        if (Validator.isNull(xe)) {
            return false;
        }

        if (!Validator.isHangXe(xe.getHangXe())) {
            return false;
        }

        if (!Validator.isDongXe(xe.getDongXe())) {
            return false;
        }

        if (!Validator.isNamSanXuat(xe.getNamSanXuat())) {
            return false;
        }

        if (!Validator.isDungTich(xe.getDungTich())) {
            return false;
        }

        if (!Validator.isBienSo(xe.getBienSo())) {
            return false;
        }

        if (!Validator.isPositive(xe.getGiaNgay())) {
            return false;
        }

        if (!Validator.isPositive(xe.getGiaTuan())) {
            return false;
        }

        if (tonTaiBienSo(xe.getBienSo())) {
            return false;
        }

        ChiNhanh cn = chiNhanhService.timTheoId(xe.getMaChiNhanh());

        if (Validator.isNull(cn)) {
            return false;
        }

        xe.setMaXe(IdGenerator.nextID(FilePath.XE_MAY));

        return dao.insert(xe);
    }
    
    /**
     * Cập nhật xe máy.
     */
    public boolean sua(XeMay xe) {

        if (Validator.isNull(xe)) {
            return false;
        }

        XeMay old = timTheoId(xe.getMaXe());

        if (Validator.isNull(old)) {
            return false;
        }

        if (!Validator.isHangXe(xe.getHangXe())) {
            return false;
        }

        if (!Validator.isDongXe(xe.getDongXe())) {
            return false;
        }

        if (!Validator.isNamSanXuat(xe.getNamSanXuat())) {
            return false;
        }

        if (!Validator.isDungTich(xe.getDungTich())) {
            return false;
        }

        if (!Validator.isBienSo(xe.getBienSo())) {
            return false;
        }

        if (!Validator.isPositive(xe.getGiaNgay())) {
            return false;
        }

        if (!Validator.isPositive(xe.getGiaTuan())) {
            return false;
        }

        if (!old.getBienSo().equalsIgnoreCase(xe.getBienSo())) {

            if (tonTaiBienSo(xe.getBienSo())) {
                return false;
            }
        }

        if (old.getMaChiNhanh() != xe.getMaChiNhanh()) {

            if (Validator.isNull(chiNhanhService.timTheoId(xe.getMaChiNhanh()))) {
                return false;
            }
        }

        return dao.update(xe);
    }
    
    /**
     * Xóa xe.
     */
    public boolean xoa(int maXe) {

        if (Validator.isNull(timTheoId(maXe))) {
            return false;
        }

        return dao.delete(maXe);
    }
}