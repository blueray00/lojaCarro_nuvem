import { useState } from "react"
import "./App.css"

function App() {
    const [pagina, setPagina] = useState("inicio")

    const [usuarios, setUsuarios] = useState([])

    const [nome, setNome] = useState("")
    const [cargo, setCargo] = useState("")

    const [usuarioEditando, setUsuarioEditando] = useState(null)

    function carregarUsuarios() {
        fetch("http://localhost:8080/usuarios")
            .then((resposta) => resposta.json())
            .then((dados) => {
                setUsuarios(dados)
            })
    }

    function cadastrarUsuario() {
        fetch("http://localhost:8080/usuarios", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
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
                "Content-Type": "application/json"
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
            method: "DELETE"
        })
            .then(() => {
                const novaLista = usuarios.filter(
                    (usuario) => usuario.id !== id
                )

                setUsuarios(novaLista)
            })
    }

    function iniciarEdicao(usuario) {
        setUsuarioEditando(usuario)
        setNome(usuario.nome)
        setCargo(usuario.cargo)
    }

    function cancelarEdicao() {
        setUsuarioEditando(null)
        setNome("")
        setCargo("")
    }

    return (
        <div className="app">

            <header className="cabecalho">
                <h1>Loja Carro</h1>
            </header>

            <nav className="menu">
                <button onClick={() => setPagina("carros")}>
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
                        <p>Aqui ficarão os carros.</p>
                    </div>
                )}

                {pagina === "usuarios" && (
                    <div>

                        <h2>Usuários</h2>

                        <div className="formulario">

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
                                        onClick={cancelarEdicao}
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
                                                onClick={() => iniciarEdicao(usuario)}
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