package dao;

import java.math.BigDecimal;

import model.XeMay;
import util.FilePath;

public class XeMayDAO extends BaseDAO<XeMay> {

	private static final String FILE_PATH = FilePath.XE_MAY;
	private static final String HEADER = "maXe,userID,hangXe,dongXe,namSanXuat,dungTich,bienSo,trangThai,giaNgay,giaTuan";

	public XeMayDAO() {
		super(FILE_PATH, HEADER);

	}

	/**
	 * Constructor phụ, chỉ dùng cho unit test: cho phép chỉ định đường dẫn
	 * file khác (ví dụ file tạm), tránh test đụng vào file dữ liệu thật.
	 *
	 * @param filePath Đường dẫn file dữ liệu dùng riêng cho test.
	 */
	public XeMayDAO(String filePath) {
		super(filePath, HEADER);
	}

	/**
	 * Chuyển 1 dòng từ file sang 1 object
	 */
	@Override
	public XeMay parse(String line) {
		String[] data = line.split(",");

		if (data.length != 10) {
			throw new IllegalArgumentException("Dữ liệu xe máy không hợp lệ" + line);
		}

		return new XeMay(Integer.parseInt(data[0].trim()), Integer.parseInt(data[1].trim()), data[2].trim(),
				data[3].trim(), Integer.parseInt(data[4].trim()), Float.parseFloat(data[5].trim()), data[6].trim(),
				XeMay.TrangThai.valueOf(data[7].trim()), new BigDecimal(data[8].trim()),
				new BigDecimal(data[9].trim()));
	}

	/**
	 * Chuyển 1 object sang 1 dòng của file
	 */
	@Override
	public String format(XeMay object) {
		return String.join(",", String.valueOf(object.getMaXe()), String.valueOf(object.getUserID()),
				object.getHangXe(), object.getDongXe(), String.valueOf(object.getNamSanXuat()),
				String.valueOf(object.getDungTich()), object.getBienSo(), object.getTrangThai().name(),
				object.getGiaNgay().toString(), object.getGiaTuan().toString());
	}

	@Override
	protected int getId(XeMay object) {
		return object.getMaXe();
	}

}