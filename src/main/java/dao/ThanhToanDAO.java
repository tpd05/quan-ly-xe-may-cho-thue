package dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import model.ThanhToan;
import util.FilePath;

public class ThanhToanDAO extends BaseDAO<ThanhToan>{

	private static final String FILE_PATH = FilePath.THANH_TOAN;
	private static final String HEADER = "maThanhToan,maDonThue,soTien,phuongThuc,thoiGianTao,trangThai";
	
	public ThanhToanDAO() {
		super(FILE_PATH, HEADER);
	}

	@Override
	public ThanhToan parse(String line) {
		String[] data = line.split(",");
		
		if(data.length != 6) {
			throw new IllegalArgumentException("Dữ liệu thanh toán không hợp lệ: " + line);
		}
		
		return new ThanhToan(
				Integer.parseInt(data[0].trim()),
				Integer.parseInt(data[1].trim()),
				new BigDecimal(data[2].trim()),
				data[3].trim(),
				LocalDateTime.parse(data[4].trim()),
				ThanhToan.TrangThai.valueOf(data[5].trim())
				);
	}

	@Override
	public String format(ThanhToan object) {
		return String.join(",",
				String.valueOf(object.getMaThanhToan()),
				String.valueOf(object.getMaDonThue()),
				object.getSoTien().toString(),
				object.getPhuongThuc(),
				object.getThoiGianTao().toString(),
				object.getTrangThai().name()
				);
	}

	@Override
	protected int getId(ThanhToan object) {
		return object.getMaThanhToan();
	}

}
