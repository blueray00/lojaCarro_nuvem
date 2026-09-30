import { useState } from "react"
import "./App.css"

function App() {
    const [pagina, setPagina] = useState("inicio")

    const [usuarios, setUsuarios] = useState([])
    const [carros, setCarros] = useState([])

    const [nome, setNome] = useState("")
    const [cargo, setCargo] = useState("")

    const [modelo, setModelo] = useState("")
    const [ano, setAno] = useState("")
    const [preco, setPreco] = useState("")

    const [usuarioAtual, setUsuarioAtual] = useState("")

    const [usuarioEditando, setUsuarioEditando] = useState(null)
    const [carroEditando, setCarroEditando] = useState(null)

    function carregarUsuarios() {
        fetch("http://localhost:8080/usuarios", {
            headers: {
                "X-Usuario-Nome": usuarioAtual
            }
        })
            .then((resposta) => resposta.json())
            .then((dados) => {
                setUsuarios(dados)
            })
    }

    function carregarCarros() {
        fetch("http://localhost:8080/carro/listarCarros", {
            headers: {
                "X-Usuario-Nome": usuarioAtual
            }
        })
            .then((resposta) => resposta.json())
            .then((dados) => {
                setCarros(dados)
            })
    }

    function cadastrarUsuario() {
        fetch("http://localhost:8080/usuarios", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "X-Usuario-Nome": usuarioAtual
            },
            body: JSON.stringify({
                nome: nome,
                cargo: cargo
            })
        })
            .then((resposta) => resposta.json())
            .then((usuario) => {
                setUsuarios([...usuarios, usuario])
                setNome("")
                setCargo("")
            })
    }

    function editarUsuario() {
        fetch(`http://localhost:8080/usuarios/${usuarioEditando.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "X-Usuario-Nome": usuarioAtual
            },
            body: JSON.stringify({
                nome: nome,
                cargo: cargo
            })
        })
            .then((resposta) => resposta.json())
            .then((usuarioAtualizado) => {
                const novaLista = usuarios.map((usuario) =>
                    usuario.id === usuarioAtualizado.id
                        ? usuarioAtualizado
                        : usuario
                )

                setUsuarios(novaLista)

                setNome("")
                setCargo("")
                setUsuarioEditando(null)
            })
    }

    function excluirUsuario(id) {
        fetch(`http://localhost:8080/usuarios/${id}`, {
            method: "DELETE",
            headers: {
                "X-Usuario-Nome": usuarioAtual
            }
        })
            .then(() => {
                const novaLista = usuarios.filter(
                    (usuario) => usuario.id !== id
                )

                setUsuarios(novaLista)
            })
    }

    function cadastrarCarro() {
        fetch("http://localhost:8080/carro/salvar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "X-Usuario-Nome": usuarioAtual
            },
            body: JSON.stringify({
                modelo: modelo,
                ano: Number(ano),
                preco: Number(preco)
            })
        })
            .then((resposta) => resposta.json())
            .then((carro) => {
                setCarros([...carros, carro])
                limparFormularioCarro()
            })
    }

    function editarCarro() {
        fetch(`http://localhost:8080/carro/${carroEditando.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "X-Usuario-Nome": usuarioAtual
            },
            body: JSON.stringify({
                modelo: modelo,
                ano: Number(ano),
                preco: Number(preco)
            })
        })
            .then((resposta) => resposta.json())
            .then((carroAtualizado) => {
                const novaLista = carros.map((carro) =>
                    carro.id === carroAtualizado.id
                        ? carroAtualizado
                        : carro
                )

                setCarros(novaLista)
                limparFormularioCarro()
            })
    }

    function excluirCarro(id) {
        fetch(`http://localhost:8080/carro/${id}`, {
            method: "DELETE",
            headers: {
                "X-Usuario-Nome": usuarioAtual
            }
        })
            .then(() => {
                const novaLista = carros.filter(
                    (carro) => carro.id !== id
                )

                setCarros(novaLista)
            })
    }

    function iniciarEdicaoUsuario(usuario) {
        setUsuarioEditando(usuario)
        setNome(usuario.nome)
        setCargo(usuario.cargo)
    }

    function cancelarEdicaoUsuario() {
        setUsuarioEditando(null)
        setNome("")
        setCargo("")
    }

    function iniciarEdicaoCarro(carro) {
        setCarroEditando(carro)
        setModelo(carro.modelo)
        setAno(carro.ano)
        setPreco(carro.preco)
    }

    function limparFormularioCarro() {
        setCarroEditando(null)
        setModelo("")
        setAno("")
        setPreco("")
    }

    return (
        <div className="app">

            <header className="cabecalho">
                <h1>Loja Carro</h1>
            </header>

            <nav className="menu">

                <button
                    onClick={() => {
                        setPagina("carros")
                        carregarCarros()
                    }}
                >
                    Carros
                </button>

                <button
                    onClick={() => {
                        setPagina("usuarios")
                        carregarUsuarios()
                    }}
                >
                    Usuários
                </button>

            </nav>

            <main className="conteudo">

                {pagina === "inicio" && (
                    <div>
                        <h2>Bem-vindo!</h2>
                        <p>Escolha uma opção acima.</p>
                    </div>
                )}

                {pagina === "carros" && (
                    <div>

                        <h2>Carros</h2>

                        <div className="formulario">

                            <input
                                type="text"
                                placeholder="Usuário atual"
                                value={usuarioAtual}
                                onChange={(evento) => setUsuarioAtual(evento.target.value)}
                            />

                            <input
                                type="text"
                                placeholder="Modelo"
                                value={modelo}
                                onChange={(evento) => setModelo(evento.target.value)}
                            />

                            <input
                                type="number"
                                placeholder="Ano"
                                value={ano}
                                onChange={(evento) => setAno(evento.target.value)}
                            />

                            <input
                                type="number"
                                placeholder="Preço"
                                value={preco}
                                onChange={(evento) => setPreco(evento.target.value)}
                            />

                            {carroEditando === null ? (
                                <button
                                    className="botao botao-cadastrar"
                                    onClick={cadastrarCarro}
                                >
                                    Cadastrar
                                </button>
                            ) : (
                                <>
                                    <button
                                        className="botao botao-editar"
                                        onClick={editarCarro}
                                    >
                                        Salvar alteração
                                    </button>

                                    <button
                                        className="botao botao-cancelar"
                                        onClick={limparFormularioCarro}
                                    >
                                        Cancelar
                                    </button>
                                </>
                            )}

                        </div>

                        <h3>Carros cadastrados</h3>

                        {carros.length === 0 ? (
                            <p className="mensagem-vazia">
                                Nenhum carro encontrado.
                            </p>
                        ) : (
                            <ul className="lista-usuarios">

                                {carros.map((carro) => (
                                    <li className="usuario" key={carro.id}>

                                        <div className="usuario-info">

                                            <span className="usuario-id">
                                                ID: {carro.id}
                                            </span>

                                            <span className="usuario-nome">
                                                {carro.modelo}
                                            </span>

                                            <span className="usuario-cargo">
                                                Ano: {carro.ano}
                                            </span>

                                            <span className="usuario-cargo">
                                                Preço: R$ {carro.preco}
                                            </span>

                                        </div>

                                        <div className="usuario-acoes">

                                            <button
                                                className="botao botao-editar"
                                                onClick={() => iniciarEdicaoCarro(carro)}
                                            >
                                                Editar
                                            </button>

                                            <button
                                                className="botao botao-excluir"
                                                onClick={() => excluirCarro(carro.id)}
                                            >
                                                Excluir
                                            </button>

                                        </div>

                                    </li>
                                ))}

                            </ul>
                        )}

                    </div>
                )}

                {pagina === "usuarios" && (
                    <div>

                        <h2>Usuários</h2>

                        <div className="formulario">

                            <input
                                type="text"
                                placeholder="Usuário atual"
                                value={usuarioAtual}
                                onChange={(evento) => setUsuarioAtual(evento.target.value)}
                            />

                            <input
                                type="text"
                                placeholder="Nome"
                                value={nome}
                                onChange={(evento) => setNome(evento.target.value)}
                            />

                            <input
                                type="text"
                                placeholder="Cargo"
                                value={cargo}
                                onChange={(evento) => setCargo(evento.target.value)}
                            />

                            {usuarioEditando === null ? (
                                <button
                                    className="botao botao-cadastrar"
                                    onClick={cadastrarUsuario}
                                >
                                    Cadastrar
                                </button>
                            ) : (
                                <>
                                    <button
                                        className="botao botao-editar"
                                        onClick={editarUsuario}
                                    >
                                        Salvar alteração
                                    </button>

                                    <button
                                        className="botao botao-cancelar"
                                        onClick={cancelarEdicaoUsuario}
                                    >
                                        Cancelar
                                    </button>
                                </>
                            )}

                        </div>

                        <h3>Usuários cadastrados</h3>

                        {usuarios.length === 0 ? (
                            <p className="mensagem-vazia">
                                Nenhum usuário encontrado.
                            </p>
                        ) : (
                            <ul className="lista-usuarios">

                                {usuarios.map((usuario) => (
                                    <li className="usuario" key={usuario.id}>

                                        <div className="usuario-info">

                                            <span className="usuario-id">
                                                ID: {usuario.id}
                                            </span>

                                            <span className="usuario-nome">
                                                {usuario.nome}
                                            </span>

                                            <span className="usuario-cargo">
                                                {usuario.cargo}
                                            </span>

                                        </div>

                                        <div className="usuario-acoes">

                                            <button
                                                className="botao botao-editar"
                                                onClick={() => iniciarEdicaoUsuario(usuario)}
                                            >
                                                Editar
                                            </button>

                                            <button
                                                className="botao botao-excluir"
                                                onClick={() => excluirUsuario(usuario.id)}
                                            >
                                                Excluir
                                            </button>

                                        </div>

                                    </li>
                                ))}

                            </ul>
                        )}

                    </div>
                )}

            </main>

        </div>
    )
}

export default App