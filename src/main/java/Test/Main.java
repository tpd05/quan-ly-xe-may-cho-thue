package Test;

import java.util.List;

import util.FilePath;
import util.FileUtil;

/**
 * @author doant
 *
 */
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileUtil.appendLine(
			    FilePath.TAI_KHOAN,
			    "102,Trần Phương Đoàn,123456,ADMIN,Test,0123456789,test@gmail.com,123456789012"
			);
		List<String> lines = FileUtil.readAllLines(FilePath.TAI_KHOAN);

		for (int i = 0; i < lines.size(); i++) {
		    System.out.println("Dòng " + i + ": " + lines.get(i));
		}
	}

}
