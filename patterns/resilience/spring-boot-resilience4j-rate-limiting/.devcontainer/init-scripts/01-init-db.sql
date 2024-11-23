-- Create application user
CREATE USER app_user WITH PASSWORD 'app_password';

-- Create database
CREATE DATABASE customerdb;

-- Connect to the database
\c customerdb

-- Create extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create customers table
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT true
);

-- Create indexes for common search patterns
CREATE INDEX idx_customers_name ON customers(name);
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_active ON customers(active);

-- Create updated_at trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger for updated_at
CREATE TRIGGER update_customers_updated_at
    BEFORE UPDATE ON customers
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Create view for active customers
CREATE OR REPLACE VIEW active_customers AS
    SELECT id, name, email, phone, created_at, updated_at
    FROM customers
    WHERE active = true;

-- Create function to search customers by name (case insensitive)
CREATE OR REPLACE FUNCTION search_customers_by_name(search_term VARCHAR)
RETURNS TABLE (
    id BIGINT,
    name VARCHAR,
    email VARCHAR,
    phone VARCHAR,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        c.id, c.name, c.email, c.phone, c.created_at, c.updated_at
    FROM 
        customers c
    WHERE 
        c.active = true 
        AND c.name ILIKE '%' || search_term || '%'
    ORDER BY 
        c.name;
END;
$$ LANGUAGE plpgsql;

-- Grant specific permissions to application user
GRANT CONNECT ON DATABASE customerdb TO app_user;
GRANT USAGE ON SCHEMA public TO app_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON customers TO app_user;
GRANT USAGE, SELECT ON SEQUENCE customers_id_seq TO app_user;
GRANT SELECT ON active_customers TO app_user;
GRANT EXECUTE ON FUNCTION search_customers_by_name(VARCHAR) TO app_user;
GRANT EXECUTE ON FUNCTION update_updated_at_column() TO app_user;

-- Insert sample data
INSERT INTO customers (name, email, phone) VALUES
    ('John Doe', 'john.doe@example.com', '+1234567890'),
    ('Jane Smith', 'jane.smith@example.com', '+1234567891'),
    ('Robert Johnson', 'robert.johnson@example.com', '+1234567892'),
    ('Maria Garcia', 'maria.garcia@example.com', '+1234567893'),
    ('James Wilson', 'james.wilson@example.com', '+1234567894'),
    ('Patricia Brown', 'patricia.brown@example.com', '+1234567895'),
    ('Michael Davis', 'michael.davis@example.com', '+1234567896'),
    ('Linda Martinez', 'linda.martinez@example.com', '+1234567897'),
    ('William Anderson', 'william.anderson@example.com', '+1234567898'),
    ('Elizabeth Thomas', 'elizabeth.thomas@example.com', '+1234567899');

-- Create statistics for query optimizer
ANALYZE customers;