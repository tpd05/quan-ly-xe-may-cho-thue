package view;

import util.Input;

/**
 * View hiển thị menu chính của ứng dụng.
 */
public class MainMenuView {

	/** Hiển thị menu chính. */
	public void hienThiMenu() {
		System.out.println("\n========== HỆ THỐNG QUẢN LÝ THUÊ XE ==========");
		System.out.println("1. Đăng nhập đối tác");
		System.out.println("0. Thoát");
	}

	/** Nhập lựa chọn của người dùng từ menu chính. */
	public int nhapLuaChon() {
		return Input.inputInt("Chọn: ");
	}

	/** Hiển thị thông báo tạm biệt khi thoát ứng dụng. */
	public void hienThiTamBiet() {
		System.out.println("Tạm biệt!");
	}

	/** Hiển thị thông báo khi tài khoản đăng nhập không có quyền đối tác. */
	public void hienThiLoiQuyen() {
		System.out.println("Chỉ tài khoản đối tác mới được phép truy cập.");
	}

	/** Hiển thị thông báo khi người dùng chọn lựa chọn không hợp lệ. */
	public void hienThiLuaChonKhongHopLe() {
		System.out.println("Lựa chọn không hợp lệ!");
	}
}