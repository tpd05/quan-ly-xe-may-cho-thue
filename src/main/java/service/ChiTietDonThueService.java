package service;

import java.util.ArrayList;
import java.util.List;

import dao.ChiTietDonThueDAO;
import model.ChiTietDonThue;
import util.Validator;

/**
 * Service xử lý nghiệp vụ chi tiết đơn thuê: tìm kiếm theo đơn thuê / xe,
 * và xóa theo lô (batch).
 */
public class ChiTietDonThueService {

	private final ChiTietDonThueDAO dao = new ChiTietDonThueDAO();

	/**
	 * Lấy toàn bộ danh sách chi tiết đơn thuê.
	 *
	 * @return Danh sách tất cả chi tiết đơn thuê.
	 */
	public List<ChiTietDonThue> timTatCa() {
		return dao.findAll();
	}

	/**
	 * Tìm các chi tiết thuộc về 1 đơn thuê cụ thể.
	 *
	 * @param maDonThue Mã đơn thuê.
	 * @return Danh sách chi tiết tương ứng, rỗng nếu mã không hợp lệ.
	 */
	public List<ChiTietDonThue> timTheoDonThue(int maDonThue) {
		if (!Validator.isPositive(maDonThue)) {
			return List.of();
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
	 * Tìm các chi tiết liên quan đến 1 xe cụ thể.
	 *
	 * @param maXe Mã xe.
	 * @return Danh sách chi tiết tương ứng, rỗng nếu mã không hợp lệ.
	 */
	public List<ChiTietDonThue> timTheoMaXe(int maXe) {
		if (!Validator.isPositive(maXe)) {
			return List.of();
		}

		List<ChiTietDonThue> res = new ArrayList<>();

		for (ChiTietDonThue ct : timTatCa()) {
			if (ct.getMaXe() == maXe) {
				res.add(ct);
			}
		}

		return res;
	}

	/**
	 * Tìm các chi tiết liên quan đến 1 danh sách mã xe.
	 * Dùng để lấy toàn bộ chi tiết đơn thuê thuộc về các xe của 1 đối tác.
	 *
	 * @param danhSachMaXe Danh sách mã xe cần tìm.
	 * @return Danh sách chi tiết tương ứng, rỗng nếu danh sách đầu vào rỗng.
	 */
	public List<ChiTietDonThue> timTheoDanhSachMaXe(List<Integer> danhSachMaXe) {
		if (danhSachMaXe == null || danhSachMaXe.isEmpty()) {
			return List.of();
		}

		List<ChiTietDonThue> res = new ArrayList<>();

		for (ChiTietDonThue ct : timTatCa()) {
			if (danhSachMaXe.contains(ct.getMaXe())) {
				res.add(ct);
			}
		}

		return res;
	}

	/**
	 * Xóa toàn bộ chi tiết thuộc về 1 đơn thuê.
	 *
	 * @param maDonThue Mã đơn thuê cần xóa chi tiết.
	 * @return true nếu có ít nhất 1 chi tiết bị xóa.
	 */
	public boolean xoaTheoMaDonThue(int maDonThue) {
		if (!Validator.isPositive(maDonThue)) {
			return false;
		}

		List<ChiTietDonThue> all = new ArrayList<>(timTatCa());
		boolean removed = all.removeIf(ct -> ct.getMaDonThue() == maDonThue);

		if (!removed) {
			return false;
		}

		saveAll(all);
		return true;
	}

	/**
	 * Xóa toàn bộ chi tiết thuộc về 1 danh sách đơn thuê (xóa theo lô).
	 *
	 * @param danhSachMaDonThue Danh sách mã đơn thuê cần xóa chi tiết.
	 * @return true nếu có ít nhất 1 chi tiết bị xóa.
	 */
	public boolean xoaTheoDanhSachMaDonThue(List<Integer> danhSachMaDonThue) {
		if (danhSachMaDonThue == null || danhSachMaDonThue.isEmpty()) {
			return false;
		}

		List<ChiTietDonThue> all = new ArrayList<>(timTatCa());
		boolean removed = all.removeIf(ct -> danhSachMaDonThue.contains(ct.getMaDonThue()));

		if (!removed) {
			return false;
		}

		saveAll(all);
		return true;
	}

	/** Ghi đè toàn bộ danh sách chi tiết đơn thuê xuống file. */
	private void saveAll(List<ChiTietDonThue> chiTietList) {
		dao.replaceAll(chiTietList);
	}
}