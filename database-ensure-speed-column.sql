-- Ensure speed column exists in vehicles table
-- SQLite doesn't support ALTER TABLE IF EXISTS, so we use a workaround

-- First, check if speed column exists by trying to select it
-- If it doesn't exist, this will be handled by Hibernate's auto-update

-- Alternative: Add column if not exists (for manual execution)
-- Note: SQLite requires specific syntax
ALTER TABLE vehicles ADD COLUMN speed REAL DEFAULT 0.0;

-- Update all vehicles to have speed = 0 if NULL
UPDATE vehicles SET speed = 0.0 WHERE speed IS NULL;
