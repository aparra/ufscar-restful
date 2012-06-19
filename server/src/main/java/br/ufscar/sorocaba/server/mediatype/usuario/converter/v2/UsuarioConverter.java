package br.ufscar.sorocaba.server.mediatype.usuario.converter.v2;

import static br.ufscar.sorocaba.server.util.XStreamWriterUtil.writeNode;

import java.text.SimpleDateFormat;

import br.ufscar.sorocaba.server.converter.registry.Registry;
import br.ufscar.sorocaba.server.model.Usuario;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

@Registry("vnd.ufscar.usuario.v2")
public class UsuarioConverter implements Converter {

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return type.equals(Usuario.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		Usuario usuario = (Usuario) source;

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		writeNode(writer, "login", usuario.getLogin());
		writeNode(writer, "ultimoAcesso", dateFormat.format(usuario.getUltimoAcesso()));
		writeNode(writer, "senha", usuario.getSenha());
		writeNode(writer, "atributo1", "valor1");
		writeNode(writer, "atributo2", "valor2");
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		return null;
	}
}
