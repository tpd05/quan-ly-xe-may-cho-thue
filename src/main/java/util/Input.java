package util;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Lớp tiện ích hỗ trợ nhập dữ liệu từ bàn phím (console), có xử lý
 * lỗi định dạng và yêu cầu nhập lại khi sai kiểu dữ liệu.
 */
public final class Input {

	private static final Scanner SCANNER = new Scanner(System.in);

	private Input() {
	}

	/**
	 * Nhập số nguyên. Yêu cầu nhập lại nếu người dùng nhập sai kiểu dữ liệu.
	 *
	 * @param message Thông báo hiển thị trước khi nhập.
	 * @return Số nguyên đã nhập.
	 */
	public static int inputInt(String message) {
		while (true) {
			try {
				System.out.print(message);
				int value = SCANNER.nextInt();
				SCANNER.nextLine();
				return value;
			} catch (InputMismatchException e) {
				System.out.println("Vui lòng nhập số nguyên.");
				SCANNER.nextLine();
			}
		}
	}

	/**
	 * Nhập chuỗi ký tự (không kiểm tra định dạng).
	 *
	 * @param message Thông báo hiển thị trước khi nhập.
	 * @return Chuỗi đã nhập.
	 */
	public static String inputString(String message) {
		System.out.print(message);
		return SCANNER.nextLine();
	}

	/**
	 * Nhập số thực (float). Yêu cầu nhập lại nếu sai kiểu dữ liệu.
	 *
	 * @param message Thông báo hiển thị trước khi nhập.
	 * @return Số thực đã nhập.
	 */
	public static float inputFloat(String message) {
		while (true) {
			try {
				System.out.print(message);
				float value = SCANNER.nextFloat();
				SCANNER.nextLine();
				return value;
			} catch (InputMismatchException e) {
				System.out.println("Vui lòng nhập số thực.");
				SCANNER.nextLine();
			}
		}
	}

	/**
	 * Nhập số thực (double). Yêu cầu nhập lại nếu sai kiểu dữ liệu.
	 *
	 * @param message Thông báo hiển thị trước khi nhập.
	 * @return Số double đã nhập.
	 */
	public static double inputDouble(String message) {
		while (true) {
			try {
				System.out.print(message);
				double value = SCANNER.nextDouble();
				SCANNER.nextLine();
				return value;
			} catch (InputMismatchException e) {
				System.out.println("Vui lòng nhập số double.");
				SCANNER.nextLine();
			}
		}
	}
}