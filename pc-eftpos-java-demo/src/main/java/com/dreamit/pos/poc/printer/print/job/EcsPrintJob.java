package com.dreamit.pos.poc.printer.print.job;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ssun on 30/10/17.
 */
public class EcsPrintJob {

	private List<EcsPrintLine> printLines = new ArrayList<>();

	public void loadDefaultLines(List<String> lines){

		List<EcsPrintTextLine> textLines = lines.stream().map(EcsPrintTextLine::new).collect(Collectors.toList());
		printLines.addAll(textLines);
	}

	public List<EcsPrintLine> getPrintLines() {
		return printLines;
	}

	public void setPrintLines(List<EcsPrintLine> printLines) {
		this.printLines = printLines;
	}
}
