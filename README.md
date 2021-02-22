# scheduling-job

## Rodar a aplicação

- Clonar este repositório;
- Abrir o bash no diretório raiz do projeto;
- Para executar no Windows, rode o comando `gradlew.bat run --args="pathToExecutionWindowFile pathToJobsFile pathToResultFile"` substituindo os parametros pelos respectivos patchs dos arquivos JSON.
- Para executar no Linux, rode o comando `./gradlew run --args="pathToExecutionWindowFile pathToJobsFile pathToResultFile"` substituindo os parametros pelos respectivos patchs dos arquivos JSON.

## Exemplos dos arquivos

#### ExecutionWindow

```
{
  "start": "2021-02-22 11:40:00",
  "end": "2021-02-23 12:00:00"
}
```
