package model;

import java.time.LocalDateTime;

public class ChiTietDonThue {
	private int maChiTiet;
	private int maDonThue;
	private int maXe;
	private LocalDateTime thoiGianBatDau;
	private LocalDateTime thoiGianKetThuc;
	private LocalDateTime thoiGianTra;
	private int donGia;

	public ChiTietDonThue(int maChiTiet, int maDonThue, int maXe, LocalDateTime thoiGianBatDau,
			LocalDateTime thoiGianKetThuc, LocalDateTime thoiGianTra, int donGia) {
		super();
		this.maChiTiet = maChiTiet;
		this.maDonThue = maDonThue;
		this.maXe = maXe;
		this.thoiGianBatDau = thoiGianBatDau;
		this.thoiGianKetThuc = thoiGianKetThuc;
		this.thoiGianTra = thoiGianTra;
		this.donGia = donGia;
	}

	public ChiTietDonThue(int maDonThue, int maXe, LocalDateTime thoiGianBatDau, LocalDateTime thoiGianKetThuc,
			LocalDateTime thoiGianTra, int donGia) {
		super();
		this.maDonThue = maDonThue;
		this.maXe = maXe;
		this.thoiGianBatDau = thoiGianBatDau;
		this.thoiGianKetThuc = thoiGianKetThuc;
		this.thoiGianTra = thoiGianTra;
		this.donGia = donGia;
	}

	public int getMaChiTiet() {
		return maChiTiet;
	}

	public void setMaChiTiet(int maChiTiet) {
		this.maChiTiet = maChiTiet;
	}

	public int getMaDonThue() {
		return maDonThue;
	}

	public void setMaDonThue(int maDonThue) {
		this.maDonThue = maDonThue;
	}

	public int getMaXe() {
		return maXe;
	}

	public void setMaXe(int maXe) {
		this.maXe = maXe;
	}

	public LocalDateTime getThoiGianBatDau() {
		return thoiGianBatDau;
	}

	public void setThoiGianBatDau(LocalDateTime thoiGianBatDau) {
		this.thoiGianBatDau = thoiGianBatDau;
	}

	public LocalDateTime getThoiGianKetThuc() {
		return thoiGianKetThuc;
	}

	public void setThoiGianKetThuc(LocalDateTime thoiGianKetThuc) {
		this.thoiGianKetThuc = thoiGianKetThuc;
	}

	public LocalDateTime getThoiGianTra() {
		return thoiGianTra;
	}

	public void setThoiGianTra(LocalDateTime thoiGianTra) {
		this.thoiGianTra = thoiGianTra;
	}

	public int getDonGia() {
		return donGia;
	}

	public void setDonGia(int donGia) {
		this.donGia = donGia;
	}

}