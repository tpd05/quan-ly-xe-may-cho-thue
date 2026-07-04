package controller;

import service.AuthService;
import view.LoginView;

/**
 * Controller xử lý nghiệp vụ xác thực: đăng nhập và đăng xuất.
 */
public class AuthController {

	private final AuthService authService = new AuthService();
	private final LoginView view = new LoginView();

	/**
	 * Thực hiện đăng nhập: nhập username/password, gọi service xác thực
	 * và hiển thị kết quả tương ứng cho người dùng.
	 */
	public void dangNhap() {
		try {
			String username = view.nhapUsername();
			String password = view.nhapPassword();
			authService.dangNhap(username, password);
			view.thongBaoThanhCong();
		} catch (IllegalArgumentException e) {
			view.thongBaoLoi(e.getMessage());
		}
	}

	/** Thực hiện đăng xuất, xóa thông tin phiên đăng nhập hiện tại. */
	public void dangXuat() {
		authService.dangXuat();
	}
}