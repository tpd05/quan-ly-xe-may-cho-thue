package service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dao.DonThueDAO;
import dto.ChiTietHienThiDTO;
import dto.DonThueHienThiDTO;
import model.ChiTietDonThue;
import model.DonThue;
import model.XeMay;
import util.Result;

/**
 * Service xử lý nghiệp vụ đơn thuê dành cho đối tác.
 * Chỉ hiển thị/quản lý các đơn thuê có liên quan đến xe của đối tác hiện tại
 * (xác định qua {@link XeMayService#timTatCa()}).
 */
public class DonThueService {

	private final DonThueDAO dao = new DonThueDAO();
	private final XeMayService xeMayService = new XeMayService();
	private final ChiTietDonThueService chiTietDonThueService = new ChiTietDonThueService();

	/**
	 * Lấy toàn bộ đơn thuê có liên quan đến xe của đối tác hiện tại.
	 * Quy trình: lấy danh sách xe của đối tác -> lấy chi tiết đơn thuê
	 * liên quan đến các xe đó -> suy ra danh sách mã đơn thuê -> lọc đơn thuê.
	 *
	 * @return Danh sách đơn thuê liên quan đến đối tác hiện tại.
	 */
	public List<DonThue> timTatCa() {
		List<Integer> maXeList = layDanhSachMaXeCuaDoiTac();
		if (maXeList.isEmpty()) {
			return List.of();
		}

		List<ChiTietDonThue> chiTietList = chiTietDonThueService.timTheoDanhSachMaXe(maXeList);
		if (chiTietList.isEmpty()) {
			return List.of();
		}

		// Dùng LinkedHashSet để loại trùng mã đơn thuê và giữ thứ tự xuất hiện
		Set<Integer> maDonThueSet = new LinkedHashSet<>();
		for (ChiTietDonThue ct : chiTietList) {
			maDonThueSet.add(ct.getMaDonThue());
		}

		List<DonThue> res = new ArrayList<>();
		for (DonThue don : dao.findAll()) {
			if (maDonThueSet.contains(don.getMaDonThue())) {
				res.add(don);
			}
		}

		return res;
	}

	/**
	 * Lấy danh sách đơn thuê dạng DTO để hiển thị ra view, kèm chi tiết
	 * đơn thuê và thông tin xe (hãng, dòng, biển số) tương ứng.
	 *
	 * @return Danh sách DTO đơn thuê kèm chi tiết hiển thị.
	 */
	public List<DonThueHienThiDTO> layDanhSachHienThi() {
		List<DonThueHienThiDTO> res = new ArrayList<>();

		for (DonThue don : timTatCa()) {
			List<ChiTietHienThiDTO> chiTietHienThi = new ArrayList<>();

			for (ChiTietDonThue ct : chiTietDonThueService.timTheoDonThue(don.getMaDonThue())) {
				XeMay xe = xeMayService.timTheoId(ct.getMaXe());

				// Xe có thể null nếu đã bị xóa khỏi hệ thống -> giữ null thay vì lỗi
				String hangXe = xe != null ? xe.getHangXe() : null;
				String dongXe = xe != null ? xe.getDongXe() : null;
				String bienSo = xe != null ? xe.getBienSo() : null;

				chiTietHienThi.add(new ChiTietHienThiDTO(
						ct.getThoiGianBatDau(),
						ct.getThoiGianKetThuc(),
						ct.getDonGia(),
						hangXe,
						dongXe,
						bienSo));
			}

			res.add(new DonThueHienThiDTO(don, chiTietHienThi));
		}

		return res;
	}

	/**
	 * Xóa toàn bộ lịch sử đơn thuê (và chi tiết liên quan) của đối tác hiện tại.
	 *
	 * @return Result.ok nếu xóa thành công toàn bộ, Result.fail nếu không có
	 *         gì để xóa hoặc xóa thất bại một phần.
	 */
	public Result xoaToanBo() {
		List<DonThue> danhSachDon = timTatCa();
		if (danhSachDon.isEmpty()) {
			return Result.fail("Không có đơn thuê nào để xóa.");
		}

		List<Integer> maDonThueList = new ArrayList<>();
		for (DonThue don : danhSachDon) {
			maDonThueList.add(don.getMaDonThue());
		}

		// Xóa chi tiết trước, sau đó mới xóa đơn thuê để tránh dữ liệu mồ côi
		if (!chiTietDonThueService.xoaTheoDanhSachMaDonThue(maDonThueList)) {
			return Result.fail("Đã xóa lịch sử đơn thuê một phần, vui lòng kiểm tra lại dữ liệu.");
		}

		for (int maDonThue : maDonThueList) {
			if (!dao.delete(maDonThue)) {
				return Result.fail("Đã xóa lịch sử đơn thuê một phần, vui lòng kiểm tra lại dữ liệu.");
			}
		}

		return Result.ok("Đã xóa toàn bộ lịch sử đơn thuê và chi tiết liên quan.");
	}

	/**
	 * Lấy danh sách mã xe thuộc về đối tác hiện tại.
	 * Dùng làm cơ sở để lọc ra các đơn thuê liên quan.
	 *
	 * @return Danh sách mã xe của đối tác hiện tại.
	 */
	private List<Integer> layDanhSachMaXeCuaDoiTac() {
		List<Integer> maXeList = new ArrayList<>();

		for (XeMay xe : xeMayService.timTatCa()) {
			maXeList.add(xe.getMaXe());
		}

		return maXeList;
	}
}