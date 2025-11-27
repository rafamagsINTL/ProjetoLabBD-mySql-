package dao;

import model.PersonagemHabilidade;
import util.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonagemHabilidadeDAO implements GenericDAO<PersonagemHabilidade> {

    @Override
    public void inserir(PersonagemHabilidade ph) {
        String sql = "INSERT INTO personagem_habilidade (personagem_id, habilidade_id) VALUES (?, ?)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, ph.getPersonagemId());
            stmt.setInt(2, ph.getHabilidadeId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                ph.setId(rs.getInt(1));
            }

            System.out.println("✅ Relação personagem-habilidade inserida!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao inserir relação personagem-habilidade: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(PersonagemHabilidade ph) {
        String sql = "UPDATE personagem_habilidade SET personagem_id=?, habilidade_id=? WHERE id=?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ph.getPersonagemId());
            stmt.setInt(2, ph.getHabilidadeId());
            stmt.setInt(3, ph.getId());

            stmt.executeUpdate();
            System.out.println("✅ Relação personagem-habilidade atualizada!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao atualizar relação personagem-habilidade: " + e.getMessage());
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM personagem_habilidade WHERE id=?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Relação personagem-habilidade deletada!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao deletar relação personagem-habilidade: " + e.getMessage());
        }
    }

    @Override
    public PersonagemHabilidade buscarPorId(int id) {
        String sql = "SELECT * FROM personagem_habilidade WHERE id=?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new PersonagemHabilidade(
                        rs.getInt("id"),
                        rs.getInt("personagem_id"),
                        rs.getInt("habilidade_id")
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar relação personagem-habilidade: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<PersonagemHabilidade> listarTodos() {
        List<PersonagemHabilidade> lista = new ArrayList<>();
        String sql = "SELECT * FROM personagem_habilidade";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new PersonagemHabilidade(
                        rs.getInt("id"),
                        rs.getInt("personagem_id"),
                        rs.getInt("habilidade_id")
                ));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar relações personagem-habilidade: " + e.getMessage());
        }

        return lista;
    }

    // JOIN com tabela intermediária 1
    public void listarHabilidadesDePersonagem(int personagemId) {
        String sql = """
                SELECT p.nome AS personagem, h.nome AS habilidade, h.elemento
                FROM personagem p
                JOIN personagem_habilidade ph ON ph.personagem_id = p.id
                JOIN habilidade h ON h.id = ph.habilidade_id
                WHERE p.id = ?
                """;

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personagemId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.printf("Personagem: %s | Habilidade: %s (%s)%n",
                        rs.getString("personagem"),
                        rs.getString("habilidade"),
                        rs.getString("elemento"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro no JOIN (habilidades de personagem): " + e.getMessage());
        }
    }

    // JOIN com tabela intermediária 2
    public void listarPersonagensPorHabilidade(int habilidadeId) {
        String sql = """
                SELECT h.nome AS habilidade, p.nome AS personagem
                FROM habilidade h
                JOIN personagem_habilidade ph ON ph.habilidade_id = h.id
                JOIN personagem p ON p.id = ph.personagem_id
                WHERE h.id = ?
                """;

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, habilidadeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.printf("Habilidade: %s | Personagem: %s%n",
                        rs.getString("habilidade"),
                        rs.getString("personagem"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro no JOIN (personagens por habilidade): " + e.getMessage());
        }
    }

    // JOIN com tabela intermediária 3
    public void listarTodasRelacoes() {
        String sql = """
                SELECT p.nome AS personagem, h.nome AS habilidade
                FROM personagem p
                JOIN personagem_habilidade ph ON ph.personagem_id = p.id
                JOIN habilidade h ON h.id = ph.habilidade_id
                ORDER BY p.nome
                """;

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.printf("Personagem: %s | Habilidade: %s%n",
                        rs.getString("personagem"),
                        rs.getString("habilidade"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro no JOIN (todas as relações): " + e.getMessage());
        }
    }
}
