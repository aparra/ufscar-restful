package br.ufscar.sorocaba.server.util;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ClassLoaderUtils {
    
	public static Set<Class<?>> getClassesForPackage(String packageName) {
		return getClassesForPackage(packageName, true);
	}
	
	public static Set<Class<?>> getClassesForPackage(String packageName, boolean recursive) {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

			Enumeration<URL> resources = classLoader.getResources(packageName.replace('.', '/'));
			while (resources.hasMoreElements()) {
				URL res = resources.nextElement();
				
				if(res.getProtocol().matches("(jar|zip)")) {
					res = new URL((res.getProtocol().equals("zip") || !res.getFile().contains("file:") ? 
							"file:".concat(res.getFile().startsWith("/") ? "": "/") : "").concat(res.getFile().split("!")[0]));
				}
				if (res.getFile().contains(".jar")) {
					JarInputStream jarInputStream = new JarInputStream(res.openStream());
					JarEntry entry = jarInputStream.getNextJarEntry();


					while (entry != null) {
						if (entry.getName().startsWith(packageName.replace('.', '/')) && entry.getName().endsWith(".class") && !entry.getName().contains("$")) {
							classes.add(Class.forName(entry.getName().replace(".class","").replace("/",".")));
						}

						entry = jarInputStream.getNextJarEntry();
					}
				} else {
					classes.addAll(scanClassesFromDirectory(new File(URLDecoder.decode(res.getPath(), "UTF-8")), packageName, recursive));
				}	
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid package", e);
		}

		return classes;
	}

	private static Collection<Class<?>> scanClassesFromDirectory(File directory, String packageName, boolean recursive) {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		if (directory.exists() && directory.isDirectory()) {
			String[] files = directory.list();
			for (String fileItem : files) {
				if(fileItem.endsWith(".class")) {
					try {
						classes.add(Class.forName(packageName.concat(".").concat(fileItem.replace(".class",""))));
					} catch (Exception e) {
						throw new IllegalArgumentException("Invalid package " + packageName);
					}
				} else if(!fileItem.matches(".+\\..+")) {
					File dir = new File(directory,fileItem);
					if(recursive && dir.isDirectory()) {
						classes.addAll(scanClassesFromDirectory(dir, packageName.concat(".").concat(fileItem), recursive));
					}
				}

			}
		}
		return classes;
	}
}
