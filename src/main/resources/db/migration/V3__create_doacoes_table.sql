-- A tabela doacoes já foi criada na migration V2.
-- Esta versão só garante os índices usados nos filtros por data e tipo.
-- A chave estrangeira para doadores não é criada aqui porque essa tabela ainda não existe.

CREATE INDEX IF NOT EXISTS idx_doacoes_data ON doacoes(data_doacao);
CREATE INDEX IF NOT EXISTS idx_doacoes_tipo ON doacoes(tipo);
