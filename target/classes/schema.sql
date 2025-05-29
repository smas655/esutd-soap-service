CREATE TABLE IF NOT EXISTS ref_dictionary (
    id BIGSERIAL PRIMARY KEY,
    reference_type VARCHAR(50) NOT NULL,
    code VARCHAR(50) NOT NULL,
    name_ru VARCHAR(255) NOT NULL,
    name_kz VARCHAR(255),
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATE NOT NULL,
    updated_at DATE,
    parent_id BIGINT,
    action_type VARCHAR(20),
    is_actual BOOLEAN DEFAULT TRUE,
    actual_date TIMESTAMP,
    is_selectable BOOLEAN DEFAULT TRUE,
    CONSTRAINT uq_reference_type_code UNIQUE (reference_type, code)
);

CREATE TABLE IF NOT EXISTS employers (
    employer_id VARCHAR(50) PRIMARY KEY,
    bin VARCHAR(12) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    kato_code VARCHAR(20),
    employer_type VARCHAR(50),
    address_country VARCHAR(10) NOT NULL,
    address_kato_code VARCHAR(20),
    address_city VARCHAR(100),
    address_street VARCHAR(255),
    address_building VARCHAR(50),
    address_postal_code VARCHAR(20),
    phone VARCHAR(20),
    email VARCHAR(100),
    bank_name VARCHAR(255),
    bik VARCHAR(50),
    account_number VARCHAR(50),
    created_at DATE NOT NULL,
    updated_at DATE
);

CREATE TABLE IF NOT EXISTS employees (
    employee_id VARCHAR(50) PRIMARY KEY,
    iin VARCHAR(12) NOT NULL UNIQUE,
    last_name VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    birth_date DATE,
    gender VARCHAR(10) NOT NULL,
    citizenship VARCHAR(10) NOT NULL,
    address_country VARCHAR(10),
    address_kato_code VARCHAR(20),
    address_city VARCHAR(100),
    address_street VARCHAR(255),
    address_building VARCHAR(50),
    address_apartment VARCHAR(50),
    address_postal_code VARCHAR(20),
    bank_name VARCHAR(255),
    bik VARCHAR(50),
    account_number VARCHAR(50),
    phone_number VARCHAR(20),
    hr_phone_number VARCHAR(20),
    created_at DATE NOT NULL,
    updated_at DATE
);

CREATE TABLE IF NOT EXISTS contracts (
    contract_id VARCHAR(50) PRIMARY KEY,
    contract_number VARCHAR(100) NOT NULL,
    contract_date DATE NOT NULL,
    contract_duration_type VARCHAR(100),
    start_date DATE NOT NULL,
    end_date DATE,
    contract_type VARCHAR(50) NOT NULL,
    position VARCHAR(255) NOT NULL,
    position_code VARCHAR(1024) NOT NULL,
    work_type VARCHAR(100),
    remote_work BOOLEAN,
    work_place_address VARCHAR(255),
    work_place_kato VARCHAR(20),
    work_place_country VARCHAR(10),
    work_hours VARCHAR(100),
    tariff_rate NUMERIC(12, 2),
    work_conditions TEXT,
    work_condition_code VARCHAR(50),
    department VARCHAR(255),
    general_skills TEXT,
    professional_skills TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    termination_date DATE,
    termination_reason VARCHAR(255),
    termination_reason_code VARCHAR(50),
    employee_id VARCHAR(50) NOT NULL,
    employer_id VARCHAR(50) NOT NULL,
    created_at DATE NOT NULL,
    updated_at DATE,
    CONSTRAINT fk_contract_employee FOREIGN KEY (employee_id) REFERENCES employees (employee_id),
    CONSTRAINT fk_contract_employer FOREIGN KEY (employer_id) REFERENCES employers (employer_id)
);

-- Reference tables for professional skills and areas
CREATE TABLE IF NOT EXISTS ref_profession (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL,
    name_ru TEXT NOT NULL,
    name_kz TEXT,
    action_type VARCHAR(20),
    parent_code VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_selectable BOOLEAN DEFAULT TRUE,
    group_code VARCHAR(20),
    created_at DATE NOT NULL,
    updated_at DATE
);

CREATE TABLE IF NOT EXISTS ref_professional_skill (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL,
    name_ru TEXT NOT NULL,
    name_kz TEXT,
    description TEXT,
    fl_check VARCHAR(10),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_selectable BOOLEAN DEFAULT TRUE,
    created_at DATE NOT NULL,
    updated_at DATE
);

CREATE TABLE IF NOT EXISTS ref_professional_area (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL,
    name_ru TEXT NOT NULL,
    name_kz TEXT,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATE NOT NULL,
    updated_at DATE
);

CREATE TABLE IF NOT EXISTS ref_prof_area_to_profession (
    id BIGSERIAL PRIMARY KEY,
    prof_area_code VARCHAR(20) NOT NULL,
    profession_code VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATE NOT NULL,
    updated_at DATE
);

CREATE TABLE IF NOT EXISTS ref_profession_to_skill (
    id BIGSERIAL PRIMARY KEY,
    profession_code VARCHAR(20) NOT NULL,
    skill_code VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATE NOT NULL,
    updated_at DATE
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_references_type_code ON ref_dictionary(reference_type, code);
CREATE INDEX IF NOT EXISTS idx_employee_iin ON employees(iin);
CREATE INDEX IF NOT EXISTS idx_employer_bin ON employers(bin);
CREATE INDEX IF NOT EXISTS idx_contract_employee ON contracts(employee_id);
CREATE INDEX IF NOT EXISTS idx_contract_employer ON contracts(employer_id);
CREATE INDEX IF NOT EXISTS idx_contract_active ON contracts(is_active);
CREATE INDEX IF NOT EXISTS idx_contract_dates ON contracts(start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_ref_profession_code ON ref_profession(code);
CREATE INDEX IF NOT EXISTS idx_ref_profession_parent_code ON ref_profession(parent_code);
CREATE INDEX IF NOT EXISTS idx_ref_professional_skill_code ON ref_professional_skill(code);
CREATE INDEX IF NOT EXISTS idx_ref_professional_area_code ON ref_professional_area(code);
CREATE INDEX IF NOT EXISTS idx_ref_prof_area_to_profession_codes ON ref_prof_area_to_profession(prof_area_code, profession_code);
CREATE INDEX IF NOT EXISTS idx_ref_profession_to_skill_codes ON ref_profession_to_skill(profession_code, skill_code);