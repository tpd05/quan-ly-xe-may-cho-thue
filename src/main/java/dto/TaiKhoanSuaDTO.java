package dto;

/**
 * DTO chứa dữ liệu người dùng nhập vào khi sửa thông tin tài khoản.
 *
 * Khác với các trường thông tin cá nhân (hoTen, soDienThoai...), trường
 * {@code matKhauMoi} KHÔNG được gộp sẵn với giá trị cũ ở tầng view — vì
 * view không có (và không nên có) mật khẩu cũ ở dạng có thể hiển thị lại.
 * Do đó {@code matKhauMoi} có thể là chuỗi rỗng, mang ý nghĩa "người dùng
 * không muốn đổi mật khẩu"; việc kiểm tra rỗng để quyết định có đổi mật
 * khẩu hay không được xử lý ở tầng service ({@code AuthService}).
 */
public class TaiKhoanSuaDTO {

	private final String hoTen;
	private final String soDienThoai;
	private final String email;
	private final String soCCCD;
	private final String tenCuaHang;
	private final String diaChiCuaHang;

	public TaiKhoanSuaDTO(String hoTen, String soDienThoai, String email, String soCCCD, String tenCuaHang,
			String diaChiCuaHang) {
		this.hoTen = hoTen;
		this.soDienThoai = soDienThoai;
		this.email = email;
		this.soCCCD = soCCCD;
		this.tenCuaHang = tenCuaHang;
		this.diaChiCuaHang = diaChiCuaHang;
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