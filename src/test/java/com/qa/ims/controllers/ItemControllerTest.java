package com.qa.ims.controllers;
import com.qa.ims.controller.ItemController;
import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.utils.Utils;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

	@Mock
	private Utils utils;

	@Mock
	private ItemDAO dao;

	@InjectMocks
	private ItemController controller;

	@Test
	public void testCreate() {
		final String ITEM_NAME = "Coca-Cola", DESCRIPTION="Cold drink";
		final LocalDate STOCK_DATE = LocalDate.now();
		final int ITEM_STOCK = 100;
		final Double ITEM_PRICE = 0.95;
		Mockito.when(utils.getString()).thenReturn(ITEM_NAME, DESCRIPTION, STOCK_DATE.toString());
		Mockito.when(utils.getDouble()).thenReturn(ITEM_PRICE);
		Mockito.when(utils.getInt()).thenReturn(ITEM_STOCK);
		final Item created = new Item(ITEM_NAME, STOCK_DATE, DESCRIPTION, ITEM_STOCK, ITEM_PRICE);
		Mockito.when(dao.create(created)).thenReturn(created);
		assertEquals(created, controller.create());
		Mockito.verify(utils, Mockito.times(2)).getString();
		Mockito.verify(dao, Mockito.times(1)).create(created);
	}

	@Test
	public void testReadAll() {
		List<Item> items = new ArrayList<>();
		items.add(new Item(1L, "Coca-Cola", LocalDate.now(), "Cold drink", 100, 0.95));
		Mockito.when(dao.readAll()).thenReturn(items);
		assertEquals(items, controller.readAll());
		Mockito.verify(dao, Mockito.times(1)).readAll();

	}
/*

	@Test
	public void testUpdate() {

		Item updated = new Item(1L, "Coca-Cola", LocalDate.now(), "Cold drink", 100, 0.95);

		Mockito.when(this.utils.getLong()).thenReturn(1L);
		Mockito.when(this.utils.getString()).thenReturn(updated.getItemName());
		Mockito.when(this.utils.getLocalDate()).thenReturn(updated.getItemStockDate());
		Mockito.when(this.utils.getString()).thenReturn(updated.getItemDescription());
		Mockito.when(this.utils.getInt()).thenReturn(updated.getItemStock());
		Mockito.when(this.utils.getDouble()).thenReturn(updated.getItemPrice());
		Mockito.when(this.dao.update(updated)).thenReturn(updated);
		assertEquals(updated, this.controller.update());
		Mockito.verify(this.utils, Mockito.times(1)).getLong();
		Mockito.verify(this.utils, Mockito.times(2)).getString();
		Mockito.verify(this.utils, Mockito.times(1)).getInt();
		Mockito.verify(this.utils, Mockito.times(1)).getLocalDate();
		Mockito.verify(this.utils, Mockito.times(1)).getDouble();
		Mockito.verify(this.dao, Mockito.times(1)).update(updated);
	}
*/

	@Test
	public void testDelete() {
		final long ID = 1L;
		Mockito.when(utils.getLong()).thenReturn(ID);
		Mockito.when(dao.delete(ID)).thenReturn(1);
		assertEquals(1L, this.controller.delete());
		Mockito.verify(utils, Mockito.times(1)).getLong();
		Mockito.verify(dao, Mockito.times(1)).delete(ID);
	}

}
