package controller;

import service.AuthService;
import service.DonThueService;
import view.DoiTacDonThueView;

/**
 * Controller quản lý nghiệp vụ đơn thuê dành cho đối tác.
 * Cho phép: xem danh sách đơn thuê và xóa toàn bộ lịch sử đơn thuê.
 */
public class DoiTacDonThueController {

	private final AuthService authService = new AuthService();
	private final DonThueService donThueService = new DonThueService();
	private final DoiTacDonThueView view = new DoiTacDonThueView();

	/**
	 * Hiển thị menu quản lý đơn thuê và xử lý các lựa chọn của người dùng.
	 * Yêu cầu người dùng phải có quyền đối tác trước khi truy cập.
	 */
	public void quanLyDonThue() {
		try {
			authService.requireDoiTac();
		} catch (IllegalStateException e) {
			view.hienThiThongBao(e.getMessage());
			return;
		}

		while (true) {
			view.hienThiMenu();
			int choice = view.nhapLuaChonMenu();

			switch (choice) {
				case 1:
					hienThiTatCaDonThue();
					break;
				case 2:
					xoaLichSuDonThue();
					break;
				case 0:
					// Thoát khỏi menu quản lý đơn thuê
					return;
				default:
					view.hienThiLuaChonKhongHopLe();
			}
		}
	}

	/** Hiển thị toàn bộ danh sách đơn thuê hiện có. */
	private void hienThiTatCaDonThue() {
		view.hienThiDanhSachDonThue(donThueService.layDanhSachHienThi());
	}

	/** Xóa toàn bộ lịch sử đơn thuê sau khi người dùng xác nhận. */
	private void xoaLichSuDonThue() {
		if (!view.xacNhanXoa()) {
			view.hienThiThongBao("Đã hủy thao tác.");
			return;
		}

		view.hienThiKetQua(donThueService.xoaToanBo());
	}
}