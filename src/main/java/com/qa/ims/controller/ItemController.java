package com.qa.ims.controller;

import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

/**
 * Takes in Item details for CRUD functionality
 *
 */
public class ItemController implements CrudController<Item> {

	public static final Logger LOGGER = LogManager.getLogger();

	private ItemDAO itemDao;
	private Utils utils;

	public ItemController(ItemDAO itemDao, Utils utils) {
		super();
		this.itemDao = itemDao;
		this.utils = utils;
	}

	@Override
	public List<Item> readAll() {
		return null;
	}

	@Override
	public Item create() {
		return null;
	}

	@Override
	public Item update() {
		return null;
	}

	@Override
	public int delete() {
		return 0;
	}
}
