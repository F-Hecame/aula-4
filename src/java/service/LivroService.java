package service;

import java.util.List;

import entity.Livro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class LivroService {

	private EntityManager em;

	public LivroService(EntityManager em) {
		this.em = em;
	}

	public void salvar(Livro livro) {
		em.persist(livro);
	}

	public void atualizar(Livro livro) {
		em.merge(livro);
	}

	public void deletar(Integer id) {
		Livro livro = em.find(Livro.class, id);
		if (livro != null) {
			em.remove(livro);
		}
	}

	public Livro buscarPorId(Integer id) {
		return em.find(Livro.class, id);
	}

	public List<Livro> listarTodos() {
		TypedQuery<Livro> query = em.createQuery("SELECT l FROM Livro l", Livro.class);
		return query.getResultList();
	}

	public List<Livro> buscarPorAutor(String nomeAutor) {
		TypedQuery<Livro> query = em.createQuery(
			"SELECT DISTINCT l FROM Livro l JOIN l.autores a WHERE a.nome LIKE :nome",
			Livro.class
		);
		query.setParameter("nome", "%" + nomeAutor + "%");
		return query.getResultList();
	}

	public List<Livro> buscarPorEditora(String nomeEditora) {
		TypedQuery<Livro> query = em.createQuery(
			"SELECT l FROM Livro l WHERE l.editora.nome LIKE :nome",
			Livro.class
		);
		query.setParameter("nome", "%" + nomeEditora + "%");
		return query.getResultList();
	}
}
