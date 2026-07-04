package dao;

import java.time.LocalDateTime;

import model.DonThue;
import util.FilePath;

/**
 * DAO quản lý dữ liệu đơn thuê (lưu trong file CSV).
 */
public class DonThueDAO extends BaseDAO<DonThue> {

	private static final String FILE_PATH = FilePath.DON_THUE;
	private static final String HEADER = "maDonThue,userID,diaChiNhanXe,trangThai,ngayDat";

	public DonThueDAO() {
		super(FILE_PATH, HEADER);
	}

	/**
	 * Chuyển 1 dòng từ file sang đối tượng {@link DonThue}.
	 */
	@Override
	public DonThue parse(String line) {
		String[] data = line.split(",");

		if (data.length != 5) {
			throw new IllegalArgumentException("Dữ liệu đơn thuê không hợp lệ: " + line);
		}

		return new DonThue(Integer.parseInt(data[0].trim()), Integer.parseInt(data[1].trim()), data[2].trim(),
				DonThue.TrangThai.valueOf(data[3].trim()), LocalDateTime.parse(data[4].trim()));
	}

	/**
	 * Chuyển đối tượng {@link DonThue} sang 1 dòng của file.
	 */
	@Override
	public String format(DonThue object) {
		return String.join(",", String.valueOf(object.getMaDonThue()), String.valueOf(object.getUserID()),
				object.getDiaChiNhanXe(), object.getTrangThai().name(), object.getNgayDat().toString());
	}

	/**
	 * Lấy Id của 1 đối tượng {@link DonThue}.
	 */
	@Override
	protected int getId(DonThue object) {
		return object.getMaDonThue();
	}
}