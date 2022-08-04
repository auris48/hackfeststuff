package com.qa.ims.persistence.dao;

import com.qa.ims.persistence.domain.Driver;
import com.qa.ims.utils.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO implements Dao<Driver> {

	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public Driver modelFromResultSet(ResultSet resultSet) throws SQLException {
		Long id = resultSet.getLong("id");
		String firstName = resultSet.getString("first_name");
		String surname = resultSet.getString("surname");
		return new Driver(id, firstName, surname);
	}

	/**
	 * Reads all Drivers from the database
	 * 
	 * @return A list of Drivers
	 */
	@Override
	public List<Driver> readAll() {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM drivers");) {
			List<Driver> drivers = new ArrayList<>();
			while (resultSet.next()) {
				drivers.add(modelFromResultSet(resultSet));
			}
			return drivers;
		} catch (SQLException e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return new ArrayList<>();
	}

	public Driver readLatest() {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM Drivers ORDER BY id DESC LIMIT 1");) {
			resultSet.next();
			return modelFromResultSet(resultSet);
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Creates a Driver in the database
	 * 
	 * @param Driver - takes in a Driver object. id will be ignored
	 */
	@Override
	public Driver create(Driver Driver) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				PreparedStatement statement = connection
						.prepareStatement("INSERT INTO Drivers(first_name, surname) VALUES (?, ?)");) {
			statement.setString(1, Driver.getFirstName());
			statement.setString(2, Driver.getSurname());
			statement.executeUpdate();
			return readLatest();
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Driver read(Long id) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM Drivers WHERE id = ?");) {
			statement.setLong(1, id);
			try (ResultSet resultSet = statement.executeQuery();) {
				resultSet.next();
				return modelFromResultSet(resultSet);
			}
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Updates a Driver in the database
	 * 
	 * @param Driver - takes in a Driver object, the id field will be used to
	 *                 update that Driver in the database
	 * @return
	 */
	@Override
	public Driver update(Driver Driver) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				PreparedStatement statement = connection
						.prepareStatement("UPDATE Drivers SET first_name = ?, surname = ? WHERE id = ?");) {
			statement.setString(1, Driver.getFirstName());
			statement.setString(2, Driver.getSurname());
			statement.setLong(3, Driver.getId());
			statement.executeUpdate();
			return read(Driver.getId());
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Deletes a Driver in the database
	 * 
	 * @param id - id of the Driver
	 */
	@Override
	public int delete(long id) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement("DELETE FROM Drivers WHERE id = ?");) {
			statement.setLong(1, id);
			return statement.executeUpdate();
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return 0;
	}

}
