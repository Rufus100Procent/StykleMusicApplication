-- Connect to the 'stykledb' database
\c stykledb;

-- Drop existing tables
DROP TABLE IF EXISTS cart_items CASCADE;
DROP TABLE IF EXISTS sizes CASCADE;
DROP TABLE IF EXISTS images CASCADE;
DROP TABLE IF EXISTS variants CASCADE;
DROP TABLE IF EXISTS products CASCADE;

-- Create 'products' table
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    category TEXT NOT NULL,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    preview_image TEXT
);

-- Create 'variants' table
CREATE TABLE IF NOT EXISTS variants (
    id BIGSERIAL PRIMARY KEY,
    color_name TEXT NOT NULL,
    product_id INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);

-- Create 'images' table
CREATE TABLE IF NOT EXISTS images (
    id BIGSERIAL PRIMARY KEY,
    url TEXT NOT NULL,
    variant_id INT,
    FOREIGN KEY (variant_id) REFERENCES variants (id) ON DELETE CASCADE
);

-- Create 'sizes' table
CREATE TABLE IF NOT EXISTS sizes (
    id BIGSERIAL PRIMARY KEY,
    size TEXT NOT NULL,
    stock INT NOT NULL,
    variant_id INT,
    FOREIGN KEY (variant_id) REFERENCES variants (id) ON DELETE CASCADE
);

-- Create 'cart_items' table
CREATE TABLE IF NOT EXISTS cart_items (
    id BIGSERIAL PRIMARY KEY,
    product_id INT NOT NULL,
    title TEXT NOT NULL,
    color TEXT NOT NULL,
    size TEXT NOT NULL,
    quantity INT NOT NULL,
    preview_image TEXT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);

-- Insert data into 'products' table
INSERT INTO products (category, title, description, preview_image) VALUES ('hats', 'Some Hat 1', 'This is some hat.', 'images/hats/01.jpg');
INSERT INTO products (category, title, description, preview_image) VALUES ('hats', 'Some Hat 2', 'This is some hat 2.', 'images/hats/02.jpg');
INSERT INTO products (category, title, description, preview_image) VALUES ('tshirts', 'Some T-shirt 1', 'This is some t-shirt.', 'images/tshirts/black-01.jpg');
INSERT INTO products (category, title, description, preview_image) VALUES ('jackets', 'Some Jacket 1', 'This is some jacket.', 'images/jackets/01.jpg');
INSERT INTO products (category, title, description, preview_image) VALUES ('bags', 'Some Bag 1', 'This is some bag.', 'images/bags/01/01.jpg');
INSERT INTO products (category, title, description, preview_image) VALUES ('bags', 'Some Bag 2', 'This is some bag 2.', 'images/bags/02/01.jpg');

-- Insert data into 'variants' table
INSERT INTO variants (color_name, product_id) VALUES ('Black', 1); -- hat 1
INSERT INTO variants (color_name, product_id) VALUES ('White', 1); -- hat 1
INSERT INTO variants (color_name, product_id) VALUES ('Blue', 2); -- hat 2
INSERT INTO variants (color_name, product_id) VALUES ('Black', 3); -- tshirt 1
INSERT INTO variants (color_name, product_id) VALUES ('Blue', 3); -- tshirt 1
INSERT INTO variants (color_name, product_id) VALUES ('White', 3); -- tshirt 1
INSERT INTO variants (color_name, product_id) VALUES ('Black', 4); -- jacket 1
INSERT INTO variants (color_name, product_id) VALUES ('White', 4); -- jacket 1
INSERT INTO variants (color_name, product_id) VALUES ('Brown', 4); -- jacket 1
INSERT INTO variants (color_name, product_id) VALUES ('Black', 5); -- bag 1
INSERT INTO variants (color_name, product_id) VALUES ('Brown', 5); -- bag 1
INSERT INTO variants (color_name, product_id) VALUES ('Black', 6); -- bag 2

-- Insert data into 'images' table
INSERT INTO images (url, variant_id) VALUES ('images/hats/01-black.jpg', 1); -- hat 1 - black
INSERT INTO images (url, variant_id) VALUES ('images/hats/01-white.jpg', 2); -- hat 1 - white
INSERT INTO images (url, variant_id) VALUES ('images/hats/02-blue.jpg', 3); -- hat 2 - blue
INSERT INTO images (url, variant_id) VALUES ('images/tshirts/black-01.jpg', 4); -- tshirt 1 - black
INSERT INTO images (url, variant_id) VALUES ('images/tshirts/blue-01.jpg', 5); -- tshirt 1 - blue
INSERT INTO images (url, variant_id) VALUES ('images/tshirts/white-01.jpg', 6); -- tshirt 1 - white
INSERT INTO images (url, variant_id) VALUES ('images/jackets/01-black.jpg', 7); -- jacket 1 - black
INSERT INTO images (url, variant_id) VALUES ('images/jackets/01-white.jpg', 8); -- jacket 1 - white
INSERT INTO images (url, variant_id) VALUES ('images/jackets/01-brown.jpg', 9); -- jacket 1 - brown
INSERT INTO images (url, variant_id) VALUES ('images/bags/01/01-black.jpg', 10); -- bag 1 - black
INSERT INTO images (url, variant_id) VALUES ('images/bags/01/01-brown.jpg', 11); -- bag 1 - brown
INSERT INTO images (url, variant_id) VALUES ('images/bags/02/01-black.jpg', 12); -- bag 2 - black

-- Insert data into 'sizes' table
INSERT INTO sizes (size, stock, variant_id) VALUES ('S', 10, 1); -- hat 1 - black - size S
INSERT INTO sizes (size, stock, variant_id) VALUES ('M', 5, 1); -- hat 1 - black - size M
INSERT INTO sizes (size, stock, variant_id) VALUES ('S', 8, 2); -- hat 1 - white - size S
INSERT INTO sizes (size, stock, variant_id) VALUES ('M', 2, 2); -- hat 1 - white - size M
INSERT INTO sizes (size, stock, variant_id) VALUES ('S', 15, 3); -- hat 2 - blue - size S
INSERT INTO sizes (size, stock, variant_id) VALUES ('M', 12, 3); -- hat 2 - blue - size M
INSERT INTO sizes (size, stock, variant_id) VALUES ('L', 10, 4); -- tshirt 1 - black - size L
INSERT INTO sizes (size, stock, variant_id) VALUES ('XL', 5, 4); -- tshirt 1 - black - size XL
INSERT INTO sizes (size, stock, variant_id) VALUES ('L', 7, 5); -- tshirt 1 - blue - size L
INSERT INTO sizes (size, stock, variant_id) VALUES ('XL', 3, 5); -- tshirt 1 - blue - size XL
INSERT INTO sizes (size, stock, variant_id) VALUES ('S', 10, 6); -- tshirt 1 - white - size S
INSERT INTO sizes (size, stock, variant_id) VALUES ('M', 5, 6); -- tshirt 1 - white - size M
INSERT INTO sizes (size, stock, variant_id) VALUES ('L', 10, 7); -- jacket 1 - black - size L
INSERT INTO sizes (size, stock, variant_id) VALUES ('XL', 5, 7); -- jacket 1 - black - size XL
INSERT INTO sizes (size, stock, variant_id) VALUES ('L', 7, 8); -- jacket 1 - white - size L
INSERT INTO sizes (size, stock, variant_id) VALUES ('XL', 3, 8); -- jacket 1 - white - size XL
INSERT INTO sizes (size, stock, variant_id) VALUES ('L', 10, 9); -- jacket 1 - brown - size L
INSERT INTO sizes (size, stock, variant_id) VALUES ('XL', 5, 9); -- jacket 1 - brown - size XL
INSERT INTO sizes (size, stock, variant_id) VALUES ('S', 10, 10); -- bag 1 - black - size S
INSERT INTO sizes (size, stock, variant_id) VALUES ('M', 5, 10); -- bag 1 - black - size M
INSERT INTO sizes (size, stock, variant_id) VALUES ('S', 8, 11); -- bag 1 - brown - size S
INSERT INTO sizes (size, stock, variant_id) VALUES ('M', 2, 11); -- bag 1 - brown - size M
INSERT INTO sizes (size, stock, variant_id) VALUES ('S', 10, 12); -- bag 2 - black - size S
INSERT INTO sizes (size, stock, variant_id) VALUES ('M', 5, 12); -- bag 2 - black - size M

-- Insert data into 'cart_items' table
INSERT INTO cart_items (product_id, title, color, size, quantity, preview_image) VALUES (1, 'Some Hat 1', 'Black', 'S', 2, 'images/hats/01-black.jpg');
INSERT INTO cart_items (product_id, title, color, size, quantity, preview_image) VALUES (3, 'Some T-shirt 1', 'Black', 'L', 1, 'images/tshirts/black-01.jpg');
INSERT INTO cart_items (product_id, title, color, size, quantity, preview_image) VALUES (4, 'Some Jacket 1', 'White', 'XL', 3, 'images/jackets/01-white.jpg');
