package controller;

import session.Session;
import view.DoiTacMenuView;

/**
 * Controller điều hướng menu chức năng dành cho đối tác sau khi đăng nhập.
 * Cho phép: quản lý xe, quản lý đơn thuê, xem/sửa thông tin tài khoản,
 * và đăng xuất.
 */
public class DoiTacMenuController {

	private final DoiTacMenuView view = new DoiTacMenuView();
	private final DoiTacXeController doiTacXeController = new DoiTacXeController();
	private final DoiTacDonThueController doiTacDonThueController = new DoiTacDonThueController();
	private final TaiKhoanController taiKhoanController = new TaiKhoanController();
	private final AuthController auth = new AuthController();

	/**
	 * Hiển thị menu đối tác và xử lý lựa chọn.
	 * Vòng lặp chỉ tiếp tục khi người dùng vẫn đang đăng nhập với vai trò đối tác.
	 */
	public void show() {
		while (Session.isLoggedIn() && Session.isDoiTac()) {
			view.hienThiMenu();
			int choice = view.nhapLuaChon();

			switch (choice) {
				case 1:
					doiTacXeController.quanLyXe();
					break;
				case 2:
					doiTacDonThueController.quanLyDonThue();
					break;
				case 3:
					taiKhoanController.quanLyThongTin();
					break;
				case 4:
					// Đăng xuất và thoát khỏi menu đối tác
					auth.dangXuat();
					view.hienThiDangXuatThanhCong();
					return;
				default:
					view.hienThiLuaChonKhongHopLe();
			}
		}
	}
}