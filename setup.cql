CREATE KEYSPACE product
WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TYPE product.price (
   currencyCode text,
   amount double
);

CREATE TABLE product.product (
  productId text PRIMARY KEY,
  currentPrice frozen<price>
);
