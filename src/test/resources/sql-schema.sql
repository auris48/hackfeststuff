DROP TABLE IF EXISTS `order_items`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `items`;
DROP TABLE IF EXISTS `customers`;

create database ims;
use ims;

CREATE TABLE IF NOT EXISTS `customers` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(40) DEFAULT NULL,
    `surname` VARCHAR(40) DEFAULT NULL,
    `address` VARCHAR(40) DEFAULT NULL,
    `postcode` VARCHAR(10) DEFAULT NULL,
    `phone` INT(10) DEFAULT NULL,
    PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `orders` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `customer_id` INT NOT NULL,
    `order_date` DATE,
    `order_dueDate` DATE,
    `order_cost` DOUBLE,
    PRIMARY KEY (`id`),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
    ON DELETE CASCADE);



CREATE TABLE IF NOT EXISTS `items` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `item_name` VARCHAR(50),
    `item_stockdate` DATE,
    `item_description` VARCHAR(50),
    `item_stock` INT,
    `item_price` DOUBLE,
    PRIMARY KEY (`id`)
);



CREATE TABLE IF NOT EXISTS `order_items` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `order_id` INT(11) NOT NULL,
    `item_id` INT(11) NOT NULL,
    `item_quantity` DOUBLE,
    PRIMARY KEY (`id`),
    FOREIGN KEY (order_id) REFERENCES orders(id)
    ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `drivers`(
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(40) DEFAULT NULL,
    `surname` VARCHAR(40) DEFAULT NULL,
    PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `delivery_orders`(
	`id` INT(11) NOT NULL AUTO_INCREMENT,
    `delivery_id` INT(11) NOT NULL,
    `order_id` INT(11) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (delivery_id) REFERENCES delivery(id)
    );


CREATE TABLE IF NOT EXISTS `delivery`(
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `driver_id` INT(11) DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`driver_id`) REFERENCES drivers(id)
);


SELECT * from drivers;

SELECT * from delivery_orders;
SELECT * FROM delivery_orders WHERE order_id =0 AND delivery_id=0;