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
	 * - Nếu là đối tác: chuyển vào menu đối tác.
	 * - Nếu không: xóa phiên đăng nhập và báo lỗi quyền truy cập.
	 */
	private void xuLyDangNhap() {
		auth.dangNhap();

		if (Session.isLoggedIn() && Session.isDoiTac()) {
			doiTacMenuController.show();
		} else {
			Session.clear();
			view.hienThiLoiQuyen();
		}
	}
}