package controller;

import dto.TaiKhoanHienThiDTO;
import dto.TaiKhoanSuaDTO;
import service.AuthService;
import view.TaiKhoanView;

/**
 * Controller quản lý chức năng xem và sửa thông tin tài khoản dành cho
 * đối tác đang đăng nhập.
 */
public class TaiKhoanController {

	private final AuthService authService = new AuthService();
	private final TaiKhoanView view = new TaiKhoanView();

	/**
	 * Hiển thị thông tin tài khoản và menu con (sửa thông tin / quay lại).
	 * Yêu cầu người dùng phải có quyền đối tác trước khi truy cập.
	 */
	public void quanLyThongTin() {
		try {
			authService.requireDoiTac();
		} catch (IllegalStateException e) {
			view.hienThiThongBao(e.getMessage());
			return;
		}

		while (true) {
			hienThiThongTinHienTai();
			view.hienThiMenu();
			int choice = view.nhapLuaChonMenu();

			switch (choice) {
				case 1:
					suaThongTin();
					break;
				case 0:
					// Quay lại menu đối tác
					return;
				default:
					view.hienThiLuaChonKhongHopLe();
			}
		}
	}

	/** Lấy và hiển thị thông tin tài khoản hiện tại. */
	private void hienThiThongTinHienTai() {
		TaiKhoanHienThiDTO dto = authService.layThongTinTaiKhoan();
		view.hienThiThongTin(dto);
	}

	/** Nhập thông tin sửa và gửi cho service xử lý, hiển thị kết quả. */
	private void suaThongTin() {
		TaiKhoanHienThiDTO hienTai = authService.layThongTinTaiKhoan();
		TaiKhoanSuaDTO input = view.nhapThongTinSua(hienTai);
		view.hienThiKetQua(authService.suaThongTinTaiKhoan(input));
	}
}