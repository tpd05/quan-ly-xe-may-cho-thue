package model;

public class TaiKhoan {
	private int userID;
	private String username;
	private String password;
	private Role role;
	private String hoTen;
	private String soDienThoai;
	private String email;
	private String soCCCD;
	private String tenCuaHang;
	private String diaChiCuaHang;

	public enum Role {
		DOI_TAC
	}

	public TaiKhoan(int userID, String username, String password, Role role, String hoTen, String soDienThoai,
			String email, String soCCCD, String tenCuaHang, String diaChiCuaHang) {
		super();
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.role = role;
		this.hoTen = hoTen;
		this.soDienThoai = soDienThoai;
		this.email = email;
		this.soCCCD = soCCCD;
		this.tenCuaHang = tenCuaHang;
		this.diaChiCuaHang = diaChiCuaHang;
	}

	public TaiKhoan(String username, String password, Role role, String hoTen, String soDienThoai, String email,
			String soCCCD, String tenCuaHang, String diaChiCuaHang) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.hoTen = hoTen;
		this.soDienThoai = soDienThoai;
		this.email = email;
		this.soCCCD = soCCCD;
		this.tenCuaHang = tenCuaHang;
		this.diaChiCuaHang = diaChiCuaHang;
	}

	public TaiKhoan() {
		super();
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSoCCCD() {
		return soCCCD;
	}

	public void setSoCCCD(String soCCCD) {
		this.soCCCD = soCCCD;
	}

	public String getTenCuaHang() {
		return tenCuaHang;
	}

	public void setTenCuaHang(String tenCuaHang) {
		this.tenCuaHang = tenCuaHang;
	}

	public String getDiaChiCuaHang() {
		return diaChiCuaHang;
	}

	public void setDiaChiCuaHang(String diaChiCuaHang) {
		this.diaChiCuaHang = diaChiCuaHang;
	}

}