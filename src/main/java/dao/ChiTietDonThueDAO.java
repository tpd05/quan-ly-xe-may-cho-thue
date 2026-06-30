package dao;

import java.time.LocalDateTime;

import model.ChiTietDonThue;
import util.FilePath;

public class ChiTietDonThueDAO extends BaseDAO<ChiTietDonThue>{

	private static final String FILE_PATH = FilePath.CHI_TIET_DON_THUE;
	private static final String HEADER =  "maChiTiet,maDonThue,maXe,thoiGianBatDau,thoiGianKetThuc,thoiGianTra,donGia";
	
	public ChiTietDonThueDAO() {
		super(FILE_PATH, HEADER);
	}

	@Override
	public ChiTietDonThue parse(String line) {
		String[] data = line.split(",");
		
		if(data.length != 7) {
			throw new IllegalArgumentException("Dữ liệu chi tiết đơn thuê không hợp lệ: " + line);
		}
		
		return new ChiTietDonThue(
				Integer.parseInt(data[0].trim()),
				Integer.parseInt(data[1].trim()),
				Integer.parseInt(data[2].trim()),
				LocalDateTime.parse(data[3].trim()),
				LocalDateTime.parse(data[4].trim()),
				LocalDateTime.parse(data[5].trim()),
				Integer.parseInt(data[6].trim())
				);
	}

	@Override
	public String format(ChiTietDonThue object) {
		return String.join(",",
				String.valueOf(object.getMaChiTiet()),
				String.valueOf(object.getMaDonThue()),
				String.valueOf(object.getMaXe()),
				object.getThoiGianBatDau().toString(),
				object.getThoiGianKetThuc().toString(),
				object.getThoiGianTra().toString(),
				String.valueOf(object.getDonGia())
				);
	}

	@Override
	protected int getId(ChiTietDonThue object) {
		return object.getMaChiTiet();
	}

}
