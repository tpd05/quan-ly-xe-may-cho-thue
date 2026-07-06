package dao;

import java.time.LocalDateTime;
import java.util.List;

import model.ChiTietDonThue;
import util.FilePath;

public class ChiTietDonThueDAO extends BaseDAO<ChiTietDonThue> {

	private static final String FILE_PATH = FilePath.CHI_TIET_DON_THUE;
	private static final String HEADER = "maChiTiet,maDonThue,maXe,thoiGianBatDau,thoiGianKetThuc,thoiGianTra,donGia";

	public ChiTietDonThueDAO() {
		super(FILE_PATH, HEADER);
	}

	/**
	 * Constructor phụ, chỉ dùng cho unit test: cho phép chỉ định đường dẫn
	 * file khác (ví dụ file tạm), tránh test đụng vào file dữ liệu thật.
	 *
	 * @param filePath Đường dẫn file dữ liệu dùng riêng cho test.
	 */
	public ChiTietDonThueDAO(String filePath) {
		super(filePath, HEADER);
	}

	/**
	 * Chuyển 1 dòng từ file sang 1 object
	 */
	@Override
	public ChiTietDonThue parse(String line) {
		String[] data = line.split(",");

		if (data.length != 7) {
			throw new IllegalArgumentException("Dữ liệu chi tiết đơn thuê không hợp lệ: " + line);
		}

		LocalDateTime thoiGianTra = null;
		String thoiGianTraRaw = data[5].trim();
		if (!thoiGianTraRaw.isEmpty()) {
			thoiGianTra = LocalDateTime.parse(thoiGianTraRaw);
		}

		return new ChiTietDonThue(Integer.parseInt(data[0].trim()), Integer.parseInt(data[1].trim()),
				Integer.parseInt(data[2].trim()), LocalDateTime.parse(data[3].trim()),
				LocalDateTime.parse(data[4].trim()), thoiGianTra, Integer.parseInt(data[6].trim()));
	}

	/**
	 * Chuyển 1 object sang 1 dòng của file
	 */
	@Override
	public String format(ChiTietDonThue object) {

		String thoiGianTra = "";

		if (object.getThoiGianTra() != null) {
			thoiGianTra = object.getThoiGianTra().toString();
		}

		return String.join(",", String.valueOf(object.getMaChiTiet()), String.valueOf(object.getMaDonThue()),
				String.valueOf(object.getMaXe()), object.getThoiGianBatDau().toString(),
				object.getThoiGianKetThuc().toString(), thoiGianTra, String.valueOf(object.getDonGia()));
	}

	/**
	 * Lấy Id của 1 object
	 */
	@Override
	protected int getId(ChiTietDonThue object) {
		return object.getMaChiTiet();
	}

	/**
	 * Ghi đè tất cả nội dung trong file bằng list object
	 * @param objects
	 */
	public void replaceAll(List<ChiTietDonThue> objects) {
		saveObjects(objects);
	}

}