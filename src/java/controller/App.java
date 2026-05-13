package controller;

import java.math.BigDecimal;
import java.util.List;

import entity.Autor;
import entity.Editora;
import entity.Livro;
import entity.TipoPublicacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import service.LivroService;

public final class App {

	private App() {
	}

	public static void main(String[] args) {
		
		System.out.println("\n=== Teste de Persistência - JPA com PostgreSQL ===\n");

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("aulajpa");
		EntityManager em = emf.createEntityManager();

		try {
			// Criar e persistir dados
			System.out.println("--- Persistindo Livros, Autores e Editoras ---");
			em.getTransaction().begin();

			// Criar Editoras
			Editora editora1 = new Editora(null, "Companhia das Letras", "São Paulo");
			Editora editora2 = new Editora(null, "Record", "Rio de Janeiro");

			// Criar Autores
			Autor autor1 = new Autor(null, "Machado de Assis", "Brasileiro");
			Autor autor2 = new Autor(null, "Carlos Drummond de Andrade", "Brasileiro");
			Autor autor3 = new Autor(null, "George Orwell", "Britânico");

			// Criar Livros
			Livro livro1 = new Livro(null, "Dom Casmurro", 1899, "978-1234567890", 
				new BigDecimal("45.90"), TipoPublicacao.IMPRESSO, editora1);
			livro1.adicionarAutor(autor1);

			Livro livro2 = new Livro(null, "Sentimento do Mundo", 1940, "978-0987654321",
				new BigDecimal("38.50"), TipoPublicacao.DIGITAL, editora1);
			livro2.adicionarAutor(autor2);

			Livro livro3 = new Livro(null, "1984", 1949, "978-5555555555",
				new BigDecimal("52.00"), TipoPublicacao.IMPRESSO, editora2);
			livro3.adicionarAutor(autor3);

			// Persistir tudo
			em.persist(editora1);
			em.persist(editora2);
			em.persist(autor1);
			em.persist(autor2);
			em.persist(autor3);
			em.persist(livro1);
			em.persist(livro2);
			em.persist(livro3);

			em.getTransaction().commit();

			// Listar todos os livros
			System.out.println("\n--- Livros Cadastrados ---");
			LivroService livroService = new LivroService(em);
			em.getTransaction().begin();
			List<Livro> livros = livroService.listarTodos();
			
			for (Livro l : livros) {
				System.out.println("\n" + l);
				System.out.println("  Editora: " + l.getEditora().getNome() + " - " + l.getEditora().getCidade());
				System.out.println("  Autores:");
				for (Autor a : l.getAutores()) {
					System.out.println("    - " + a.getNome() + " (" + a.getNacionalidade() + ")");
				}
			}
			em.getTransaction().commit();

			// Buscar livros de um autor específico
			System.out.println("\n--- Livros de Machado de Assis ---");
			em.getTransaction().begin();
			List<Livro> livrosMachado = livroService.buscarPorAutor("Machado");
			for (Livro l : livrosMachado) {
				System.out.println("  " + l.getTitulo() + " (" + l.getAnoPublicacao() + ")");
			}
			em.getTransaction().commit();

		} finally {
			em.close();
			emf.close();
		}

		System.out.println("\n=== Teste concluído com sucesso! ===\n");
	}
}
