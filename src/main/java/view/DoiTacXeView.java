package view;

import java.math.BigDecimal;
import java.util.List;

import model.XeMay;
import util.Input;
import util.Result;
import util.Validator;

/**
 * View xử lý nhập liệu và hiển thị dữ liệu cho chức năng quản lý xe máy
 * dành cho đối tác.
 */
public class DoiTacXeView {

	/** Hiển thị menu quản lý xe. */
	public void hienThiMenu() {
		System.out.println("\n========== QUẢN LÝ XE ==========");
		System.out.println("1. Xem danh sách xe");
		System.out.println("2. Thêm xe mới");
		System.out.println("3. Sửa thông tin xe");
		System.out.println("4. Xóa xe");
		System.out.println("5. Tìm kiếm xe");
		System.out.println("0. Quay lại menu đối tác");
	}

	/** Nhập lựa chọn của người dùng từ menu quản lý xe. */
	public int nhapLuaChonMenu() {
		return Input.inputInt("Chọn chức năng: ");
	}

	/**
	 * Nhập mã xe cần thao tác (sửa/xóa).
	 *
	 * @param hanhDong Tên hành động, dùng để hiển thị trong câu hỏi (ví dụ: "sửa", "xóa").
	 * @return Mã xe đã nhập.
	 */
	public int nhapMaXe(String hanhDong) {
		return Input.inputInt("Nhập mã xe cần " + hanhDong + ": ");
	}

	/**
	 * Nhập từ khóa tìm kiếm xe (theo biển số, hãng xe, hoặc dòng xe).
	 *
	 * @return Từ khóa đã nhập.
	 */
	public String nhapTuKhoaTimKiem() {
		return Input.inputString("Nhập biển số hoặc từ khóa tìm kiếm: ");
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
	 * Hiển thị danh sách xe.
	 *
	 * @param danhSachXe Danh sách xe cần hiển thị.
	 */
	public void hienThiDanhSachXe(List<XeMay> danhSachXe) {
		if (danhSachXe.isEmpty()) {
			System.out.println("Không có xe nào.");
			return;
		}

		System.out.println("\n===== DANH SÁCH XE =====");
		for (XeMay xe : danhSachXe) {
			System.out.printf("Mã xe: %d | %s %s | Biển số: %s | Trạng thái: %s | Giá/ngày: %s | Giá/tuần: %s%n",
					xe.getMaXe(), xe.getHangXe(), xe.getDongXe(), xe.getBienSo(), xe.getTrangThai(),
					xe.getGiaNgay(), xe.getGiaTuan());
		}
	}

	/**
	 * Nhập đầy đủ thông tin để tạo 1 xe mới. Mỗi trường đều được validate
	 * ngay khi nhập, yêu cầu nhập lại nếu không hợp lệ.
	 *
	 * @return Đối tượng xe mới với đầy đủ thông tin hợp lệ (trạng thái mặc định
	 *         là SAN_SANG).
	 */
	public XeMay nhapThongTinXeMoi() {
		System.out.println("\n===== THÊM XE MỚI =====");

		XeMay xe = new XeMay();

		while (true) {
			String hangXe = Input.inputString("Nhập hãng xe: ");
			if (Validator.isHangXe(hangXe)) {
				xe.setHangXe(hangXe);
				break;
			}
			System.out.println("Hãng xe không hợp lệ!");
		}

		while (true) {
			String dongXe = Input.inputString("Nhập dòng xe: ");
			if (Validator.isDongXe(dongXe)) {
				xe.setDongXe(dongXe);
				break;
			}
			System.out.println("Dòng xe không hợp lệ!");
		}

		while (true) {
			int namSanXuat = Input.inputInt("Nhập năm sản xuất: ");
			if (Validator.isNamSanXuat(namSanXuat)) {
				xe.setNamSanXuat(namSanXuat);
				break;
			}
			System.out.println("Năm sản xuất không hợp lệ!");
		}

		while (true) {
			float dungTich = Input.inputFloat("Nhập dung tích (cc): ");
			if (Validator.isDungTich(dungTich)) {
				xe.setDungTich(dungTich);
				break;
			}
			System.out.println("Dung tích không hợp lệ!");
		}

		while (true) {
			String bienSo = Input.inputString("Nhập biển số (VD: 30A-123.45): ");
			if (Validator.isBienSo(bienSo)) {
				xe.setBienSo(bienSo);
				break;
			}
			System.out.println("Biển số không hợp lệ!");
		}

		// Xe mới thêm luôn ở trạng thái sẵn sàng cho thuê
		xe.setTrangThai(XeMay.TrangThai.SAN_SANG);

		while (true) {
			double giaNgay = Input.inputDouble("Nhập giá thuê/ngày: ");
			if (Validator.isPositive(BigDecimal.valueOf(giaNgay))) {
				xe.setGiaNgay(BigDecimal.valueOf(giaNgay));
				break;
			}
			System.out.println("Giá thuê/ngày phải lớn hơn 0!");
		}

		while (true) {
			double giaTuan = Input.inputDouble("Nhập giá thuê/tuần: ");
			if (Validator.isPositive(BigDecimal.valueOf(giaTuan))) {
				xe.setGiaTuan(BigDecimal.valueOf(giaTuan));
				break;
			}
			System.out.println("Giá thuê/tuần phải lớn hơn 0!");
		}

		return xe;
	}

	/**
	 * Nhập thông tin sửa cho 1 xe đã tồn tại. Mỗi trường có thể để trống để
	 * giữ nguyên giá trị cũ. Giữ nguyên mã xe, userID và trạng thái xe gốc.
	 *
	 * @param xeCu Thông tin xe hiện tại (trước khi sửa).
	 * @return Đối tượng xe mới với các trường đã được cập nhật (hoặc giữ
	 *         nguyên nếu bỏ trống).
	 */
	public XeMay nhapThongTinXeSua(XeMay xeCu) {
		System.out.println("\n===== SỬA THÔNG TIN XE =====");

		XeMay xe = new XeMay();
		xe.setMaXe(xeCu.getMaXe());
		xe.setUserID(xeCu.getUserID());

		String hangXe = Input.inputString("Nhập hãng xe mới (bỏ trống để giữ nguyên): ");
		xe.setHangXe(Validator.isNotBlank(hangXe) ? hangXe : xeCu.getHangXe());

		String dongXe = Input.inputString("Nhập dòng xe mới (bỏ trống để giữ nguyên): ");
		xe.setDongXe(Validator.isNotBlank(dongXe) ? dongXe : xeCu.getDongXe());

		xe.setNamSanXuat(nhapIntGiuNguyenNeuBoTrong(
				"Nhập năm sản xuất mới (bỏ trống để giữ nguyên): ",
				xeCu.getNamSanXuat(), Validator::isNamSanXuat, "Năm sản xuất không hợp lệ!"));

		xe.setDungTich(nhapFloatGiuNguyenNeuBoTrong(
				"Nhập dung tích mới cc (bỏ trống để giữ nguyên): ",
				xeCu.getDungTich(), Validator::isDungTich, "Dung tích không hợp lệ!"));

		String bienSo = Input.inputString("Nhập biển số mới (bỏ trống để giữ nguyên): ");
		xe.setBienSo(Validator.isNotBlank(bienSo) ? bienSo : xeCu.getBienSo());

		// Trạng thái xe không cho sửa trực tiếp qua form này, giữ nguyên giá trị gốc
		xe.setTrangThai(xeCu.getTrangThai());

		xe.setGiaNgay(nhapGiaGiuNguyenNeuBoTrong(
				"Nhập giá thuê/ngày mới (bỏ trống để giữ nguyên): ",
				xeCu.getGiaNgay(), "Giá thuê/ngày phải lớn hơn 0!"));

		xe.setGiaTuan(nhapGiaGiuNguyenNeuBoTrong(
				"Nhập giá thuê/tuần mới (bỏ trống để giữ nguyên): ",
				xeCu.getGiaTuan(), "Giá thuê/tuần phải lớn hơn 0!"));

		return xe;
	}

	/**
	 * Nhập 1 giá trị int cho form sửa: bỏ trống để giữ nguyên giá trị cũ,
	 * nhập lại nếu không phải số nguyên hợp lệ hoặc không thỏa điều kiện.
	 *
	 * @param message       Thông báo hiển thị khi nhập.
	 * @param giaTriCu      Giá trị cũ, dùng khi người dùng bỏ trống.
	 * @param dieuKienHopLe Điều kiện hợp lệ áp dụng cho giá trị mới.
	 * @param thongBaoLoi   Thông báo hiển thị khi giá trị nhập không hợp lệ.
	 * @return Giá trị mới hợp lệ, hoặc giá trị cũ nếu bỏ trống.
	 */
	private int nhapIntGiuNguyenNeuBoTrong(String message, int giaTriCu,
			java.util.function.IntPredicate dieuKienHopLe, String thongBaoLoi) {
		while (true) {
			String input = Input.inputString(message);

			if (!Validator.isNotBlank(input)) {
				return giaTriCu;
			}

			try {
				int value = Integer.parseInt(input.trim());
				if (dieuKienHopLe.test(value)) {
					return value;
				}
			} catch (NumberFormatException e) {
				// rơi xuống thông báo lỗi bên dưới, yêu cầu nhập lại
			}

			System.out.println(thongBaoLoi);
		}
	}

	/**
	 * Nhập 1 giá trị float cho form sửa: bỏ trống để giữ nguyên giá trị cũ,
	 * nhập lại nếu không phải số thực hợp lệ hoặc không thỏa điều kiện.
	 *
	 * @param message       Thông báo hiển thị khi nhập.
	 * @param giaTriCu      Giá trị cũ, dùng khi người dùng bỏ trống.
	 * @param dieuKienHopLe Điều kiện hợp lệ áp dụng cho giá trị mới.
	 * @param thongBaoLoi   Thông báo hiển thị khi giá trị nhập không hợp lệ.
	 * @return Giá trị mới hợp lệ, hoặc giá trị cũ nếu bỏ trống.
	 */
	private float nhapFloatGiuNguyenNeuBoTrong(String message, float giaTriCu,
			java.util.function.Predicate<Float> dieuKienHopLe, String thongBaoLoi) {
		while (true) {
			String input = Input.inputString(message);

			if (!Validator.isNotBlank(input)) {
				return giaTriCu;
			}

			try {
				float value = Float.parseFloat(input.trim());
				if (dieuKienHopLe.test(value)) {
					return value;
				}
			} catch (NumberFormatException e) {
				// rơi xuống thông báo lỗi bên dưới, yêu cầu nhập lại
			}

			System.out.println(thongBaoLoi);
		}
	}

	/**
	 * Nhập 1 giá trị tiền (BigDecimal, phải &gt; 0) cho form sửa: bỏ trống để
	 * giữ nguyên giá trị cũ, nhập lại nếu không phải số hợp lệ hoặc không lớn hơn 0.
	 *
	 * @param message     Thông báo hiển thị khi nhập.
	 * @param giaTriCu    Giá trị cũ, dùng khi người dùng bỏ trống.
	 * @param thongBaoLoi Thông báo hiển thị khi giá trị nhập không hợp lệ.
	 * @return Giá trị mới hợp lệ, hoặc giá trị cũ nếu bỏ trống.
	 */
	private BigDecimal nhapGiaGiuNguyenNeuBoTrong(String message, BigDecimal giaTriCu, String thongBaoLoi) {
		while (true) {
			String input = Input.inputString(message);

			if (!Validator.isNotBlank(input)) {
				return giaTriCu;
			}

			try {
				BigDecimal value = BigDecimal.valueOf(Double.parseDouble(input.trim()));
				if (Validator.isPositive(value)) {
					return value;
				}
			} catch (NumberFormatException e) {
				// rơi xuống thông báo lỗi bên dưới, yêu cầu nhập lại
			}

			System.out.println(thongBaoLoi);
		}
	}

	/**
	 * Hiển thị kết quả của 1 thao tác nghiệp vụ (thành công/thất bại).
	 *
	 * @param result Kết quả cần hiển thị.
	 */
	public void hienThiKetQua(Result result) {
		System.out.println(result.getMessage());
	}
}