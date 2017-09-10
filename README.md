# AvailFormsGenerator

[![Build Status](https://travis-ci.org/leogomes26/AvailFormsGenerator.svg?branch=master)](https://travis-ci.org/leogomes26/AvailFormsGenerator)

AvailFormsGenerator é um framework para realizar o mapeamento de suas classes java, juntamente com o [javax.persistence](https://docs.oracle.com/javaee/7/api/javax/persistence/package-summary.html), através de anotações com o objetivo final de criar objetos que irão conter informações sobre o formulário(view) para as ações de CRUD da classe mapeada.
Esse projeto pode ser utilizado juntamente com o projeto [AvailFormsAngular](https://github.com/leogomes26/AvailFormsAngular.git) para geração de suas telas dinâmicamente, ou você pode criar seu proprio framework cliente que irá utlizar os dados gerados pelo AvailFormsGenerator.

**Duvidas ou Sugestões:** leonardogomesdev@gmail.com
**Desenvolvido Por**: Leonardo Gomes

# Pré Requisitos:

 - Maven 3.5.0 ou superior
 - Java 7 ou superior

## Índice

 - [Instalação](#instalacao)
 - [Anotações](#anotacoes)
 - [@Form](#form)
 - [@CampoForm](#campoForm)
 - [@Image](#imagem)
 - [Relacionamento entre entidades](#relacionamentos)
 - [Geração de dados do formulário](#gerDadosForm)
 - [Geração de dados de listagem](#gerDadosListagem)

## Instalação

Para utilizar o AvailFormsGenerator em seu projeto java com [Maven](https://maven.apache.org/) basta adicionar as essas duas tags no seu arquivo pom.xml
- Essa primeira é para que o Maven conheça o repositório onde está nosso projeto.
```xml

```

- Essa segunda irá importar para seu projeto a biblioteca AvailFormsGenerator e suas dependências.
```xml

```
## Anotações

As anotações são a base de nosso projeto, é atrvés delas que iremos mapear nossas classes para geração das dados de nossa tela. São 3 as principais anotações, @Form, uma anotação a nível de classe que irá identificar essa classe como um formulário.

## @Form

 - Exemplo
 ```java
 @Form(nomeEntidade = "Usuário", orientacao = Orientacao.HORIZONTAL)
 public class UsuarioEntity
 ```
- Atributos da anotação @Form
 - nomeEntidade: Esse atributo é obrigatório e servirá para identificar o nome de nossa tela.
 - oritenracao: Esse não é obrigatório e servirá para identificar a orientação de nosso formulário sendo dois tipos, HORIZONTAL e VERTICAL, o valor default é HORIZONTAL.

## @CampoFrom

A anotação @CampoForm é uma anotação a nivel de atributo de classes e será utilizada para identificar um atributo como um campo do formulário.

 - Exemplo
 ```java
 @Form(nomeEntidade = "Usuário")
 public class UsuarioEntity{
 @CampoForm(label = "Email", tipo = TipoCampo.EMAIL, listagem = true)
 private String email;
 }
 ```
- Atributos da anotção @CampoForm
 - label: Será o label utilizado na apresentação do campo, atributo obrigatório.
 - tipo: Identificador do tipo de campo, valor padrão TEXTO.
 - Opções de tipo de campo:

 | Tipo Campo | Descrição |
 | ------ | ------ |
 | Nenhum | Quando o campo não tem tipo definido.|
 | CPF | Numero de documento de pessoa física |
 | CNPJ | Numero de documento de pessoa jurídica |
 | TELEFONE | Telefone |
 | CEP | Código de endereço postal |
 | SENHA | Senha |
 | COMBOBOX | Combobox para escolha |
 | CHECKMARK | Checkmark para multipla escolha |
 | TEXTO | Texto Livre |
 | NUMERICO | Numeral |
 | DATA | Data |
 | HORA | Hora |
 | DATA_HORA | Data e Hora |
 | MOEDA | Dinheiro |
 | PESQUISA | Campo do tipo pesquisável, onde serpá buscado um registro relecionado|
 | IMAGEM | Campo utilizado para Upload de imagens |
 | TEXTORICO | Editor de texto |

 - opcoes: esse atributo é utilizado quando o campo é do tipo COMBOBOX ou CHECKMARK, e ele servirá para identificar as opções de seleção desse campo. Não obrigatório, valor padrão "".
 - editavel: Representa se o campo é editavel ou não. Não obrigatório, valor default TRUE.
 - listagem: Representa se o campo estrá presente na apresentação de listagem do objeto. Não obrigatório, valor default FALSE.

## @Image

A anotação @Image será utilizada para identificar o campo como um campo que representa uma imagem.
 - Exemplo
 ```java
 @Form(nomeEntidade = "Usuário")
 public class UsuarioEntity{
 @Image(label = "Imagem de Perfil")
 private FileBase64 imagemPerfil;
 }
 ```
- Atributos da anotção @Image
 - label: Será o label utilizado na apresentação do campo, atributo obrigatório.
 - qtdImgs: Quantidade de imagens que será possivél inserir no campo.

## Relacionamento entre Entidades

A nossa ferramenta suporta criação de subtelas de relacionamentos de nosso objeto mapeado, para isso basta nossa classe principal estar com o campo relacionado mapeado com uma das anotações de relacionamento do [javax.persistence](https://docs.oracle.com/javaee/7/api/javax/persistence/package-summary.html), os relacionamentos suportados estão listados na tabela abaixo, e a classe relacionada precisa estar mapeada com a anotação @Form e seus campos com a anotação @CampoForm

- Relacionamentos Suportados

 | Tipo Relacionamento(javax.persistence) | Tipo Entidade|
 | ------ | ------ |
 | @ManyToMany | PESQUISAVEL_MUITOS|
 | @ManyToMany(cascade = CascadeType.ALL) | ADICIONAVEL_MUITOS|
 | @ManyToOne | PESQUISAVEL_UM|
 | @ManyToOne(cascade = CascadeType.ALL) | ADICIONAVEL_UM|
 | @OneToOne | PESQUISAVEL_UM|
 | @OneToOne(cascade = CascadeType.ALL) | ADICIONAVEL_UM|
 | @OneToMany | ADICIONAVEL_MUITOS |
 | @OneToMany(cascade = CascadeType.ALL) | PESQUISAVEL_MUITOS|

## Geração de dados do formulário

Para gerar os dados do formulário será necessário acessar o método static CreateForms.getDadosForm(java.lang.Class) passando a classe que você deseja criar o formulário, esse metódo irá retornar um objeto do tipo FormPojo com os dados de seu formulário, utilizando o exempo do usuário que utilizamos anteriormente:
- Classes:
 ```java
 //Classe Principal
 @Form(nomeEntidade = "Usuário")
 public class UsuarioEntity{
 @Column(nullable = false)
 @CampoForm(label = "Nome", listagem = true)
 private String nome;

 @ManyToMany(fetch = FetchType.EAGER)
 private List
