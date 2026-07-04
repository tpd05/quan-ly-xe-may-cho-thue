package view;

import util.Input;
import util.Validator;

/**
 * View xử lý nhập liệu và hiển thị thông báo cho chức năng đăng nhập.
 */
public class LoginView {

	/**
	 * Nhập username. Yêu cầu nhập lại nếu không hợp lệ.
	 *
	 * @return Username hợp lệ.
	 */
	public String nhapUsername() {
		while (true) {
			String username = Input.inputString("Username: ");

			if (Validator.isUserName(username)) {
				return username;
			}

			System.out.println("Username không hợp lệ!\n"
					+ "Username phải từ 3 - 20 ký tự, chỉ gồm chữ cái, số và dấu gạch dưới.");
		}
	}

	/**
	 * Nhập password. Yêu cầu nhập lại nếu không hợp lệ.
	 *
	 * @return Password hợp lệ.
	 */
	public String nhapPassword() {
		while (true) {
			String password = Input.inputString("Password: ");

			if (Validator.isPassword(password)) {
				return password;
			}

			System.out.println("Password không hợp lệ!\n" + "Password phải có ít nhất 8 ký tự.");
		}
	}

	/** Hiển thị thông báo đăng nhập thành công. */
	public void thongBaoThanhCong() {
		System.out.println("Đăng nhập thành công.");
	}

	/**
	 * Hiển thị thông báo lỗi.
	 *
	 * @param message Nội dung lỗi.
	 */
	public void thongBaoLoi(String message) {
		System.out.println(message);
	}
}