/**************************************************************************
 * <pre>
 *
 * Copyright (c) Unterrainer Informatik OG.
 * This source is subject to the Microsoft Public License.
 *
 * See http://www.microsoft.com/opensource/licenses.mspx#Ms-PL.
 * All other rights reserved.
 *
 * (In other words you may copy, use, change and redistribute it without
 * any restrictions except for not suing me because it broke something.)
 *
 * THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR
 * PURPOSE.
 *
 * </pre>
 ***************************************************************************/
package info.unterrainer.java.tools.reporting.consoleprogressbar.drawablecomponents;

import info.unterrainer.java.tools.datastructures.Fader;
import info.unterrainer.java.tools.utils.NullUtils;
import info.unterrainer.java.tools.utils.StringUtils;

import java.io.PrintStream;

import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;

/**
 * This progress-bar draws a bar like:
 *
 * <pre>
 * {@code
 * file a: [####-----------]
 *
 * ...
 *
 * file a: [########-------]
 *
 * ...
 *
 * file a: [###############]
 * }
 * </pre>
 *
 * You may specify any other begin- , end- , full- or empty-character you like.<br />
 * This bar is only a good choice if your console supports control characters since for this representation you have to to clear all characters on each redraw
 * using '\b' (backspace).
 * <p>
 * <table>
 * <tr>
 * <td><b>prefix</b></td>
 * <td>"file a: "</td>
 * </tr>
 * <tr>
 * <td><b>begin</b></td>
 * <td>"["</td>
 * </tr>
 * <tr>
 * <td><b>end</b></td>
 * <td>"]"</td>
 * </tr>
 * <tr>
 * <td><b>full</b></td>
 * <td>'#'</td>
 * </tr>
 * <tr>
 * <td><b>empty</b></td>
 * <td>'-'</td>
 * </tr>
 * </table>
 */
@ExtensionMethod({ NullUtils.class, StringUtils.class })
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressBar implements DrawableComponent {

	@Getter
	@Setter
	private String prefix;
	@Getter
	@Setter
	private String begin;
	@Getter
	@Setter
	private String end;
	@Getter
	@Setter
	private Character full;
	@Getter
	@Setter
	private Character empty;

	@Builder
	public ProgressBar(@Nullable String begin, @Nullable String end, @Nullable Character full, @Nullable Character empty, @Nullable String prefix) {
		super();
		this.prefix = prefix.or("");
		this.begin = begin.or("[");
		this.end = end.or("]");
		this.full = full.or('#');
		this.empty = empty.or('-');
	}

	@Override
	public void draw(PrintStream ps, Fader fader, int width, boolean drawInitialized, int value, int lastValue) {
		String s = prefix;
		s += begin;
		s += full.repeat(value);
		s += empty.repeat(width - value);
		s += end;
		ps.print(s);
	}

	@Override
	public void remove(PrintStream ps, int width, int lastValue) {
		// Delete already drawn bar using command-characters.
		int len = prefix.length() + begin.length() + width + end.length();
		String s = "\b".repeat(len);
		s += " ".repeat(len);
		s += "\b".repeat(len);
		ps.print(s);
	}
}
