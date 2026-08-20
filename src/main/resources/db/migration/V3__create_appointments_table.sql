CREATE TABLE appointments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    professional_id BIGINT NOT NULL,
    start_time TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_patient_id FOREIGN KEY (patient_id) REFERENCES users (id),
    CONSTRAINT fk_professionals_id FOREIGN KEY (professional_id) REFERENCES professionals (id)
);