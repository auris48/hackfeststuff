package com.qa.ims.persistence.dao;

import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.persistence.domain.OrderDetail;
import com.qa.ims.utils.DBUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDAOTest {

    private final OrderDAO ORDERDAO = new OrderDAO();
    private final ItemDAO ITEMDAO = new ItemDAO();

    @BeforeEach
    public void setup() {
        DBUtils.connect();
        DBUtils.getInstance().init("src/test/resources/sql-schema.sql", "src/test/resources/sql-data.sql");
    }

    @Test
    public void testReadAll() {
        List<Order> expected = new ArrayList<>();
        expected.add(new Order(1L, 1L, LocalDate.of(2022, 07, 28), LocalDate.of(2022, 07, 28)));
        expected.get(0).addToOrderDetailList(new OrderDetail(1L, new Item(
                1L, "Toy car", LocalDate.of(
                        2005, 04, 23)
                , "Ferrari replica car", 100, 25.95), 10));
        assertEquals(expected, ORDERDAO.readAll());
    }

    @Test
    public void testRead() {
        final long ID = 1L;
        Order expected = (new Order(1L, 1L, LocalDate.of(2022, 07, 28), LocalDate.of(2022, 07, 28)));
        expected.addToOrderDetailList(new OrderDetail(1L, new Item(
                1L, "Toy car", LocalDate.of(
                2005, 04, 23)
                , "Ferrari replica car", 100, 25.95), 10));

        assertEquals(expected, ORDERDAO.read(ID));
    }


    @Test
    public void testCreate() {
        final Order created = new Order(2L, 1L, LocalDate.of(2022, 07, 28),
                LocalDate.of(2022, 07, 28));
        assertEquals(created, ORDERDAO.create(created));
    }


    @Test
    public void testDelete() {
        assertEquals(1, ORDERDAO.delete(1));
    }

    @Test
    void createOrderDetail() {
        Order order = ORDERDAO.readLatest();
        order.addToOrderDetailList(ORDERDAO.readOrderDetails(order));
        Item item = new Item(1L, "Pringles",  LocalDate.now(), "crisps", 100, 2.95);
        order.addToOrderDetailList(new OrderDetail(2L, ITEMDAO.create(item), 10));
        Order updatedOrder = ORDERDAO.readLatest();
        ORDERDAO.createOrderDetail(order);
        updatedOrder.addToOrderDetailList(ORDERDAO.readOrderDetails(order));
        assertEquals(order,updatedOrder);
    }


    @Test
    void update() {
        Order expected = ORDERDAO.readLatest();
        expected.setOrderDueDate(LocalDate.of(2001, 01, 01));
        Order actual = ORDERDAO.update(expected);
        actual.setOrderDetailList(new ArrayList<>());
        assertEquals(expected, actual);
    }

    @Test
    void updateOrderItemsQuantity() {
        Order order = ORDERDAO.readLatest();
        order.setOrderDetailList(ORDERDAO.readOrderDetails(order));
        order.getOrderDetailList().get(0).setQuantity(5);
        assertEquals(order, ORDERDAO.updateOrderItemsQuantity(order, 1, 5));
    }


    @Test
    public void testReadLatest() {
        assertEquals(new Order(1L, 1L, LocalDate.of(2022, 07, 28),
                        LocalDate.of(2022, 07, 28)),
                ORDERDAO.readLatest());
    }
}