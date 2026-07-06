package controller;

import session.Session;
import view.MainMenuView;

/**
 * Controller điều hướng menu chính của ứng dụng.
 * Xử lý luồng: đăng nhập -> kiểm tra vai trò -> chuyển vào menu tương ứng,
 * hoặc thoát ứng dụng.
 */
public class MainMenuController {

	private final MainMenuView view = new MainMenuView();
	private final AuthController auth = new AuthController();
	private final DoiTacMenuController doiTacMenuController = new DoiTacMenuController();

	/**
	 * Hiển thị menu chính và lặp lại cho tới khi người dùng chọn thoát.
	 */
	public void show() {
		while (true) {
			view.hienThiMenu();
			int choice = view.nhapLuaChon();

			switch (choice) {
				case 1:
					xuLyDangNhap();
					break;
				case 0:
					// Thoát ứng dụng
					view.hienThiTamBiet();
					return;
				default:
					view.hienThiLuaChonKhongHopLe();
			}
		}
	}

	/**
	 * Xử lý đăng nhập và điều hướng theo vai trò:
	 * - Đăng nhập thất bại (sai tài khoản/mật khẩu...): AuthController đã hiển
	 *   thị thông báo lỗi cụ thể, không cần làm gì thêm ở đây.
	 * - Đăng nhập thành công và là đối tác: chuyển vào menu đối tác.
	 * - Đăng nhập thành công nhưng không phải đối tác (dự phòng cho tương lai
	 *   khi hệ thống có thêm vai trò khác): xóa phiên và báo lỗi quyền truy cập.
	 */
	private void xuLyDangNhap() {
		boolean dangNhapThanhCong = auth.dangNhap();

		if (!dangNhapThanhCong) {
			// Thông báo lỗi cụ thể (sai tài khoản/mật khẩu, ...) đã được
			// AuthController hiển thị ở LoginView, không hiển thị thêm ở đây
			// để tránh 2 thông báo mâu thuẫn nhau.
			return;
		}

		if (Session.isLoggedIn() && Session.isDoiTac()) {
			doiTacMenuController.show();
		} else {
			Session.clear();
			view.hienThiLoiQuyen();
		}
	}
}