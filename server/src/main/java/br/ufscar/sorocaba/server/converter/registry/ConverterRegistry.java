package br.ufscar.sorocaba.server.converter.registry;

import static br.ufscar.sorocaba.server.util.ClassLoaderUtils.getClassesForPackage;
import static org.apache.commons.lang.ClassUtils.isAssignable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import br.com.caelum.vraptor.ioc.Component;

import com.thoughtworks.xstream.converters.Converter;

@Component
public class ConverterRegistry {

    private Map<String, Set<Converter>> map = new HashMap<String, Set<Converter>>();

	@PostConstruct
    @SuppressWarnings("unused")
    private void registry() {
    	for (Class<?> clazz : getClassesForPackage("br.ufscar.sorocaba.server.mediatype")) {
    		if (isAssignable(clazz, Converter.class)) {
    			Registry registry = clazz.getAnnotation(Registry.class);

    			if (registry == null) {
    				throw new RuntimeException("No @Registry present for: " + clazz.getName());
    			}
    			
    			try {
					this.addConverter(registry.value(), (Converter) clazz.newInstance());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
    		}
		}
    }

    private void addConverter(String key, Converter converter) {
    	Set<Converter> converters = map.get(key);
    	
    	if (converters == null) {
    		converters = new HashSet<Converter>();
    		map.put(key, converters);
    	}
    	
    	converters.add(converter);
    }
    
    public Set<Converter> load(String key) {
        Set<Converter> converters = map.get(key);

        if (converters == null) {
            return Collections.emptySet();
        }

        return converters;
    }
}
