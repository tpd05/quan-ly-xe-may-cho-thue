package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ThanhToan {
	private int maThanhToan;
	private int maDonThue;
	private BigDecimal soTien;
	private String phuongThuc;
	private LocalDateTime thoiGianTao;
	private String trangThai;

	public ThanhToan(int maThanhToan, int maDonThue, BigDecimal soTien, String phuongThuc, LocalDateTime thoiGianTao,
			String trangThai) {
		super();
		this.maThanhToan = maThanhToan;
		this.maDonThue = maDonThue;
		this.soTien = soTien;
		this.phuongThuc = phuongThuc;
		this.thoiGianTao = thoiGianTao;
		this.trangThai = trangThai;
	}

	public ThanhToan(int maDonThue, BigDecimal soTien, String phuongThuc, LocalDateTime thoiGianTao, String trangThai) {
		super();
		this.maDonThue = maDonThue;
		this.soTien = soTien;
		this.phuongThuc = phuongThuc;
		this.thoiGianTao = thoiGianTao;
		this.trangThai = trangThai;
	}

	public int getMaThanhToan() {
		return maThanhToan;
	}

	public void setMaThanhToan(int maThanhToan) {
		this.maThanhToan = maThanhToan;
	}

	public int getMaDonThue() {
		return maDonThue;
	}

	public void setMaDonThue(int maDonThue) {
		this.maDonThue = maDonThue;
	}

	public BigDecimal getSoTien() {
		return soTien;
	}

	public void setSoTien(BigDecimal soTien) {
		this.soTien = soTien;
	}

	public String getPhuongThuc() {
		return phuongThuc;
	}

	public void setPhuongThuc(String phuongThuc) {
		this.phuongThuc = phuongThuc;
	}

	public LocalDateTime getThoiGianTao() {
		return thoiGianTao;
	}

	public void setThoiGianTao(LocalDateTime thoiGianTao) {
		this.thoiGianTao = thoiGianTao;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

}
