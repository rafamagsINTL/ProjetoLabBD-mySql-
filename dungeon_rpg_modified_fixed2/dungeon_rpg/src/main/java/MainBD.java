import dao.*;
import model.*;

import java.util.List;
import java.util.Scanner;

public class MainBD {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ClasseDAO classeDAO = new ClasseDAO();
        PersonagemDAO personagemDAO = new PersonagemDAO();
        HabilidadeDAO habilidadeDAO = new HabilidadeDAO();
        PersonagemHabilidadeDAO phDAO = new PersonagemHabilidadeDAO();
        CombateLogDAO combateLogDAO = new CombateLogDAO();

        int opcao;
        do {
            System.out.println("\n=== MENU BANCO DE DADOS RPG ===");
            System.out.println("1 - Gerenciar Classes");
            System.out.println("2 - Gerenciar Personagens");
            System.out.println("3 - Gerenciar Habilidades");
            System.out.println("4 - Gerenciar Personagem-Habilidade");
            System.out.println("5 - Gerenciar Combates");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> menuClasses(classeDAO);
                case 2 -> menuPersonagens(personagemDAO, classeDAO);
                case 3 -> menuHabilidades(habilidadeDAO);
                case 4 -> menuPersonagemHabilidade(phDAO);
                case 5 -> menuCombates(combateLogDAO);
            }
        } while (opcao != 0);

        System.out.println("Encerrando...");
    }

    // ===============================
    // CLASSES
    // ===============================
    private static void menuClasses(ClasseDAO classeDAO) {
        int op;
        do {
            System.out.println("\n-- Classes --");
            System.out.println("1 - Listar");
            System.out.println("2 - Inserir");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Deletar");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            op = Integer.parseInt(scanner.nextLine());

            switch (op) {
                case 1 -> classeDAO.listarTodos().forEach(System.out::println);
                case 2 -> {
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Descrição: ");
                    String desc = scanner.nextLine();
                    classeDAO.inserir(new Classe(nome, desc));
                }
                case 3 -> {
                    System.out.print("ID da classe para atualizar: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    Classe c = classeDAO.buscarPorId(id);
                    if (c == null) {
                        System.out.println("Classe não encontrada.");
                        break;
                    }
                    System.out.print("Novo nome (" + c.getNome() + "): ");
                    String nome = scanner.nextLine();
                    if (!nome.isBlank()) c.setNome(nome);

                    System.out.print("Nova descrição (" + c.getDescricao() + "): ");
                    String desc = scanner.nextLine();
                    if (!desc.isBlank()) c.setDescricao(desc);

                    classeDAO.atualizar(c);
                }
                case 4 -> {
                    System.out.print("ID da classe para deletar: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    classeDAO.deletar(id);
                }
            }
        } while (op != 0);
    }

    // ===============================
    // PERSONAGENS
    // ===============================
    private static void menuPersonagens(PersonagemDAO personagemDAO, ClasseDAO classeDAO) {
        int op;
        do {
            System.out.println("\n-- Personagens --");
            System.out.println("1 - Listar (com classe)");
            System.out.println("2 - Inserir");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Deletar");
            System.out.println("5 - Listar com JOIN");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            op = Integer.parseInt(scanner.nextLine());

            switch (op) {
                case 1 -> personagemDAO.listarTodos().forEach(System.out::println);
                case 2 -> inserirPersonagem(personagemDAO, classeDAO);
                case 3 -> atualizarPersonagem(personagemDAO, classeDAO);
                case 4 -> {
                    System.out.print("ID do personagem para deletar: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    personagemDAO.deletar(id);
                }
                case 5 -> personagemDAO.listarPersonagensComClasse();
            }
        } while (op != 0);
    }

    private static void inserirPersonagem(PersonagemDAO personagemDAO, ClasseDAO classeDAO) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Nível: ");
        int nivel = Integer.parseInt(scanner.nextLine());
        System.out.print("HP: ");
        int hp = Integer.parseInt(scanner.nextLine());
        System.out.print("Ataque: ");
        int atk = Integer.parseInt(scanner.nextLine());
        System.out.print("Defesa: ");
        int def = Integer.parseInt(scanner.nextLine());

        System.out.println("Classes disponíveis:");
        classeDAO.listarTodos().forEach(System.out::println);

        System.out.print("ID da classe (ou 0 para nenhuma): ");
        int idClasse = Integer.parseInt(scanner.nextLine());
        Classe classe = idClasse > 0 ? classeDAO.buscarPorId(idClasse) : null;

        Personagem p = new Personagem(nome);
        p.setNivel(nivel);
        p.setHp(hp);
        p.setAtaque(atk);
        p.setDefesa(def);
        p.setClasse(classe);

        personagemDAO.inserir(p);
    }

    private static void atualizarPersonagem(PersonagemDAO personagemDAO, ClasseDAO classeDAO) {
        System.out.print("ID do personagem para atualizar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Personagem p = personagemDAO.buscarPorId(id);
        if (p == null) {
            System.out.println("Personagem não encontrado.");
            return;
        }

        // 🔥 CORREÇÃO DO NOME
        System.out.print("Novo nome (" + p.getNome() + "): ");
        String nomeNovo = scanner.nextLine();
        if (!nomeNovo.isBlank()) {
            p.setNome(nomeNovo);
        }

        // 🔥 CORREÇÃO DA CLASSE
        System.out.println("Classes disponíveis:");
        classeDAO.listarTodos().forEach(System.out::println);

        System.out.print("Novo ID da classe (0 para manter / nenhuma): ");
        String classeStr = scanner.nextLine();
        if (!classeStr.isBlank()) {
            int idClasse = Integer.parseInt(classeStr);
            if (idClasse == 0) {
                p.setClasse(null);
            } else {
                p.setClasse(classeDAO.buscarPorId(idClasse));
            }
        }

        personagemDAO.atualizar(p);
    }

    // ===============================
    // HABILIDADES
    // ===============================
    private static void menuHabilidades(HabilidadeDAO habilidadeDAO) {
        int op;
        do {
            System.out.println("\n-- Habilidades --");
            System.out.println("1 - Listar");
            System.out.println("2 - Inserir");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Deletar");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            op = Integer.parseInt(scanner.nextLine());

            switch (op) {
                case 1 -> habilidadeDAO.listarTodos().forEach(System.out::println);
                case 2 -> {
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Descrição: ");
                    String desc = scanner.nextLine();
                    System.out.print("Dano base: ");
                    int dano = Integer.parseInt(scanner.nextLine());
                    System.out.print("Elemento (FOGO, AGUA, ELETRICO, LUZ, SOMBRA, NEUTRO): ");
                    String elem = scanner.nextLine().toUpperCase();

                    Habilidade h = new Habilidade(nome, desc, 0, dano, Elemento.valueOf(elem));
                    habilidadeDAO.inserir(h);
                }
                case 3 -> {
                    System.out.print("ID da habilidade para atualizar: ");
                    int id = Integer.parseInt(scanner.nextLine());

                    Habilidade h = habilidadeDAO.buscarPorId(id);
                    if (h == null) {
                        System.out.println("Habilidade não encontrada.");
                        break;
                    }

                    System.out.print("Novo nome (" + h.getNome() + "): ");
                    String nome = scanner.nextLine();
                    if (!nome.isBlank()) {
                        h.setNome(nome);
                    }

                    habilidadeDAO.atualizar(h);
                }
                case 4 -> {
                    System.out.print("ID da habilidade para deletar: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    habilidadeDAO.deletar(id);
                }
            }
        } while (op != 0);
    }

    // ===============================
    // PERSONAGEM-HABILIDADE
    // ===============================
    private static void menuPersonagemHabilidade(PersonagemHabilidadeDAO phDAO) {
        int op;
        do {
            System.out.println("\n-- Personagem x Habilidade --");
            System.out.println("1 - Inserir relação");
            System.out.println("2 - Listar todas relações (JOIN)");
            System.out.println("3 - Listar habilidades de um personagem (JOIN)");
            System.out.println("4 - Listar personagens por habilidade (JOIN)");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            op = Integer.parseInt(scanner.nextLine());

            switch (op) {
                case 1 -> {
                    System.out.print("ID do personagem: ");
                    int idP = Integer.parseInt(scanner.nextLine());
                    System.out.print("ID da habilidade: ");
                    int idH = Integer.parseInt(scanner.nextLine());
                    phDAO.inserir(new PersonagemHabilidade(idP, idH));
                }
                case 2 -> phDAO.listarTodasRelacoes();
                case 3 -> {
                    System.out.print("ID do personagem: ");
                    int idP = Integer.parseInt(scanner.nextLine());
                    phDAO.listarHabilidadesDePersonagem(idP);
                }
                case 4 -> {
                    System.out.print("ID da habilidade: ");
                    int idH = Integer.parseInt(scanner.nextLine());
                    phDAO.listarPersonagensPorHabilidade(idH);
                }
            }
        } while (op != 0);
    }

    // ===============================
    // COMBATES
    // ===============================
    private static void menuCombates(CombateLogDAO combateLogDAO) {
        int op;
        do {
            System.out.println("\n-- Combates --");
            System.out.println("1 - Listar simples");
            System.out.println("2 - Inserir combate");
            System.out.println("3 - Listar combates com personagem (JOIN)");
            System.out.println("4 - Listar vitórias (JOIN)");
            System.out.println("5 - Listar dano total por personagem (JOIN)");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            op = Integer.parseInt(scanner.nextLine());

            switch (op) {
                case 1 -> combateLogDAO.listarTodos().forEach(c ->
                        System.out.println("ID " + c.getId() + " - Personagem " + c.getPersonagemId() +
                                " vs " + c.getMonstroNome() + " (" + c.getResultado() + ")"));

                case 2 -> {
                    System.out.print("ID do personagem: ");
                    int idP = Integer.parseInt(scanner.nextLine());
                    System.out.print("Nome do monstro: ");
                    String monstro = scanner.nextLine();
                    System.out.print("Dano causado: ");
                    int danoC = Integer.parseInt(scanner.nextLine());
                    System.out.print("Dano recebido: ");
                    int danoR = Integer.parseInt(scanner.nextLine());
                    System.out.print("Resultado (VITORIA/DERROTA): ");
                    String res = scanner.nextLine().toUpperCase();

                    combateLogDAO.inserir(new CombateLog(idP, monstro, danoC, danoR, res));
                }

                case 3 -> combateLogDAO.listarCombatesComPersonagem();
                case 4 -> combateLogDAO.listarVitorias();
                case 5 -> combateLogDAO.listarDanoTotalPorPersonagem();
            }
        } while (op != 0);
    }
}
