package br.ufscar.sorocaba.server.service;

import static br.com.caelum.vraptor.view.Results.representation;

import java.util.Date;

import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.fixturefactory.Fixture;
import br.com.fixturefactory.Rule;
import br.ufscar.sorocaba.server.mediatype.usuario.UsuarioMediaType;
import br.ufscar.sorocaba.server.model.Usuario;

@Resource
@Path("/servicos/usuarios")
public class UsuarioRest {

	private final Result result;
	
	public UsuarioRest(Result result) {
		this.result = result;
	}

	@Put("/{usuario.id}")
	public void editar(Usuario usuario) { }

	@Delete("/{usuario.id}")
	public void remover(Usuario usuario) { }
	
	@Get("")
	public void buscar() {
		Fixture.of(Usuario.class).addTemplate("valid", new Rule() {{
			add("id", random(Long.class, range(1, 50)));
			add("login", firstName());
			add("ultimoAcesso", new Date());
			add("senha", regex("\\w{8}"));
		}});
		
		Usuario usuario = Fixture.from(Usuario.class).gimme("valid");
		result.use(representation()).from(new UsuarioMediaType(usuario)).recursive().serialize();
	}
}
