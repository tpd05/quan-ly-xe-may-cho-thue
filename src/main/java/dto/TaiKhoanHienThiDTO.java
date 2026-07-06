package dto;

import model.TaiKhoan;

/**
 * DTO chứa thông tin tài khoản dùng để hiển thị ra view.
 * Ẩn 2 trường nhạy cảm: userID (thông tin nội bộ, không cần thiết với
 * người dùng) và password (không hiển thị dù đã băm, tránh lộ thông tin
 * xác thực).
 */
public class TaiKhoanHienThiDTO {

	private String username;
	private String hoTen;
	private String soDienThoai;
	private String email;
	private String soCCCD;
	private String tenCuaHang;
	private String diaChiCuaHang;

	/**
	 * Khởi tạo DTO hiển thị từ đối tượng {@link TaiKhoan} đầy đủ, tự động
	 * bỏ qua userID và password.
	 *
	 * @param taiKhoan Tài khoản gốc cần hiển thị.
	 */
	public TaiKhoanHienThiDTO(TaiKhoan taiKhoan) {
		this.username = taiKhoan.getUsername();
		this.hoTen = taiKhoan.getHoTen();
		this.soDienThoai = taiKhoan.getSoDienThoai();
		this.email = taiKhoan.getEmail();
		this.soCCCD = taiKhoan.getSoCCCD();
		this.tenCuaHang = taiKhoan.getTenCuaHang();
		this.diaChiCuaHang = taiKhoan.getDiaChiCuaHang();
	}

	public String getUsername() {
		return username;
	}

	public String getHoTen() {
		return hoTen;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public String getEmail() {
		return email;
	}

	public String getSoCCCD() {
		return soCCCD;
	}

	public String getTenCuaHang() {
		return tenCuaHang;
	}

	public String getDiaChiCuaHang() {
		return diaChiCuaHang;
	}
}