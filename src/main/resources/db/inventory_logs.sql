-- Bang ghi lich su thay doi ton kho
CREATE TABLE IF NOT EXISTS inventory_logs (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    option_variant_id   INT NOT NULL,
    product_id          INT,
    action_type         ENUM('IMPORT','EXPORT','ADJUSTMENT') NOT NULL,
    quantity_change     INT NOT NULL,
    stock_before        INT NOT NULL,
    stock_after         INT NOT NULL,
    reason              VARCHAR(500),
    user_id             INT,
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_option    (option_variant_id),
    INDEX idx_product   (product_id),
    INDEX idx_created   (created_at),
    INDEX idx_action    (action_type)
);
