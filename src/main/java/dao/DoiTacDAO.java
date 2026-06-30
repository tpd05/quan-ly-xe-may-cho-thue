package dao;

import model.DoiTac;
import util.FilePath;

public class DoiTacDAO extends BaseDAO<DoiTac>{
	
	private static final String FILE_PATH = FilePath.DOI_TAC;
	private static final String HEADER = "maDoiTac,userID";
	
	public DoiTacDAO() {
		super(FILE_PATH, HEADER);
	}

	@Override
	public DoiTac parse(String line) {
		String[] data = line.split(",");
		
		if(data.length != 2) {
			throw new IllegalArgumentException("Dữ liệu đối tác không hợp lệ: " + line);
		}
		return new DoiTac(
				Integer.parseInt(data[0].trim()),
				Integer.parseInt(data[1].trim()));
	}

	@Override
	public String format(DoiTac object) {
		return String.join(",", 
				String.valueOf(object.getMaDoiTac()),
				String.valueOf(object.getUserID()));
	}

	@Override
	protected int getId(DoiTac object) {
		return object.getMaDoiTac();
	}
}
