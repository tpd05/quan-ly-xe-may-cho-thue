package dao;

import java.time.LocalDateTime;
import java.util.List;

import model.ChiTietDonThue;
import util.FilePath;

/**
 * DAO quản lý dữ liệu chi tiết đơn thuê (lưu trong file CSV).
 * Mỗi chi tiết đơn thuê gắn với 1 đơn thuê và 1 xe cụ thể.
 */
public class ChiTietDonThueDAO extends BaseDAO<ChiTietDonThue> {

	private static final String FILE_PATH = FilePath.CHI_TIET_DON_THUE;
	private static final String HEADER =
			"maChiTiet,maDonThue,maXe,thoiGianBatDau,thoiGianKetThuc,thoiGianTra,donGia";

	public ChiTietDonThueDAO() {
		super(FILE_PATH, HEADER);
	}

	/**
	 * Chuyển 1 dòng từ file sang đối tượng {@link ChiTietDonThue}.
	 * Trường thoiGianTra có thể rỗng (chưa trả xe) nên được xử lý riêng.
	 */
	@Override
	public ChiTietDonThue parse(String line) {
		String[] data = line.split(",");

		if (data.length != 7) {
			throw new IllegalArgumentException("Dữ liệu chi tiết đơn thuê không hợp lệ: " + line);
		}

		// thoiGianTra có thể chưa có giá trị (xe chưa được trả)
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
	 * Chuyển đối tượng {@link ChiTietDonThue} sang 1 dòng của file.
	 * Nếu chưa có thoiGianTra thì ghi giá trị rỗng.
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
	 * Lấy Id của 1 đối tượng {@link ChiTietDonThue}.
	 */
	@Override
	protected int getId(ChiTietDonThue object) {
		return object.getMaChiTiet();
	}

	/**
	 * Ghi đè toàn bộ nội dung file bằng danh sách đối tượng mới.
	 * Khác với insert/update/delete (thao tác từng phần tử), method này
	 * thay thế toàn bộ dữ liệu hiện có.
	 *
	 * @param objects Danh sách đối tượng cần lưu.
	 */
	public void replaceAll(List<ChiTietDonThue> objects) {
		saveObjects(objects);
	}
}