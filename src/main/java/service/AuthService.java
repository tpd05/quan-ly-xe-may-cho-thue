package service;

import dao.TaiKhoanDAO;
import model.TaiKhoan;
import session.Session;

/**
 * Service xử lý nghiệp vụ xác thực: đăng nhập, đăng xuất và kiểm tra quyền
 * truy cập theo vai trò đối tác.
 */
public class AuthService {

	private final TaiKhoanDAO dao = new TaiKhoanDAO();

	/**
	 * Đăng nhập vào hệ thống với vai trò đối tác.
	 *
	 * @param username Tên đăng nhập.
	 * @param password Mật khẩu.
	 * @return Tài khoản đã đăng nhập thành công.
	 * @throws IllegalArgumentException nếu tài khoản không tồn tại, sai mật khẩu,
	 *                                   hoặc không có quyền đối tác.
	 */
	public TaiKhoan dangNhap(String username, String password) {
		TaiKhoan taiKhoan = dao.findByUsername(username);

		if (taiKhoan == null) {
			throw new IllegalArgumentException("Tài khoản không tồn tại.");
		}

		if (!taiKhoan.getPassword().equals(password)) {
			throw new IllegalArgumentException("Sai mật khẩu.");
		}

		if (taiKhoan.getRole() != TaiKhoan.Role.DOI_TAC) {
			throw new IllegalArgumentException("Tài khoản không có quyền truy cập.");
		}

		Session.setCurrentUser(taiKhoan);
		return taiKhoan;
	}

	/** Đăng xuất, xóa thông tin phiên đăng nhập hiện tại. */
	public void dangXuat() {
		Session.clear();
	}

	/**
	 * Kiểm tra người dùng hiện tại có đang đăng nhập với vai trò đối tác hay không.
	 * Dùng để bảo vệ các chức năng chỉ dành cho đối tác.
	 *
	 * @throws IllegalStateException nếu chưa đăng nhập hoặc không phải đối tác.
	 */
	public void requireDoiTac() {
		if (!Session.isLoggedIn() || !Session.isDoiTac()) {
			throw new IllegalStateException("Bạn cần đăng nhập với vai trò đối tác.");
		}
	}
}