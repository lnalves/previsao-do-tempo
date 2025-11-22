# Previsão do Tempo

Este é um projeto de aplicação Desktop desenvolvida em **Java** para gerenciamento e visualização de dados de previsão do tempo.
O sistema utiliza a API open-meteo.com para buscar dados em tempo real de execução e atualizar a interface. Além disso, usa arquitetura em camadas (MVC) e persistência de dados via banco de dados SQL.

O software foi estruturado para separar a lógica de negócios, a interface do usuário e o acesso aos dados, facilitando a manutenção e a escalabilidade.

* **Model:** Classes que representam os dados do sistema .
* **View:** Interface gráfica com o usuário.
* **DAL (Data Access Layer):** Camada responsável pela comunicação com o banco de dados.
* **Script SQL:** Scripts para criação e configuração do banco de dados.
* **Lib:** Bibliotecas externas (dependências do projeto, como drivers de banco de dados).


## Tecnologias Utilizadas

* **Linguagem:** Java
* **Banco de Dados:** MySQL 
* **Interface Gráfica:** Java Swing

## Como Executar o Projeto

1. Clonar o Repositório

git clone [https://github.com/lnalves/previsao-do-tempo.git](https://github.com/lnalves/previsao-do-tempo.git)

3. Configurar o Banco de Dados

Navegue até a pasta script sql no diretório do projeto.

Abra o arquivo de script (ex: script.sql ou similar) em seu cliente de banco de dados (MySQL Workbench, DBeaver, etc.).

Execute o script para criar o banco de dados e as tabelas necessárias.

3. Configurar a Conexão
   
Abra o projeto na sua IDE.
  
Navegue até o pacote dal (Data Access Layer).
  
Localize a classe de conexão(nomeada Conexao.java neste projeto).
  
Verifique se as credenciais (URL do banco, usuário e senha) correspondem às do seu banco de dados local. Caso necessário, altere-as.

5. Importar Dependências

Certifique-se de que os arquivos .jar localizados na pasta lib estejam adicionados ao Classpath ou às Bibliotecas do seu projeto na IDE.

7. Rodar a Aplicação

Navegue até o pacote view.
Execute App.java
