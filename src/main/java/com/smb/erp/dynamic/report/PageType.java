/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

/**
 *
 * @author Burhani152
 * jasper report uses default 72dpi
 */
public enum PageType {
        CASHMEMO(609, 425),     //215x150mm @72dpi
        INVOICE(609, 794),      //215x280mm @72dpi
	LETTER(612, 792),
	NOTE(540, 720),
	LEGAL(612, 1008),
	A0(2380, 3368),
	A1(1684, 2380),
	A2(1190, 1684),
	A3(842, 1190),
	A4(595, 842),
	A5(421, 595),
	A6(297, 421),
	A7(210, 297),
	A8(148, 210),
	A9(105, 148),
	A10(74, 105),
	B0(2836, 4008),
	B1(2004, 2836),
	B2(1418, 2004),
	B3(1002, 1418),
	B4( 709, 1002),
	B5(501, 709),
	B6(353, 497),
	B7(252, 353),
	B8(173, 252),
	B9(122, 173),
	B10(86, 122),
	C0(2599, 3679),
	C1(1836, 2599),
	C2(1296, 1836),
	C3(922, 1296),
	C4(648, 922),
	C5(461, 648),
	C6(324, 461),
	C7(230, 324),
	C8(158, 230),
	C9(115, 158),
	C10(79, 115),
	ARCH_E(2592, 3456),
	ARCH_D(1728, 2592),
	ARCH_C(1296, 1728),
	ARCH_B(864, 1296),
	ARCH_A(648, 864),
	FLSA(612, 936),
	FLSE(612, 936),
	HALFLETTER(396, 612),
	_11X17(792, 1224),
	LEDGER(1224, 792);

	private int width;
	private int height;

	private PageType(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
