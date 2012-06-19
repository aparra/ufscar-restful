package br.ufscar.sorocaba.server.mediatype.usuario;

import java.io.Serializable;

import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaResource;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;
import br.ufscar.sorocaba.server.model.Usuario;
import br.ufscar.sorocaba.server.service.UsuarioRest;

public class UsuarioMediaType implements HypermediaResource, Serializable {

	private static final long serialVersionUID = 8235472813564735847L;
	
	private Usuario usuario;

	public UsuarioMediaType(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	@Override
	public void configureRelations(RelationBuilder builder) {
		builder.relation("editar").uses(UsuarioRest.class).editar(usuario);
		builder.relation("remover").uses(UsuarioRest.class).remover(usuario);
	}
}
