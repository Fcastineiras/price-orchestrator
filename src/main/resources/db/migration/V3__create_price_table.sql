CREATE TABLE IF NOT EXISTS public.price
(
  brand_id INT NOT NULL,
  priority INT NOT NULL,
  price NUMERIC(10,2) NOT NULL,
  curr varchar(3) NOT NULL,
  price_list BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP NOT NULL,
  CONSTRAINT price_brand_id_fk FOREIGN KEY (brand_id) REFERENCES brand (id)
);