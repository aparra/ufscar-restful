require 'rubygems'
require 'restfulie'

# accepts: application/vnd.ufscar.usuario.(v1|v2)+(json|xml)
response = Restfulie.at("http://localhost:8080/restful-server/servicos/usuarios").accepts('application/vnd.ufscar.usuario.v1+json').get
usuario_media = response.resource.usuarioMediaType

puts "Usuario: #{usuario_media.usuario}"
usuario_media.links.each do |link|
  puts "link: #{link.rel} => #{link.href}"
end
