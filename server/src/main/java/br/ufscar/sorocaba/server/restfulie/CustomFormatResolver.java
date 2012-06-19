package br.ufscar.sorocaba.server.restfulie;

import static org.apache.commons.lang.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.http.FormatResolver;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.view.AcceptHeaderToFormat;

@Component
@RequestScoped
public class CustomFormatResolver implements FormatResolver {

	private final HttpServletRequest request;
	private final AcceptHeaderToFormat acceptHeaderToFormat;

	private static final Pattern ACCEPT_PATTERN = Pattern.compile("(.+/)(vnd\\..*\\+)?(.*)");
	
	public CustomFormatResolver(HttpServletRequest request, AcceptHeaderToFormat acceptHeaderToFormat) {
		this.request = request;
		this.acceptHeaderToFormat = acceptHeaderToFormat;
	}

	@Override
	public String getAcceptFormat() {
		String format = request.getParameter("_format");
		
		if (!isBlank(format)) {
			return format;
		}
		
		return acceptHeaderToFormat.getFormat(this.extractFormat());
	}

	private String extractFormat() {
		Matcher matcher = ACCEPT_PATTERN.matcher(request.getHeader("Accept"));
		if (matcher.matches()) {
			return matcher.replaceAll("$1$3");
		}
		return null;
	}

	
	public String getVendor() {
		return this.extractVendor(request.getHeader("Accept"));
	}
	
	private String extractVendor(String format) {
		return defaultIfEmpty(format, "").contains("vnd.ufscar") ? format.substring(format.indexOf("vnd"), format.indexOf("+")) : "";
	}
}
