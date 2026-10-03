# Registro de Uso de IA Generativa

> **Política da disciplina:** O uso de IA generativa é permitido como
> assistente. O discente é **integralmente responsável** por testar, auditar e
> defender todo o código entregue, independentemente de como foi gerado.

## Instruções

Para cada aula ou entrega, registre abaixo:
- **Data**
- **Ferramenta** (ChatGPT, Copilot, Claude, etc.)
- **Prompt(s) utilizado(s)** (resumo ou cópia)
- **O que foi feito com a saída** (copiado integralmente, adaptado, usado como referência, descartado)

---

## Registro

| Data | Aula | Ferramenta | Prompt (resumo) | Uso da saída |
|------|------|------------|-----------------|--------------|
| 02/10/2026 | A1 | Cursor (Grok) | Ajuda na criação e implementação completa do módulo de produtos (entidades, DTOs, CRUD e Swagger). | A estrutura inicial foi gerada pela IA e adaptada ao padrão arquitetural já existente no projeto. |
| 02/10/2026 | A1 | Cursor (Grok) | Ajuda na criação de testes unitários para a camada de serviço e testes de integração executando em containers Docker. | Código gerado foi revisado, adaptado e utilizado para validar o fluxo de execução dos endpoints. |
| 02/10/2026 | A1 | Cursor (Grok) | Análise, diagnóstico e correção de erros de lógica internos da aplicação e falhas na validação de endpoints. | As sugestões da IA foram testadas, ajustadas e aplicadas onde a regra de negócio apresentava comportamentos incorretos. |

## Declaração de Uso de IA (A1)

### Ferramentas utilizadas:
- [x] Cursor (Grok)
- [ ] ChatGPT / Claude / Gemini
- [ ] Copilot / Codeium
- [ ] Nenhuma

### Como utilizei:
- **Implementação do Módulo de Produtos:** Utilizei a IA para auxiliar na criação e estruturação do módulo de produtos, mantendo a consistência com o padrão já adotado nos outros módulos da aplicação.
- **Testes Unitários e de Integração:** Usei para criar a cobertura de testes unitários da camada de serviço e configurar os testes de integração utilizando ambiente Docker.
- **Correção de Lógica e Validação de Endpoints:** Recorri à IA para identificar e resolver erros de lógica de negócio e garantir o funcionamento correto das requisições e respostas dos endpoints.

### O que eu entendo 100%:
- A estrutura do CRUD de produtos, incluindo fluxo de dados entre controller, service, DTOs e repositório.
- A lógica de validação aplicada às regras de negócio e os retornos HTTP dos endpoints.
- A estrutura e os cenários cobertos pelos testes unitários e de integração.

### O que precisei estudar mais:
- Configuração e execução de testes de integração isolados com containers Docker.
- Diagnóstico e resolução de erros de lógica na validação de estados e regras de negócio da aplicação.

---

_Declaração: Ao submeter este repositório, confirmo que todo o código foi
revisado, testado e compreendido por mim._