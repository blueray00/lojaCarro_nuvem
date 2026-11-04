
package br.org.edu.ifrn.LojaCarro.comissao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComissaoRepository extends JpaRepository<Comissao, Long> {

    List<Comissao> findByVendedorId(Long vendedorId);

    List<Comissao> findByVendaId(Long vendaId);
}
