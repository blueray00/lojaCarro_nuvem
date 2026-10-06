package br.org.edu.ifrn.LojaCarro.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UsuarioApiService {

    private final RestClient restClient;

    public UsuarioApiService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

    public String buscarNomePorId(Long usuarioId) {

        UsuarioResponse usuario = restClient.get()
                .uri("/usuarios/{id}", usuarioId)
                .retrieve()
                .body(UsuarioResponse.class);

        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        return usuario.getNome();
    }

    private static class UsuarioResponse {

        private Long id;
        private String nome;
        private String cargo;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCargo() {
            return cargo;
        }

        public void setCargo(String cargo) {
            this.cargo = cargo;
        }
    }
}