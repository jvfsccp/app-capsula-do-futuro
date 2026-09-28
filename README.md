# Cápsula do Futuro

Aplicativo Android (Java + XML) em que o usuário escreve uma mensagem para o futuro,
escolhe o ano em que ela poderá ser aberta e visualiza a cápsula selada em uma segunda tela.

## Como executar

Abra a pasta no Android Studio e rode a configuração `app`, ou pela linha de comando:

```bash
./gradlew installDebug
```

## Estrutura

| Arquivo | Papel |
| --- | --- |
| `MainActivity.java` | Tela de criação: SeekBar com `OnSeekBarChangeListener`, validação do formulário e Intent explícita |
| `CapsulaActivity.java` | Tela de visualização: recupera os extras, botão **Editar** (`finish()`) e **Compartilhar** (`ACTION_SEND`) |
| `NivelImportancia.java` | Converte o percentual em uma descrição ("Essencial para o meu futuro" etc.) |
| `TelaUtils.java` | Ajusta o espaçamento às barras do sistema e ao teclado |
| `res/layout/` e `res/layout-land/` | Layouts em `ConstraintLayout` para retrato e paisagem |
| `res/values/strings.xml` | Todos os textos exibidos ao usuário |

## Regras de validação

- Nome do autor, título, ano e mensagem são obrigatórios.
- O ano de abertura precisa ser posterior a 2026.
- A mensagem precisa ter pelo menos 20 caracteres.

Cada campo inválido mostra sua mensagem de erro; o foco vai para o primeiro campo com problema
e a segunda tela só abre quando tudo estiver correto.
