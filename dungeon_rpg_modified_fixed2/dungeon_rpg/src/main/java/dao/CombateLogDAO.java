package dao;

import model.CombateLog;
import util.DBconnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CombateLogDAO implements GenericDAO<CombateLog> {

    @Override
    public void inserir(CombateLog c) {
        String sql = """
                INSERT INTO combate_log (personagem_id, monstro_nome, dano_causado, dano_recebido, resultado)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, c.getPersonagemId());
            stmt.setString(2, c.getMonstroNome());
            stmt.setInt(3, c.getDanoCausado());
            stmt.setInt(4, c.getDanoRecebido());
            stmt.setString(5, c.getResultado());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1));
            }

            System.out.println("✅ Combate registrado!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao inserir combate: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(CombateLog c) {
        String sql = """
                UPDATE combate_log
                SET personagem_id=?, monstro_nome=?, dano_causado=?, dano_recebido=?, resultado=?
                WHERE id=?
                """;

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, c.getPersonagemId());
            stmt.setString(2, c.getMonstroNome());
            stmt.setInt(3, c.getDanoCausado());
            stmt.setInt(4, c.getDanoRecebido());
            stmt.setString(5, c.getResultado());
            stmt.setInt(6, c.getId());

            stmt.executeUpdate();
            System.out.println("✅ Combate atualizado!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao atualizar combate: " + e.getMessage());
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM combate_log WHERE id=?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Combate deletado!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao deletar combate: " + e.getMessage());
        }
    }

    @Override
    public CombateLog buscarPorId(int id) {
        String sql = "SELECT * FROM combate_log WHERE id=?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return montarCombate(rs);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar combate: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<CombateLog> listarTodos() {
        List<CombateLog> lista = new ArrayList<>();
        String sql = "SELECT * FROM combate_log";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montarCombate(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar combates: " + e.getMessage());
        }

        return lista;
    }

    private CombateLog montarCombate(ResultSet rs) throws SQLException {
        CombateLog c = new CombateLog();
        c.setId(rs.getInt("id"));
        c.setPersonagemId(rs.getInt("personagem_id"));
        c.setMonstroNome(rs.getString("monstro_nome"));
        c.setDanoCausado(rs.getInt("dano_causado"));
        c.setDanoRecebido(rs.getInt("dano_recebido"));
        c.setResultado(rs.getString("resultado"));

        Timestamp ts = rs.getTimestamp("data_hora");
        if (ts != null) {
            c.setDataHora(ts.toLocalDateTime());
        }

        return c;
    }

    // JOIN sem intermediária 1
    public void listarCombatesComPersonagem() {
        String sql = """
                SELECT cl.*, p.nome AS personagem
                FROM combate_log cl
                JOIN personagem p ON cl.personagem_id = p.id
                """;

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.printf("[%s] Personagem: %s x Monstro: %s | Dano Causado: %d | Dano Recebido: %d | Resultado: %s%n",
                        rs.getTimestamp("data_hora"),
                        rs.getString("personagem"),
                        rs.getString("monstro_nome"),
                        rs.getInt("dano_causado"),
                        rs.getInt("dano_recebido"),
                        rs.getString("resultado"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro no JOIN combates+personagem: " + e.getMessage());
        }
    }

    // JOIN sem intermediária 2
    public void listarVitorias() {
        String sql = """
                SELECT cl.*, p.nome AS personagem
                FROM combate_log cl
                JOIN personagem p ON cl.personagem_id = p.id
                WHERE cl.resultado = 'VITORIA'
                """;

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.printf("VITÓRIA - %s derrotou %s (%d de dano)%n",
                        rs.getString("personagem"),
                        rs.getString("monstro_nome"),
                        rs.getInt("dano_causado"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro no JOIN vitórias: " + e.getMessage());
        }
    }

    // JOIN sem intermediária 3
    public void listarDanoTotalPorPersonagem() {
        String sql = """
                SELECT p.nome AS personagem, SUM(cl.dano_causado) AS dano_total
                FROM combate_log cl
                JOIN personagem p ON cl.personagem_id = p.id
                GROUP BY p.id, p.nome
                ORDER BY dano_total DESC
                """;

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.printf("Personagem: %s | Dano total causado: %d%n",
                        rs.getString("personagem"),
                        rs.getInt("dano_total"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro no JOIN dano total: " + e.getMessage());
        }
    }
}
