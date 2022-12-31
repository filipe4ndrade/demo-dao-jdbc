package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.ConnectionFactory;
import db.DbException;
import model.dao.DepartmentDAO;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDAO {

	private Connection conn;

	public DepartmentDaoJDBC() {
		this.conn = ConnectionFactory.getConnection();
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		String sql = "INSERT INTO public.department " + "(name) " + "VALUES (?);";
		try {

			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				rs.close();
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		String sql = "UPDATE public.department " + "SET name=? " + "WHERE id=?;";
		try {

			st = conn.prepareStatement(sql);

			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		String sql = "DELETE FROM public.department WHERE id = ?;";

		try {
			st = conn.prepareStatement(sql);
			
			st.setInt(1,id);
			
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM department WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		finally {
			try {
				rs.close();
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM department ORDER BY Name");
			rs = st.executeQuery();

			List<Department> list = new ArrayList<>();

			while (rs.next()) {
				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				list.add(obj);
			}
			return list;
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		finally {
			try {
				rs.close();
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

}
