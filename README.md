
# TCCFerramenta
Ferramenta desenvolvida para auxiliar na visualização e comparação dos algoritmos explorados no Trabalho de Conclusão de Curso do aluno Igor Henrique Torati Ruy.

O código desta ferramenta pode ser aberto como projeto no Eclipse IDE, também é possível executar o arquivo TCCFerramenta.jar, caso deseje apenas utilizar a ferramenta.

# Propósito da ferramenta
O propósito desta ferramenta é possibilitar a criação cenários e a visualização do funcionamento dos algoritmos mencionados no segundo capítulo da monografia do TCC apresentado.

Para essa finalidade, a ferramenta permite a criação de grades quadradas, e permite ao usuário definir os obstáculos, ponto de partida e ponto de destino.

# Opções da ferramenta
A ferramenta possui um menu lateral com várias opções, são elas:

 - Obstáculo (*Radio*): quando essa opção está selecionada e o usuário
   clica com o mouse em uma das grades, a célula é preenchida com a cor
   preta, representando um obstáculo.
 - Apagar (*Radio*): quando essa opção está selecionada e o usuário
   clica com o mouse em uma das grades, a célula é limpa de qualquer
   preenchimento, seja ele obstáculo, marcação de início e marcação de
   destino.
 - Início (*Radio*): quando essa opção está selecionada e o usuário
   clica com o mouse em uma das grades, a célula é limpa de qualquer
   obstáculo ou marcador de destino, e é preenchida com o marcador de
   início. Apenas pode existir um início na grade. Se já houver um, ao
   clicar com o mouse, o marcador existente será apagado e o novo será
   definido.
 - Destino (*Radio*): quando essa opção está selecionada e o usuário
   clica com o mouse em uma das grades, a célula é limpa de qualquer
   obstáculo ou marcador de início, e é preenchida com o marcador de
   destino. Apenas pode existir um destino na grade. Se já houver um, ao
   clicar com o mouse o marcador existente será apagado e o novo será
   definido.
 - Buscar Caminho (Botão): quando este botão é pressionado, todos os
   algoritmos começam a buscar um caminho do ponto de início até o ponto
   de destino, considerando que os obstáculos não podem ser percorridos.
   Caso não exista um ponto de início, um ponto de destino ou o destino
   não seja alcançável a partir do ponto de início, é mostrado abaixo de
   cada grade a mensagem “Caminho não encontrado!”
 - Limpar Matriz (Botão): limpa a grade, removendo obstáculos, marcador
   de início e marcador de fim. Elimina também qualquer marcação de
   célula visitada e caminho selecionado.
 - Tamanho da Célula (*Scroll*): determina o tamanho da célula mostrada
   na grade. Com um valor maior, a célula possui um tamanho maior e um
   menor número de linhas e colunas para que caiba no espaço definido.
 - Delay da animação (*Scroll*): determina o intervalo entre uma
   iteração e outra. Valores mais altos ocasionam em um menor número de
   atualizações, o que permite visualizar cada passo dos algoritmos. Um
   valor menor permite que a execução termine mais rapidamente.
 - Salvar Matriz (Botão): permite que a matriz seja salva em um arquivo
   json, que pode ser importado posteriormente. Além da matriz, também é
   salvo o tamanho da célula.
 - Carregar Matriz (Botão): permite que uma matriz previamente salva
   seja importada, com os obstáculos, e, opcionalmente, marcador de
   início e fim, já definidos.
 - Salvar Resultados (Botão): exporta os resultados para uma nova pasta.
   Esse diretório será criado a partir da raiz de onde o código foi
   executado, e tem o nome igual ao momento que foi clicado (no formato
   “yyyy-MM-dd-HH.mm.ss”). Dentro dessa pasta, é salvo um Json contendo
   o nome do algoritmo, o número de células visitadas e o custo para se
   chegar ao destino. Além do arquivo Json, é salvo também uma imagem
   PNG, que mostra o grade e o caminho encontrado por cada algoritmo.

# Exemplo de uso
Para auxiliar na compreensão de como a ferramenta pode ser utilizada, será descrito abaixo um exemplo de como ela pode ser utilizada por um usuário.

Assim que acessa a aplicação, o usuário percebe que as células são muito pequenas, ele deseja aumentar o tamanho delas, para isso ele modifica a opção destacada abaixo:

![imagem1](https://i.imgur.com/SnB0v7y.png)

Ao fazer isso, ele seleciona um tamanho que fique agradável para o cenário que deseja criar, ficando da seguinte forma:
![imagem2](https://i.imgur.com/30ITpHN.png)

Ao acertar o tamanho, ele decide que o delay da animação deve ser o menor possível, ele não deseja observar e compreender como cada um dos algoritmos irá buscar o caminho, está apenas interessado no caminho encontrado por cada um, sendo assim, ele modifica a seguinte opção:
![imagem3](https://i.imgur.com/ReqHGnM.png)

E a define com o menor valor permitido:
![imagem4](https://i.imgur.com/3Wzfk4r.png)

Após isso, ele já está utilizando as configurações desejadas de intervalo e tamanho das células, e começa a desenhar o mapa, para tal, ele verifica se a opção "Obstáculo" está selecionada, nesse caso, está:
![imagem5](https://i.imgur.com/u7B0Bje.png)

Ele desenha o mapa da forma que deseja, em seguida, seleciona a opção "Início" para determinar o ponto de partida:
![imagem6](https://i.imgur.com/Kld8hOy.png)

Ele marca o ponto desejado, em seguida, seleciona a opção "Destino", para determinar o ponto de destino:
![imagem7](https://i.imgur.com/UuShaY9.png)

Após selecionar o destino, a grade está da seguinte forma:
![imagem8](https://i.imgur.com/aqpX4MS.png)

O usuário deseja agora encontrar o caminho, para isso, escolhe a opção "Buscar Caminho".
![imagem9](https://i.imgur.com/74i5dsc.png)

Ele aguarda alguns instantes e logo tem o seguinte resultado e sua tela:
![imagem9.1](https://i.imgur.com/v2S3vJK.png)

Ele está satisfeito com os resultados e decide salva-los, também acha necessário salvar a matriz, para isso, ele clica em "Salvar Resultados" e em seguida "Salvar Matriz" e escolhe o local onde deseja salvar a configuração da grade.
![imagem10](https://i.imgur.com/qZohDvI.png)

Ele observa que o resultado foi salvo na mesma pasta onde está o executável da ferramenta e decide salvar a grade no mesmo local. Desse modo, ele acaba com os seguintes arquivos:
![imagem11](https://i.imgur.com/ksMk9rv.png)

Dentro da pasta "2022-04-12-21.46.23" é onde está salvo o resultado de sua execução, com os seguintes arquivos:
![imagem12](https://i.imgur.com/4Vl1wcn.png)
