package dao;

import model.TaiKhoan;
import util.FilePath;

public class TaiKhoanDAO extends BaseDAO<TaiKhoan> {

	private static final String FILE_PATH = FilePath.TAI_KHOAN;
	private static final String HEADER = "userID,username,password,role,hoTen,soDienThoai,email,soCCCD";

	public TaiKhoanDAO() {
		super(FILE_PATH, HEADER);
	}

	@Override
	public TaiKhoan parse(String line) {

		String[] data = line.split(",");

		if (data.length != 8) {
			throw new IllegalArgumentException("Dữ liệu tài khoản không hợp lệ: " + line);
		}

		return new TaiKhoan(Integer.parseInt(data[0].trim()), data[1].trim(), data[2].trim(),
				TaiKhoan.Role.valueOf(data[3].trim()), data[4].trim(), data[5].trim(), data[6].trim(), data[7].trim());
	}

	@Override
	public String format(TaiKhoan object) {

		return String.join(",", String.valueOf(object.getUserID()), object.getUsername(), object.getPassword(),
				object.getRole().name(), object.getHoTen(), object.getSoDienThoai(), object.getEmail(),
				object.getSoCCCD());
	}

	@Override
	protected int getId(TaiKhoan object) {
		return object.getUserID();
	}

}