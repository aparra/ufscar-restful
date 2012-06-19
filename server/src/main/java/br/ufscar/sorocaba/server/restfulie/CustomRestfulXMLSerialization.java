package br.ufscar.sorocaba.server.restfulie;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.config.Configuration;
import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.restfulie.Restfulie;
import br.com.caelum.vraptor.restfulie.serialization.RestfulSerialization;
import br.com.caelum.vraptor.serialization.ProxyInitializer;
import br.com.caelum.vraptor.serialization.Serializer;
import br.com.caelum.vraptor.serialization.xstream.XStreamBuilder;
import br.ufscar.sorocaba.server.converter.registry.ConverterRegistry;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;

@Component
@RequestScoped
public class CustomRestfulXMLSerialization extends RestfulSerialization {

	private final ConverterRegistry registry;
	private final HttpServletResponse response;
	private final CustomFormatResolver formatResolver;

    public CustomRestfulXMLSerialization(HttpServletResponse response, TypeNameExtractor extractor, Restfulie restfulie, Configuration config, ProxyInitializer initializer, XStreamBuilder builder, ConverterRegistry registry, CustomFormatResolver formatResolver) {
        super(response, extractor, restfulie, config, initializer, builder);
        this.registry = registry;
        this.formatResolver = formatResolver;
        this.response = response;
    }

    @Override
    public <T> Serializer from(T object, String alias) {
        Serializer serializer = super.from(object, alias);
        response.setContentType("application/xml; charset=UTF-8");
        return serializer;
    }
    
	@Override
	protected XStream getXStream() {
		XStream xStream = super.getXStream();

		for (Converter converter : registry.load(formatResolver.getVendor())) {
            xStream.registerConverter(converter, XStream.PRIORITY_LOW);    
        }

		for (Converter converter : registry.load("base.converters")) {
		    xStream.registerConverter(converter, XStream.PRIORITY_LOW);
        }
		
		return xStream;
	}
}
