package dao;

import model.Classe;
import model.Personagem;
import util.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonagemDAO implements GenericDAO<Personagem> {

    // Método antigo mantido para compatibilidade com o jogo
    public void salvar(Personagem p) {
        inserir(p);
    }

    @Override
    public void inserir(Personagem p) {
        String sql = "INSERT INTO personagem (nome, nivel, hp, ataque, defesa, classe_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getNome());
            stmt.setInt(2, p.getNivel());
            stmt.setInt(3, p.getHp());
            stmt.setInt(4, p.getAtaque());
            stmt.setInt(5, p.getDefesa());

            if (p.getClasse() != null) {
                stmt.setInt(6, p.getClasse().getId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                p.setId(rs.getInt(1));
            }

            System.out.println("✅ Personagem inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao inserir personagem: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Personagem p) {
        String sql = "UPDATE personagem SET nome=?, nivel=?, hp=?, ataque=?, defesa=?, classe_id=? WHERE id=?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setInt(2, p.getNivel());
            stmt.setInt(3, p.getHp());
            stmt.setInt(4, p.getAtaque());
            stmt.setInt(5, p.getDefesa());

            if (p.getClasse() != null) {
                stmt.setInt(6, p.getClasse().getId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setInt(7, p.getId());

            stmt.executeUpdate();
            System.out.println("✅ Personagem atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao atualizar personagem: " + e.getMessage());
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM personagem WHERE id=?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Personagem deletado com sucesso!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao deletar personagem: " + e.getMessage());
        }
    }

    @Override
    public Personagem buscarPorId(int id) {
        String sql = "SELECT p.*, c.id AS c_id, c.nome AS c_nome, c.descricao AS c_desc " +
                     "FROM personagem p " +
                     "LEFT JOIN classe c ON p.classe_id = c.id " +
                     "WHERE p.id=?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return montarPersonagem(rs);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar personagem: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Personagem> listarTodos() {
        List<Personagem> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.id AS c_id, c.nome AS c_nome, c.descricao AS c_desc " +
                     "FROM personagem p " +
                     "LEFT JOIN classe c ON p.classe_id = c.id";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montarPersonagem(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar personagens: " + e.getMessage());
        }

        return lista;
    }

    // JOIN sem tabela intermediária: personagem + classe
    public void listarPersonagensComClasse() {
        String sql = "SELECT p.nome AS personagem, c.nome AS classe " +
                     "FROM personagem p " +
                     "LEFT JOIN classe c ON p.classe_id = c.id";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.printf("Personagem: %s | Classe: %s%n",
                        rs.getString("personagem"),
                        rs.getString("classe"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro no SELECT com JOIN: " + e.getMessage());
        }
    }

    private Personagem montarPersonagem(ResultSet rs) throws SQLException {

        // Constrói o personagem somente com o nome
        Personagem p = new Personagem(rs.getString("nome"));

        // Sobrescreve atributos vindos do banco (sem lógica de combate)
        p.setId(rs.getInt("id"));
        p.setNivel(rs.getInt("nivel"));
        p.setHp(rs.getInt("hp"));
        p.setAtaque(rs.getInt("ataque"));
        p.setDefesa(rs.getInt("defesa"));

        // Classe do personagem
        int classeId = rs.getInt("classe_id");
        if (!rs.wasNull()) {
            Classe c = new Classe(
                    classeId,
                    rs.getString("c_nome"),
                    rs.getString("c_desc")
            );
            p.setClasse(c);
        }

        return p;
    }

    public boolean testarConexao() {
        try (Connection conn = DBconnection.getConnection()) {
            System.out.println("✅ Conexão com banco de dados estabelecida!");
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erro de conexão com banco: " + e.getMessage());
            return false;
        }
    }
}
