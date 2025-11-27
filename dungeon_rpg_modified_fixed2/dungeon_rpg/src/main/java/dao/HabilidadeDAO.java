package dao;

import model.Elemento;
import model.Habilidade;
import util.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabilidadeDAO implements GenericDAO<Habilidade> {

    @Override
    public void inserir(Habilidade h) {
        String sql = "INSERT INTO habilidade (nome, descricao, dano_base, elemento) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, h.getNome());
            stmt.setString(2, h.getDescricao());
            stmt.setInt(3, h.getDanoBase());
            stmt.setString(4, h.getElemento().name());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                h.setId(rs.getInt(1));
            }

            System.out.println("✅ Habilidade inserida com sucesso!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao inserir habilidade: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Habilidade h) {
        String sql = "UPDATE habilidade SET nome=?, descricao=?, dano_base=?, elemento=? WHERE id=?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, h.getNome());
            stmt.setString(2, h.getDescricao());
            stmt.setInt(3, h.getDanoBase());
            stmt.setString(4, h.getElemento().name());
            stmt.setInt(5, h.getId());

            stmt.executeUpdate();
            System.out.println("✅ Habilidade atualizada com sucesso!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao atualizar habilidade: " + e.getMessage());
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM habilidade WHERE id=?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Habilidade deletada com sucesso!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao deletar habilidade: " + e.getMessage());
        }
    }

    @Override
    public Habilidade buscarPorId(int id) {
        String sql = "SELECT * FROM habilidade WHERE id=?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Habilidade h = new Habilidade(
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        0,
                        rs.getInt("dano_base"),
                        Elemento.valueOf(rs.getString("elemento"))
                );
                h.setId(rs.getInt("id"));
                return h;
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar habilidade: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Habilidade> listarTodos() {
        List<Habilidade> lista = new ArrayList<>();
        String sql = "SELECT * FROM habilidade";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Habilidade h = new Habilidade(
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        0,
                        rs.getInt("dano_base"),
                        Elemento.valueOf(rs.getString("elemento"))
                );
                h.setId(rs.getInt("id"));
                lista.add(h);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar habilidades: " + e.getMessage());
        }

        return lista;
    }
}
