package com.qa.ims;

import com.qa.ims.controller.*;
import com.qa.ims.persistence.dao.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.ims.persistence.domain.Domain;
import com.qa.ims.utils.DBUtils;
import com.qa.ims.utils.Utils;

import java.util.List;

public class IMS {

	public static final Logger LOGGER = LogManager.getLogger();
	private final DeliveryController delivery;
	private final CustomerController customers;
	private final ItemController items;
	private final OrderController orders;
	private final DriverController drivers;

	private final Utils utils;
	private List<User> users;

	public IMS() {
		this.utils = new Utils();
		final DeliveryDAO deliveryDAO = new DeliveryDAO();
		final CustomerDAO custDAO = new CustomerDAO();
		final ItemDAO itemDAO = new ItemDAO();
		final OrderDAO orderDAO = new OrderDAO();
		final DriverDAO driverDAO = new DriverDAO();
		this.customers = new CustomerController(custDAO, utils);
		this.items = new ItemController(itemDAO, utils);
		this.orders = new OrderController(orderDAO, utils);
		this.drivers = new DriverController(driverDAO, utils);
		this.delivery = new DeliveryController(orderDAO, deliveryDAO, new DriverDAO(), utils);
	}

	public void imsSystem() {
		LOGGER.info("Welcome to the Inventory Management System!");
		DBUtils.connect();

		Domain domain = null;
		do {
			LOGGER.info("Which entity would you like to use?");
			Domain.printDomains();

			domain = Domain.getDomain(utils);

			domainAction(domain);

		} while (domain != Domain.STOP);
	}

	private void domainAction(Domain domain) {
		boolean changeDomain = false;
		do {

			CrudController<?> active = null;
			switch (domain) {
			case CUSTOMER:
				active = this.customers;
				break;
			case ITEM:
				active = this.items;
				break;
			case ORDER:
				active = this.orders;
				break;
			case DRIVER:
				active = this.drivers;
				break;
			case DELIVERY:
				active = this.delivery;
				break;
			case STOP:
				return;
			default:
				break;
			}
			LOGGER.info(() ->"What would you like to do with " + domain.name().toLowerCase() + ":");
			Action.printActions();
			Action action = Action.getAction(utils);

			if (action == Action.RETURN) {
				changeDomain = true;
			} else {
				doAction(active, action);
			}
		} while (!changeDomain);
	}

	public void doAction(CrudController<?> crudController, Action action) {
		switch (action) {
		case CREATE:
			crudController.create();
			break;
		case READ:
			crudController.readAll();
			break;
		case UPDATE:
			crudController.update();
			break;
		case DELETE:
			crudController.delete();
			break;
		case RETURN:
			break;
		default:
			break;
		}
	}

}
