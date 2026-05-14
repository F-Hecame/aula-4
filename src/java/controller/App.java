package controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import entity.Autor;
import entity.Editora;
import entity.Livro;
import entity.TipoPublicacao;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("aulajpa");
        EntityManager em = emf.createEntityManager();

        // ============================================================
        // CADASTRO
        // ============================================================
        em.getTransaction().begin();

        Autor autor1 = new Autor();
        autor1.setNome("Machado de Assis");
        autor1.setNacionalidade("Brasileira");
        em.persist(autor1);

        Autor autor2 = new Autor();
        autor2.setNome("Clarice Lispector");
        autor2.setNacionalidade("Brasileira");
        em.persist(autor2);

        Editora editora1 = new Editora();
        editora1.setNome("Companhia das Letras");
        editora1.setCidade("Sao Paulo");
        em.persist(editora1);

        Editora editora2 = new Editora();
        editora2.setNome("Rocco");
        editora2.setCidade("Rio de Janeiro");
        em.persist(editora2);

        // Livro 1 — um autor
        Livro livro1 = new Livro();
        livro1.setTitulo("Dom Casmurro");
        livro1.setAnoPublicacao(1899);
        livro1.setIsbn("978-85-359-0277-5");
        livro1.setPreco(new BigDecimal("49.90"));
        livro1.setTipo(TipoPublicacao.IMPRESSO);
        livro1.setAutores(Arrays.asList(autor1));        // N:N
        livro1.setEditora(editora1);                     // N:1
        em.persist(livro1);

        // Livro 2 — dois autores (exemplo de N:N)
        Livro livro2 = new Livro();
        livro2.setTitulo("A Hora da Estrela");
        livro2.setAnoPublicacao(1977);
        livro2.setIsbn("978-85-325-2086-2");
        livro2.setPreco(new BigDecimal("34.50"));
        livro2.setTipo(TipoPublicacao.DIGITAL);
        livro2.setAutores(Arrays.asList(autor2, autor1)); // N:N com 2 autores
        livro2.setEditora(editora2);                      // N:1
        em.persist(livro2);

        em.getTransaction().commit();
        System.out.println(">>> 2 livro(s) cadastrado(s) com sucesso!");

        // ============================================================
        // CONSULTA — TODOS OS LIVROS
        // ============================================================
        System.out.println("\n================================================");
        System.out.println("         CONSULTA - TODOS OS LIVROS             ");
        System.out.println("================================================");

        TypedQuery<Livro> query = em.createQuery("SELECT l FROM Livro l", Livro.class);
        List<Livro> livros = query.getResultList();

        for (Livro l : livros) {
            System.out.println("------------------------------------------------");
            System.out.printf("ID:       %d%n",      l.getId());
            System.out.printf("Titulo:   %s%n",      l.getTitulo());
            System.out.printf("Ano:      %d%n",      l.getAnoPublicacao());
            System.out.printf("ISBN:     %s%n",      l.getIsbn());
            System.out.printf("Preco:    R$ %.2f%n", l.getPreco());
            System.out.printf("Tipo:     %s%n",      l.getTipo());
            System.out.printf("Editora:  %s%n",      l.getEditora().getNome());

            System.out.print("Autores:  ");
            for (Autor a : l.getAutores()) {
                System.out.print(a.getNome() + " ");
            }
            System.out.println();
        }
        System.out.println("------------------------------------------------");
        System.out.println("Total de livros: " + livros.size());

        em.close();
        emf.close();
    }
}