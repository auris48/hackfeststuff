INSERT INTO `customers` (`first_name`, `surname`) VALUES ('jordan', 'harrison');
INSERT INTO `items`(id, item_name, item_stockdate, item_description, item_stock, item_price) VALUES  ('1', 'Toy car', '2005-04-23', 'Ferrari replica car', '100', 25.95);
INSERT INTO `orders` (id, customer_id, order_date, order_dueDate, order_cost) VALUES ('1','1','2022-07-28','2022-07-28', 0);
INSERT INTO `order_items` (id, order_id, item_id, item_quantity) VALUES (1, 1, 1, 10);
