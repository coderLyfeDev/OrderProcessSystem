

--Queries that run on startup to populate items and their inventory in to an H2 database.
--Database Accessible via the browser using http://localhost:8080/h2-console/login.jsp
insert into Item(price, sku) values(4.0, 1234);
insert into Item(price, sku) values(7.0,5476);
insert into Item(price, sku) values(2.50, 9532);
insert into Item(price, sku) values(5.0, 7332);
insert into INVENTORY(qty, item_id) values(10, 1);
insert into INVENTORY(qty, item_id) values(6, 2);
insert into INVENTORY(qty, item_id) values(11, 3);
insert into INVENTORY(qty, item_id) values(20, 4);
