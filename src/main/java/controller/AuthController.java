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
	 *
	 * @return true nếu đăng nhập thành công (tài khoản, mật khẩu và vai trò
	 *         hợp lệ), false nếu thất bại (sai tài khoản/mật khẩu hoặc
	 *         không có quyền đối tác) — trong trường hợp false, thông báo
	 *         lỗi tương ứng đã được hiển thị cho người dùng ngay tại đây.
	 */
	public boolean dangNhap() {
		try {
			String username = view.nhapUsername();
			String password = view.nhapPassword();
			authService.dangNhap(username, password);
			view.thongBaoThanhCong();
			return true;
		} catch (IllegalArgumentException e) {
			view.thongBaoLoi(e.getMessage());
			return false;
		}
	}

	/** Thực hiện đăng xuất, xóa thông tin phiên đăng nhập hiện tại. */
	public void dangXuat() {
		authService.dangXuat();
	}
}