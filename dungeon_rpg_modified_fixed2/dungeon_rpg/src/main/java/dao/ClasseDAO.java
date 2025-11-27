package dao;

import model.Classe;
import util.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClasseDAO implements GenericDAO<Classe> {

    @Override
    public void inserir(Classe c) {
        String sql = "INSERT INTO classe (nome, descricao) VALUES (?, ?)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getDescricao());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1));
            }

            System.out.println("✅ Classe inserida com sucesso!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao inserir classe: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Classe c) {
        String sql = "UPDATE classe SET nome = ?, descricao = ? WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getDescricao());
            stmt.setInt(3, c.getId());

            stmt.executeUpdate();
            System.out.println("✅ Classe atualizada com sucesso!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao atualizar classe: " + e.getMessage());
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM classe WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Classe deletada com sucesso!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao deletar classe: " + e.getMessage());
        }
    }

    @Override
    public Classe buscarPorId(int id) {
        String sql = "SELECT * FROM classe WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Classe(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao")
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar classe: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Classe> listarTodos() {
        List<Classe> lista = new ArrayList<>();
        String sql = "SELECT * FROM classe";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Classe c = new Classe(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao")
                );
                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar classes: " + e.getMessage());
        }

        return lista;
    }
}
