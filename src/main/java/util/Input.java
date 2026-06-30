package util;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class Input {

	private Input() {
	}

	private static final Scanner SCANNER = new Scanner(System.in);

	// Nhập vào số nguyên
	public static int inputInt(String ms) {
		while (true) {
			try {
				System.out.print(ms);
				int res = SCANNER.nextInt();
				SCANNER.nextLine();
				return res;
			} catch (InputMismatchException e) {
				System.out.println("Vui long nhap so nguyen");
				SCANNER.nextLine();
			}
		}
	}

	// Nhập vào chuỗi
	public static String inputString(String ms) {
		while (true) {
			System.out.print(ms);
			String res = SCANNER.nextLine();
			return res;
		}
	}

	// Nhập vào số thực
	public static float inputFloat(String ms) {
		while (true) {
			try {
				System.out.print(ms);
				float res = SCANNER.nextFloat();
				SCANNER.nextLine();
				return res;
			} catch (InputMismatchException e) {
				System.out.println("Vui long nhap so thuc");
				SCANNER.nextLine();
			}
		}
	}

}
