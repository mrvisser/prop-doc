/**
 * Copyright (c) 2011, Branden Visser (mrvisser at gmail dot com)
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package ca.mrvisser.propdoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ca.mrvisser.propdoc.api.PropDoc;
import ca.mrvisser.propdoc.api.PropDocWriter;
import ca.mrvisser.propdoc.impl.writer.VelocityPropDocWriterImpl;
import ca.mrvisser.propdoc.util.ResourceUtil;

/**
 * @author branden
 */
public final class VelocityMain {

	private static final String PROPDOC_OUTPUT_PATH_PROPERTY = "propdoc.output.path";
	private static final String VELOCITY_TEMPLATE_PATH_PROPERTY = "velocity.template.url";
	private static final String PROPERTIES_FILE_URL_PROPERTY = "properties.file.url";
	private static final String DEFAULT_TEMPLATE_PATH = "classpath:ca/mrvisser/propdoc/velocity/template.html.vm";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		String outputPath = getOutputPath();
		String propertiesFileUrl = getPropertiesFileUrl();
		if (outputPath == null || propertiesFileUrl == null) {
			printUsage();
			System.exit(0);
		}
		
		String templatePath = getTemplatePath();
		File outputFile = new File(outputPath);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = ResourceUtil.createInputStreamFromUrl(propertiesFileUrl);
			PropDoc propDoc = new PropDoc(in);
			
			out = new FileOutputStream(outputFile);
			PropDocWriter writer = new VelocityPropDocWriterImpl(templatePath);
			
			writer.write(propDoc, out);
			
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (Exception e) {}
			
			try {
				if (out != null)
					out.close();
			} catch (Exception e) {}
		}
	}

	public static final void printUsage() {
		System.out.printf("Usage: java \\\n\t-D%s=<output propdoc file-system path> " +
				"\\\n\t-D%s=<source properties file URL> (e.g., file://C:\\Temp\\config.properties; e.g., classpath:org/my/config/config.properties)" +
				"\\\n\t [-D%s=<velocity template url> (e.g., file://C:\\Temp\\propdoc.vm; default: %s)]\\\n" +
				"\t-jar <propdoc executable>.jar\n", PROPDOC_OUTPUT_PATH_PROPERTY, PROPERTIES_FILE_URL_PROPERTY,
				VELOCITY_TEMPLATE_PATH_PROPERTY, DEFAULT_TEMPLATE_PATH);
	}
	
	public static String getOutputPath() {
		return System.getProperty(PROPDOC_OUTPUT_PATH_PROPERTY);
	}
	
	public static String getTemplatePath() {
		return System.getProperty(VELOCITY_TEMPLATE_PATH_PROPERTY, DEFAULT_TEMPLATE_PATH);
	}
	
	public static String getPropertiesFileUrl() {
		return System.getProperty(PROPERTIES_FILE_URL_PROPERTY);
	}
}
