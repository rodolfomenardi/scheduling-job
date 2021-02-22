# scheduling-job

## Rodar a aplicação

- Clonar este repositório;
- Abrir o bash no diretório raiz do projeto;
- Para executar no Windows, rode o comando `gradlew.bat run --args="pathToExecutionWindowFile pathToJobsFile pathToResultFile"` substituindo os parametros pelos respectivos patchs dos arquivos JSON.
- Para executar no Linux, rode o comando `./gradlew run --args="pathToExecutionWindowFile pathToJobsFile pathToResultFile"` substituindo os parametros pelos respectivos patchs dos arquivos JSON.

## Exemplos dos arquivos

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
			"description": "Importação de arquivos de fundos",
			"estimatedDuration": "02:00:00",
			"id": 1,
			"maxDateTimeToFinish": "2019-11-10 12:00:00"
		},
		{
			"description": "Importação de dados de integração",
			"estimatedDuration": "06:00:00",
			"id": 3,
			"maxDateTimeToFinish": "2019-11-11 08:00:00"
		}
	],
	[
		{
			"description": "Importação de dados da Base Legada",
			"estimatedDuration": "04:00:00",
			"id": 2,
			"maxDateTimeToFinish": "2019-11-11 12:00:00"
		}
	]
]
```
