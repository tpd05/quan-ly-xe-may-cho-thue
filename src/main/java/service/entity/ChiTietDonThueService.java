package service.entity;

import java.util.ArrayList;
import java.util.List;

import dao.ChiTietDonThueDAO;
import model.ChiTietDonThue;
import model.DonThue;
import model.XeMay;
import util.FilePath;
import util.IdGenerator;
import util.Validator;

public class ChiTietDonThueService {

	private final ChiTietDonThueDAO dao = new ChiTietDonThueDAO();
	private final DonThueService donThueService = new DonThueService();
	private final XeMayService xeMayService = new XeMayService();

	/**
	 * Tìm chi tiết đơn thuê theo mã.
	 */
	public ChiTietDonThue timTheoId(int maChiTiet) {

		if (!Validator.isPositive(maChiTiet)) {
			return null;
		}

		return dao.findById(maChiTiet);
	}

	/**
	 * Lấy toàn bộ chi tiết đơn thuê.
	 */
	public List<ChiTietDonThue> timTatCa() {
		return dao.findAll();
	}

	/**
	 * Lấy danh sách chi tiết của một đơn thuê.
	 */
	public List<ChiTietDonThue> timTheoDonThue(int maDonThue) {

		if (!Validator.isPositive(maDonThue)) {
			return null;
		}

		List<ChiTietDonThue> res = new ArrayList<>();

		for (ChiTietDonThue ct : timTatCa()) {

			if (ct.getMaDonThue() == maDonThue) {
				res.add(ct);
			}
		}

		return res;
	}

	/**
	 * Thêm chi tiết đơn thuê.
	 */
	public boolean them(ChiTietDonThue ct) {

		if (Validator.isNull(ct)) {
			return false;
		}

		if (!Validator.isPositive(ct.getDonGia())) {
			return false;
		}

		if (!Validator.isValidDateRange(ct.getThoiGianBatDau(), ct.getThoiGianKetThuc())) {
			return false;
		}

		if (ct.getThoiGianTra() != null && ct.getThoiGianTra().isBefore(ct.getThoiGianBatDau())) {
			return false;
		}

		DonThue donThue = donThueService.timTheoId(ct.getMaDonThue());

		if (Validator.isNull(donThue)) {
			return false;
		}

		XeMay xe = xeMayService.timTheoId(ct.getMaXe());

		if (Validator.isNull(xe)) {
			return false;
		}

		ct.setMaChiTiet(IdGenerator.nextID(FilePath.CHI_TIET_DON_THUE));

		return dao.insert(ct);
	}

	/**
	 * Cập nhật chi tiết đơn thuê.
	 */
	public boolean sua(ChiTietDonThue ct) {

		if (Validator.isNull(ct)) {
			return false;
		}

		ChiTietDonThue old = timTheoId(ct.getMaChiTiet());

		if (Validator.isNull(old)) {
			return false;
		}

		if (!Validator.isPositive(ct.getDonGia())) {
			return false;
		}

		if (!Validator.isValidDateRange(ct.getThoiGianBatDau(), ct.getThoiGianKetThuc())) {
			return false;
		}

		if (ct.getThoiGianTra() != null && Validator.isValidDateRange(ct.getThoiGianTra(), ct.getThoiGianBatDau())) {
			return false;
		}

		if (old.getMaDonThue() != ct.getMaDonThue()) {

			if (Validator.isNull(donThueService.timTheoId(ct.getMaDonThue()))) {
				return false;
			}
		}

		if (old.getMaXe() != ct.getMaXe()) {

			if (Validator.isNull(xeMayService.timTheoId(ct.getMaXe()))) {
				return false;
			}
		}

		return dao.update(ct);
	}

	/**
	 * Xóa chi tiết đơn thuê.
	 */
	public boolean xoa(int maChiTiet) {

		if (Validator.isNull(timTheoId(maChiTiet))) {
			return false;
		}

		return dao.delete(maChiTiet);
	}

}