package view;

import java.util.List;

import dto.ChiTietHienThiDTO;
import dto.DonThueHienThiDTO;
import util.Input;
import util.Result;

/**
 * View xử lý nhập liệu và hiển thị dữ liệu cho chức năng quản lý đơn thuê
 * dành cho đối tác.
 */
public class DoiTacDonThueView {

	/** Hiển thị menu quản lý đơn thuê. */
	public void hienThiMenu() {
		System.out.println("\n========== QUẢN LÝ ĐƠN THUÊ ==========");
		System.out.println("1. Xem danh sách đơn thuê");
		System.out.println("2. Xóa toàn bộ lịch sử đơn thuê");
		System.out.println("0. Quay lại");
	}

	/** Nhập lựa chọn của người dùng từ menu quản lý đơn thuê. */
	public int nhapLuaChonMenu() {
		return Input.inputInt("Chọn chức năng: ");
	}

	/**
	 * Yêu cầu người dùng xác nhận trước khi xóa toàn bộ lịch sử đơn thuê.
	 *
	 * @return true nếu người dùng xác nhận xóa (chọn 1).
	 */
	public boolean xacNhanXoa() {
		int luaChon = Input.inputInt("Bạn có chắc muốn xóa toàn bộ lịch sử đơn thuê? 1. Có / 0. Không: ");
		return luaChon == 1;
	}

	/**
	 * Hiển thị thông báo bất kỳ.
	 *
	 * @param message Nội dung thông báo.
	 */
	public void hienThiThongBao(String message) {
		System.out.println(message);
	}

	/** Hiển thị thông báo khi người dùng chọn lựa chọn không hợp lệ. */
	public void hienThiLuaChonKhongHopLe() {
		System.out.println("Lựa chọn không hợp lệ!");
	}

	/**
	 * Hiển thị kết quả của 1 thao tác nghiệp vụ (thành công/thất bại).
	 *
	 * @param result Kết quả cần hiển thị.
	 */
	public void hienThiKetQua(Result result) {
		System.out.println(result.getMessage());
	}

	/**
	 * Hiển thị danh sách đơn thuê kèm chi tiết (thông tin xe, thời gian thuê,
	 * đơn giá) cho từng đơn.
	 *
	 * @param danhSach Danh sách đơn thuê dạng DTO hiển thị.
	 */
	public void hienThiDanhSachDonThue(List<DonThueHienThiDTO> danhSach) {
		if (danhSach.isEmpty()) {
			System.out.println("Không có đơn thuê nào.");
			return;
		}

		System.out.println("\n===== DANH SÁCH ĐƠN THUÊ =====");
		for (DonThueHienThiDTO item : danhSach) {
			System.out.printf("Mã đơn: %d | Người dùng: %d | Địa chỉ nhận: %s | Trạng thái: %s | Ngày đặt: %s%n",
					item.getDon().getMaDonThue(), item.getDon().getUserID(), item.getDon().getDiaChiNhanXe(),
					item.getDon().getTrangThai(), item.getDon().getNgayDat());

			if (item.getChiTiet().isEmpty()) {
				System.out.println("  - Không có chi tiết đơn thuê.");
			} else {
				for (ChiTietHienThiDTO ct : item.getChiTiet()) {
					// Xe có thể không còn tồn tại (đã bị xóa) -> hiển thị thông báo thay thế
					String thongTinXe = ct.coThongTinXe()
							? String.format("%s %s - %s", ct.getHangXe(), ct.getDongXe(), ct.getBienSo())
							: "Không tìm thấy thông tin xe";

					System.out.printf("  - Xe: %s | Thời gian: %s -> %s | Đơn giá: %d%n",
							thongTinXe, ct.getThoiGianBatDau(), ct.getThoiGianKetThuc(), ct.getDonGia());
				}
			}
		}
	}
}