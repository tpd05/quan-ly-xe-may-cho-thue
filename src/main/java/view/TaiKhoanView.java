package view;

import dto.TaiKhoanHienThiDTO;
import dto.TaiKhoanSuaDTO;
import util.Input;
import util.Result;
import util.Validator;

/**
 * View xử lý hiển thị và nhập liệu cho chức năng xem/sửa thông tin tài
 * khoản của đối tác đang đăng nhập.
 */
public class TaiKhoanView {

	/** Hiển thị menu thông tin tài khoản. */
	public void hienThiMenu() {
		System.out.println("\n========== THÔNG TIN TÀI KHOẢN ==========");
		System.out.println("1. Sửa thông tin");
		System.out.println("0. Quay lại");
	}

	/** Nhập lựa chọn của người dùng từ menu thông tin tài khoản. */
	public int nhapLuaChonMenu() {
		return Input.inputInt("Chọn chức năng: ");
	}

	/**
	 * Hiển thị thông tin tài khoản hiện tại.
	 * Không hiển thị userID và password theo yêu cầu bảo mật.
	 *
	 * @param dto Thông tin tài khoản cần hiển thị.
	 */
	public void hienThiThongTin(TaiKhoanHienThiDTO dto) {
		System.out.println("\n===== THÔNG TIN TÀI KHOẢN =====");
		System.out.println("Username: " + dto.getUsername());
		System.out.println("Họ tên: " + dto.getHoTen());
		System.out.println("Số điện thoại: " + dto.getSoDienThoai());
		System.out.println("Email: " + dto.getEmail());
		System.out.println("Số CCCD: " + dto.getSoCCCD());
		System.out.println("Tên cửa hàng: " + dto.getTenCuaHang());
		System.out.println("Địa chỉ cửa hàng: " + dto.getDiaChiCuaHang());
	}

	/**
	 * Nhập thông tin sửa cho tài khoản. Mỗi trường có thể để trống để giữ
	 * nguyên giá trị cũ (trừ mật khẩu, xem giải thích ở
	 * {@link TaiKhoanSuaDTO}).
	 *
	 * @param hienTai Thông tin tài khoản hiện tại (trước khi sửa).
	 * @return DTO chứa dữ liệu đã nhập, sẵn sàng gửi cho service xử lý.
	 */
	public TaiKhoanSuaDTO nhapThongTinSua(TaiKhoanHienThiDTO hienTai) {
		System.out.println("\n===== SỬA THÔNG TIN TÀI KHOẢN =====");
		System.out.println("(Bỏ trống nếu không muốn thay đổi trường đó)");

		String hoTen = Input.inputString("Họ tên mới [" + hienTai.getHoTen() + "]: ");
		hoTen = Validator.isNotBlank(hoTen) ? hoTen : hienTai.getHoTen();

		String soDienThoai = Input.inputString("Số điện thoại mới [" + hienTai.getSoDienThoai() + "]: ");
		soDienThoai = Validator.isNotBlank(soDienThoai) ? soDienThoai : hienTai.getSoDienThoai();

		String email = Input.inputString("Email mới [" + hienTai.getEmail() + "]: ");
		email = Validator.isNotBlank(email) ? email : hienTai.getEmail();

		String soCCCD = Input.inputString("Số CCCD mới [" + hienTai.getSoCCCD() + "]: ");
		soCCCD = Validator.isNotBlank(soCCCD) ? soCCCD : hienTai.getSoCCCD();

		String tenCuaHang = Input.inputString("Tên cửa hàng mới [" + hienTai.getTenCuaHang() + "]: ");
		tenCuaHang = Validator.isNotBlank(tenCuaHang) ? tenCuaHang : hienTai.getTenCuaHang();

		String diaChiCuaHang = Input.inputString("Địa chỉ cửa hàng mới [" + hienTai.getDiaChiCuaHang() + "]: ");
		diaChiCuaHang = Validator.isDiaChi(diaChiCuaHang) ? diaChiCuaHang : hienTai.getDiaChiCuaHang();

		return new TaiKhoanSuaDTO(hoTen, soDienThoai, email, soCCCD, tenCuaHang, diaChiCuaHang);
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
}