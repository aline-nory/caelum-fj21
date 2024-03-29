package br.com.caelum.jdbc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.caelum.jdbc.ConnectionFactory;
import br.com.caelum.jdbc.modelo.Contato;

public class FuncionarioDAO {
private Connection connection;
	
	public FuncionarioDAO() {
		this.connection = new ConnectionFactory().getConnection();
	}
	
	public void adiciona(Contato contato) {
		String sql = "insert into contatos " + "(nome, email, endereco, dataNascimento)" + 
					" values (?,?,?,?)";
		
		try {
			//prepared statement para a inser��o
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			//seta valores
			stmt.setString(1, contato.getNome());
			stmt.setString(2, contato.getEmail());
			stmt.setString(3, contato.getEndereco());
			stmt.setDate(4, new Date(contato.getDataNascimento().getTimeInMillis()));
			
			//executa
			stmt.execute();
			stmt.close();
		}catch(SQLException e) {
			throw new DAOException();
		}
			
	}
	
	public List<Contato> getLista(){
		try {
			List<Contato> contatos = new ArrayList<Contato>();
			PreparedStatement stmt = this.connection.prepareStatement("select * from contatos"); // this.connection.prepareStatement("select * from contatos WHERE nome like 'c')
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				//criando o obj contato
				Contato contato = new Contato();
				contato.setId(rs.getLong("id"));
				contato.setNome(rs.getString("nome"));
				contato.setEmail(rs.getString("email"));
				contato.setEndereco(rs.getString("endereco"));
				
				//montando a data atrav�s do Calendar	
//				Calendar data = Calendar.getInstance();
//				data.setTime(rs.getDate("dataNascimento"));			
//				contato.setDataNascimento(data);
						
				Calendar data = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				data.setTime(rs.getDate("dataNascimento"));	
				String s = sdf.format(data.getTime());
				
				
				//adicionando contato a lista
				contatos.add(contato);
			}
				rs.close();
				stmt.close();
				return contatos;
				
			} catch (SQLException e) {
				throw new DAOException();
			}
		}

	public void altera(Contato contato) {
		String sql = "update contatos set nome=?, email=?, endereco=?, dataNascimento=? where id=?";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, contato.getNome());
			stmt.setString(2, contato.getEmail());
			stmt.setString(3, contato.getEndereco());
			stmt.setDate(4, new Date (contato.getDataNascimento().getTimeInMillis()));
			stmt.setLong(5, contato.getId());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new DAOException();
		}
	}
	
	public void remove(Contato contato) {	
		try {
			PreparedStatement stmt = connection.prepareStatement("delete from contatos where id=?");
			stmt.setLong(1, contato.getId());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new DAOException();
		}
	}
}
