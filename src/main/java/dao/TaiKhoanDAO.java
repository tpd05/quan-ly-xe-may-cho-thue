package dao;

import model.TaiKhoan;
import util.FilePath;

public class TaiKhoanDAO extends BaseDAO<TaiKhoan> {

	private static final String FILE_PATH = FilePath.TAI_KHOAN;
	private static final String HEADER = "userID,username,password,role,hoTen,soDienThoai,email,soCCCD,tenCuaHang,diaChiCuaHang";

	public TaiKhoanDAO() {
		super(FILE_PATH, HEADER);
	}

	/**
	 * Constructor phụ, chỉ dùng cho unit test: cho phép chỉ định đường dẫn
	 * file khác (ví dụ file tạm), tránh test đụng vào file dữ liệu thật.
	 *
	 * @param filePath Đường dẫn file dữ liệu dùng riêng cho test.
	 */
	public TaiKhoanDAO(String filePath) {
		super(filePath, HEADER);
	}

	/**
	 * Chuyển 1 dòng từ file sang 1 object
	 */
	@Override
	public TaiKhoan parse(String line) {

		String[] data = line.split(",");

		if (data.length != 10) {
			throw new IllegalStateException("Dữ liệu tài khoản không hợp lệ: " + line);
		}

		return new TaiKhoan(Integer.parseInt(data[0].trim()), data[1].trim(), data[2].trim(),
				TaiKhoan.Role.valueOf(data[3].trim()), data[4].trim(), data[5].trim(), data[6].trim(), data[7].trim(),
				data[8].trim(), data[9].trim());
	}

	/**
	 * Chuyển 1 object sang 1 dòng của file
	 */
	@Override
	public String format(TaiKhoan object) {

		return String.join(",", String.valueOf(object.getUserID()), object.getUsername(), object.getPassword(),
				object.getRole().name(), object.getHoTen(), object.getSoDienThoai(), object.getEmail(),
				object.getSoCCCD(), object.getTenCuaHang(), object.getDiaChiCuaHang());
	}

	@Override
	protected int getId(TaiKhoan object) {
		return object.getUserID();
	}
	
	public TaiKhoan findByUsername(String username) {

	    for (TaiKhoan tk : findAll()) {

	        if (tk.getUsername().equals(username)) {
	            return tk;
	        }

	    }

	    return null;
	}

}