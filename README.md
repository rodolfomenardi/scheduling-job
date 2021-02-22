# scheduling-job

## Rodar a aplicação

- Clonar este repositório;
- Abrir o bash no diretório raiz do projeto;
- Para executar no Windows, rode o comando `gradlew run --args="pathToExecutionWindowFile pathToJobsFile pathToResultFile"` substituindo os parametros pelos respectivos patchs dos arquivos JSON. Ex.: `gradlew run --args="C:\temp\executionWindows.json C:\temp\jobs.json C:\temp\result.json"`;
- Para executar no Linux, rode o comando `./gradlew run --args="pathToExecutionWindowFile pathToJobsFile pathToResultFile"` substituindo os parametros pelos respectivos patchs dos arquivos JSON. Ex.: `./gradlew run --args="/temp/executionWindows.json /temp/jobs.json /temp/result.json"`;

## Exemplos dos arquivos

- Abrir o bash no diretório raiz do projeto;
- Para executar no windows, rode o comando `gradlew test`
- Para executar no Linux, rode o comando `./gradlew test`

#### ExecutionWindow:

```
{
  "start": "2021-02-22 11:40:00",
  "end": "2021-02-23 12:00:00"
}
```

#### Jobs:

```
[
  {
    "id": 1,
    "description": "Importação de arquivos de fundos",
    "maxDateTimeToFinish": "2019-11-10 12:00:00",
    "estimatedDuration": "02:00:00"
  },
  {
    "id": 2,
    "description": "Importação de dados da Base Legada",
    "maxDateTimeToFinish": "2019-11-11 12:00:00",
    "estimatedDuration": "04:00:00"
  },
  {
    "id": 3,
    "description": "Importação de dados de integração",
    "maxDateTimeToFinish": "2019-11-11 08:00:00",
    "estimatedDuration": "06:00:00"
  }
]
```

#### Retorno experado:

```
[
  [
    {
      "id": 1,
      "description": "Importação de arquivos de fundos",
      "maxDateTimeToFinish": "2019-11-10 12:00:00",
      "estimatedDuration": "02:00:00"
    },
    {
      "id": 3,
      "description": "Importação de dados de integração",
      "maxDateTimeToFinish": "2019-11-11 08:00:00",
      "estimatedDuration": "06:00:00"
    }
  ], [
    {
      "id": 2,
      "description": "Importação de dados da Base Legada",
      "maxDateTimeToFinish": "2019-11-11 12:00:00",
      "estimatedDuration": "04:00:00"
    }
  ]
]
```
