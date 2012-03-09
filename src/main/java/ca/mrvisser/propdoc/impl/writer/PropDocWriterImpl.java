package ca.mrvisser.propdoc.impl.writer;

import ca.mrvisser.propdoc.api.PropDoc;
import ca.mrvisser.propdoc.api.PropDocWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author branden
 */
public class PropDocWriterImpl implements PropDocWriter {
	private static final String VELOCITY_CONFIG_FILE = "ca/mrvisser/propdoc/velocity/config/velocity.properties";
	
	private String templateFile;
	
	public PropDocWriterImpl(String templateFile) {
		this.templateFile = templateFile;
	}
	
	@Override
	public void write(PropDoc propDoc, OutputStream out) throws IOException {
		Map<String, Object> context = buildContext(propDoc);
		VelocityEngine engine = new VelocityEngine();
		
		Writer writer = null;
		
		try {
			writer = new OutputStreamWriter(out);
			engine.init(getVelocityConfiguration());
			engine.mergeTemplate(getTemplateFile(), new VelocityContext(context), writer);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			//what could this be? Assume it's runtime since it's coming from a velocity template which
			//can't have checked exceptions.
			throw new RuntimeException(e);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public String getTemplateFile() {
		return templateFile;
	}
	
	private Map<String, Object> buildContext(PropDoc propDoc) {
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("propDoc", propDoc);
		return context;
	}
	
	private Properties getVelocityConfiguration() throws IOException {
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(
							VELOCITY_CONFIG_FILE);
			props.load(in);
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return props;
	}
}
