package dao;

import model.ChiNhanh;
import util.FilePath;

public class ChiNhanhDAO extends BaseDAO<ChiNhanh>{

	private static final String FILE_PATH = FilePath.CHI_NHANH;
	private static final String HEADER = "maChiNhanh,maDoiTac,tenChiNhanh,diaDiem";
	
	public ChiNhanhDAO() {
		super(FILE_PATH, HEADER);
	}

	@Override
	public ChiNhanh parse(String line) {
		String[] data = line.split(",");
		
		if(data.length != 4) {
			throw new IllegalArgumentException("Dữ liệu chi nhánh không hợp lệ: " + line);
		}
		
		return new ChiNhanh(
				Integer.parseInt(data[0].trim()),
				Integer.parseInt(data[1].trim()),
				data[2].trim(),
				data[3].trim()
				);
	}

	@Override
	public String format(ChiNhanh object) {
		return String.join(",",
				String.valueOf(object.getMaChiNhanh()),
				String.valueOf(object.getMaDoiTac()),
				object.getTenChiNhanh(),
				object.getDiaDiem()
				);
	}

	@Override
	protected int getId(ChiNhanh object) {
		return object.getMaChiNhanh();
	}

}
