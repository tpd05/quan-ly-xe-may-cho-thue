package controller;

import java.util.List;

import model.XeMay;
import service.AuthService;
import service.XeMayService;
import view.DoiTacXeView;

/**
 * Controller quản lý nghiệp vụ xe máy dành cho đối tác.
 * Cho phép: xem danh sách, thêm, sửa, xóa và tìm kiếm xe.
 */
public class DoiTacXeController {

	private final AuthService authService = new AuthService();
	private final XeMayService xeMayService = new XeMayService();
	private final DoiTacXeView view = new DoiTacXeView();

	/**
	 * Hiển thị menu quản lý xe và xử lý các lựa chọn của người dùng.
	 * Yêu cầu người dùng phải có quyền đối tác trước khi truy cập.
	 */
	public void quanLyXe() {
		try {
			authService.requireDoiTac();
		} catch (IllegalStateException e) {
			view.hienThiThongBao(e.getMessage());
			return;
		}

		while (true) {
			view.hienThiMenu();
			int choice = view.nhapLuaChonMenu();

			switch (choice) {
				case 1:
					hienThiTatCaXe();
					break;
				case 2:
					themXe();
					break;
				case 3:
					suaXe();
					break;
				case 4:
					xoaXe();
					break;
				case 5:
					timKiemXe();
					break;
				case 0:
					// Thoát khỏi menu quản lý xe
					return;
				default:
					view.hienThiLuaChonKhongHopLe();
			}
		}
	}

	/** Hiển thị toàn bộ danh sách xe hiện có. */
	private void hienThiTatCaXe() {
		List<XeMay> danhSachXe = xeMayService.timTatCa();
		view.hienThiDanhSachXe(danhSachXe);
	}

	/** Nhập thông tin xe mới từ người dùng và thêm vào hệ thống. */
	private void themXe() {
		XeMay xe = view.nhapThongTinXeMoi();
		view.hienThiKetQua(xeMayService.them(xe));
	}

	/** Sửa thông tin xe theo mã xe được nhập. */
	private void suaXe() {
		int maXe = view.nhapMaXe("sửa");
		XeMay xe = xeMayService.timTheoId(maXe);

		if (xe == null) {
			view.hienThiThongBao("Không tìm thấy xe.");
			return;
		}

		XeMay xeMoi = view.nhapThongTinXeSua(xe);
		view.hienThiKetQua(xeMayService.sua(xeMoi));
	}

	/** Xóa xe theo mã xe được nhập. */
	private void xoaXe() {
		int maXe = view.nhapMaXe("xóa");
		view.hienThiKetQua(xeMayService.xoa(maXe));
	}

	/** Tìm kiếm xe theo từ khóa và hiển thị kết quả. */
	private void timKiemXe() {
		String tuKhoa = view.nhapTuKhoaTimKiem();
		List<XeMay> ketQua = xeMayService.timKiem(tuKhoa);
		view.hienThiDanhSachXe(ketQua);
	}
}