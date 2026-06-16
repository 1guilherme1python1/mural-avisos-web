import { useEffect, useMemo, useState } from "react";

const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";
const API_URL = `${API_BASE_URL}/comunicados`;

const categorias = [
  "Falta de agua",
  "Seguranca",
  "Infraestrutura",
  "Iluminacao publica",
  "Coleta de lixo",
  "Outros",
];

const statusOptions = ["Ativo", "Resolvido"];

const initialForm = {
  titulo: "",
  descricao: "",
  categoria: "Outros",
  localizacao: "",
  nomeResponsavel: "",
  status: "Ativo",
};

const initialFilters = {
  categoria: "",
  status: "",
};

function App() {
  const [comunicados, setComunicados] = useState([]);
  const [formData, setFormData] = useState(initialForm);
  const [filters, setFilters] = useState(initialFilters);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    loadComunicados();
  }, []);

  const comunicadosFiltrados = useMemo(() => {
    return comunicados.filter((comunicado) => {
      const categoriaValida = !filters.categoria || normalizar(comunicado.categoria) === normalizar(filters.categoria);
      const statusValido = !filters.status || normalizar(comunicado.status) === normalizar(filters.status);
      return categoriaValida && statusValido;
    });
  }, [comunicados, filters]);

  const totalComunicados = comunicados.length;
  const totalAtivos = comunicados.filter((item) => normalizar(item.status) === "ativo").length;
  const totalResolvidos = comunicados.filter((item) => normalizar(item.status) === "resolvido").length;

  async function loadComunicados() {
    setLoading(true);
    setError("");

    try {
      const response = await fetch(API_URL);
      if (!response.ok) {
        throw new Error("Nao foi possivel carregar os comunicados.");
      }

      const data = await response.json();
      setComunicados(data);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  function handleFormChange(event) {
    const { name, value } = event.target;
    setFormData((current) => ({
      ...current,
      [name]: value,
    }));
  }

  function handleFilterChange(event) {
    const { name, value } = event.target;
    setFilters((current) => ({
      ...current,
      [name]: value,
    }));
  }

  function resetForm() {
    setFormData(initialForm);
    setEditingId(null);
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setSubmitting(true);
    setError("");

    const method = editingId ? "PUT" : "POST";
    const url = editingId ? `${API_URL}/${editingId}` : API_URL;

    try {
      const response = await fetch(url, {
        method,
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        const responseBody = await response.json().catch(() => null);
        throw new Error(responseBody?.message || "Nao foi possivel salvar o comunicado.");
      }

      resetForm();
      await loadComunicados();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSubmitting(false);
    }
  }

  function handleEdit(comunicado) {
    setEditingId(comunicado.id);
    setFormData({
      titulo: comunicado.titulo,
      descricao: comunicado.descricao,
      categoria: comunicado.categoria,
      localizacao: comunicado.localizacao,
      nomeResponsavel: comunicado.nomeResponsavel,
      status: comunicado.status,
    });
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  async function handleDelete(id) {
    const confirmed = window.confirm("Deseja realmente excluir este comunicado?");
    if (!confirmed) {
      return;
    }

    setError("");

    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        const responseBody = await response.json().catch(() => null);
        throw new Error(responseBody?.message || "Nao foi possivel excluir o comunicado.");
      }

      if (editingId === id) {
        resetForm();
      }

      await loadComunicados();
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  async function handleResolve(id) {
    setError("");

    try {
      const response = await fetch(`${API_URL}/${id}/resolver`, {
        method: "POST",
      });

      if (!response.ok) {
        const responseBody = await response.json().catch(() => null);
        throw new Error(responseBody?.message || "Nao foi possivel atualizar o status.");
      }

      if (editingId === id) {
        setFormData((current) => ({
          ...current,
          status: "Resolvido",
        }));
      }

      await loadComunicados();
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  function clearFilters() {
    setFilters(initialFilters);
  }

  return (
    <div className="page-shell">
      <header className="hero">
        <div className="hero-copy-block">
          <p className="eyebrow">Atividade Extensionista II</p>
          <h1>Mural Comunitario</h1>
          <p className="hero-copy">Seguranca, infraestrutura e avisos do bairro</p>
        </div>

        <div className="hero-badge">
          <span>{comunicadosFiltrados.length}</span>
          <small>comunicados em exibicao</small>
        </div>
      </header>

      <section className="summary-grid">
        <article className="summary-card summary-total">
          <span className="summary-label">Total de comunicados</span>
          <strong>{totalComunicados}</strong>
          <p>Quantidade geral cadastrada no sistema.</p>
        </article>

        <article className="summary-card summary-active">
          <span className="summary-label">Comunicados ativos</span>
          <strong>{totalAtivos}</strong>
          <p>Ocorrencias que ainda demandam acompanhamento.</p>
        </article>

        <article className="summary-card summary-resolved">
          <span className="summary-label">Comunicados resolvidos</span>
          <strong>{totalResolvidos}</strong>
          <p>Registros ja tratados ou finalizados.</p>
        </article>
      </section>

      <main className="layout">
        <section className="panel form-panel">
          <div className="panel-heading">
            <h2>{editingId ? "Editar comunicado" : "Cadastrar comunicado"}</h2>
            <p>Registre ocorrencias e avisos importantes para a comunidade.</p>
          </div>

          <form className="announcement-form" onSubmit={handleSubmit}>
            <label>
              Titulo
              <input
                name="titulo"
                value={formData.titulo}
                onChange={handleFormChange}
                maxLength="120"
                required
              />
            </label>

            <label>
              Descricao
              <textarea
                name="descricao"
                value={formData.descricao}
                onChange={handleFormChange}
                maxLength="1000"
                rows="5"
                required
              />
            </label>

            <label>
              Categoria
              <select name="categoria" value={formData.categoria} onChange={handleFormChange} required>
                {categorias.map((categoria) => (
                  <option key={categoria} value={categoria}>
                    {categoria}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Localizacao
              <input
                name="localizacao"
                value={formData.localizacao}
                onChange={handleFormChange}
                maxLength="150"
                required
              />
            </label>

            <label>
              Nome do responsavel
              <input
                name="nomeResponsavel"
                value={formData.nomeResponsavel}
                onChange={handleFormChange}
                maxLength="100"
                required
              />
            </label>

            <label>
              Status
              <select name="status" value={formData.status} onChange={handleFormChange} required>
                {statusOptions.map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </select>
            </label>

            <div className="form-actions">
              <button type="submit" disabled={submitting}>
                {submitting ? "Salvando..." : editingId ? "Atualizar" : "Cadastrar"}
              </button>
              {editingId && (
                <button type="button" className="secondary" onClick={resetForm}>
                  Cancelar
                </button>
              )}
            </div>
          </form>
        </section>

        <section className="panel content-panel">
          <div className="panel-heading panel-heading-inline">
            <div>
              <h2>Comunicados</h2>
              <p>Visualize, filtre e acompanhe os avisos ativos ou resolvidos.</p>
            </div>
            <div className="results-chip">
              {comunicadosFiltrados.length} resultado(s)
            </div>
          </div>

          <div className="filters">
            <label>
              Filtrar por categoria
              <select name="categoria" value={filters.categoria} onChange={handleFilterChange}>
                <option value="">Todas</option>
                {categorias.map((categoria) => (
                  <option key={categoria} value={categoria}>
                    {categoria}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Filtrar por status
              <select name="status" value={filters.status} onChange={handleFilterChange}>
                <option value="">Todos</option>
                {statusOptions.map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </select>
            </label>

            <button type="button" className="secondary clear-button" onClick={clearFilters}>
              Limpar filtros
            </button>
          </div>

          {error && <div className="feedback error">{error}</div>}
          {loading ? (
            <div className="feedback">Carregando comunicados...</div>
          ) : comunicadosFiltrados.length === 0 ? (
            <div className="feedback">Nenhum comunicado encontrado para os filtros informados.</div>
          ) : (
            <div className="announcement-list">
              {comunicadosFiltrados.map((comunicado) => (
                <article key={comunicado.id} className={`announcement-card category-${toSlug(comunicado.categoria)}`}>
                  <div className="announcement-top">
                    <div>
                      <span className={`category-chip category-${toSlug(comunicado.categoria)}`}>
                        {comunicado.categoria}
                      </span>
                      <h3>{comunicado.titulo}</h3>
                    </div>
                    <span className={`status-badge ${normalizar(comunicado.status)}`}>
                      {comunicado.status}
                    </span>
                  </div>

                  <p className="description">{comunicado.descricao}</p>

                  <dl className="details-grid">
                    <div>
                      <dt>Localizacao</dt>
                      <dd>{comunicado.localizacao}</dd>
                    </div>
                    <div>
                      <dt>Responsavel</dt>
                      <dd>{comunicado.nomeResponsavel}</dd>
                    </div>
                    <div>
                      <dt>Data de criacao</dt>
                      <dd>{formatDate(comunicado.dataCriacao)}</dd>
                    </div>
                    <div>
                      <dt>Status</dt>
                      <dd>{comunicado.status}</dd>
                    </div>
                  </dl>

                  <div className="card-actions">
                    <button type="button" className="secondary" onClick={() => handleEdit(comunicado)}>
                      Editar
                    </button>
                    <button type="button" className="danger" onClick={() => handleDelete(comunicado.id)}>
                      Excluir
                    </button>
                    {normalizar(comunicado.status) !== "resolvido" && (
                      <button type="button" onClick={() => handleResolve(comunicado.id)}>
                        Marcar como resolvido
                      </button>
                    )}
                  </div>
                </article>
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

function formatDate(value) {
  if (!value) {
    return "-";
  }

  return new Date(value).toLocaleString("pt-BR");
}

function normalizar(value) {
  return value.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");
}

function toSlug(value) {
  return normalizar(value).replace(/\s+/g, "-");
}

export default App;
