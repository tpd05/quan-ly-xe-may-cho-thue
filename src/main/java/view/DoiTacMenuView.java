package view;

import util.Input;

/**
 * View hiển thị menu chức năng dành cho đối tác sau khi đăng nhập.
 */
public class DoiTacMenuView {

	/** Hiển thị menu đối tác. */
	public void hienThiMenu() {
		System.out.println("\n========== MENU ĐỐI TÁC ==========");
		System.out.println("1. Quản lý xe");
		System.out.println("2. Quản lý đơn thuê");
		System.out.println("3. Thông tin tài khoản");
		System.out.println("4. Đăng xuất");
	}

	/** Nhập lựa chọn của người dùng từ menu đối tác. */
	public int nhapLuaChon() {
		return Input.inputInt("Chọn chức năng: ");
	}

	/** Hiển thị thông báo đăng xuất thành công. */
	public void hienThiDangXuatThanhCong() {
		System.out.println("Đăng xuất thành công.");
	}

	/** Hiển thị thông báo khi người dùng chọn lựa chọn không hợp lệ. */
	public void hienThiLuaChonKhongHopLe() {
		System.out.println("Lựa chọn không hợp lệ!");
	}
}