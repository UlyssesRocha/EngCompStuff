//  Created by Ulysses on 8/1/15.
//  Copyright (c) 2015. All rights reserved.

/*
 == Exercicio Problema 01 ======
 == Metodos Numericos 2015.1 ===
     Equipe:
    --> Antonio Rufino
    --> Jecol Bamutsha
    --> Katarina Hachem
    --> Ulysses Rocha
*//*
 == Descricao ==
 
 Escreva um programa em C com um menu principal que possua 3 opções: ‘C’ – Conversão, ‘S’ – Sistema Linear e ‘F’ – Finalizar.
 
 Ao selecionar a opção ‘C’, o usuario devera digitar um numero decimal, eventualmente com parte fracionaria, e o programa devera exibir esse número no sistema numérico binario, octal e hexadecimal, com ate 20 casas decimais.
 
 Ao selecionar a opção ‘S’, o programa deverá pedir o nome de um arquivo de texto contendo um sistema linear de n equações e n variáveis. O lay-out do arquivo deverá ser:
 
 n
 a1,1 a1,2 ... a1,n b1
 a2,1 a2,2 ... a2,n b2
 ...
 an,1 an,2 ... an,n bn
 
 onde ai,j e o coeficiente da variavel i na equação j e bj e o termo independente da equação j. Por exemplo, o arquivo correspondente ao sistema:
 
 x1 –  x2 + 3x3 =  8
 2x1 – 2x2 +  x3 =  1
 –x1 + 3x2 –  x3 =  2
 
 seria
 
 3
 1 -1  3  8
 2 -2  1  1
 -1  3 -1  2
 
 O programa devera então transformar a matriz de coeficientes numa matriz diagonal usando o Metodo de Jordan. Se o sistema for compativel, exiba a matriz diagonal e uma solução do sistema.
 Se na iteração k do metodo, ak,k for zero, troque a coluna k com uma coluna j tal que k < j ≤ n e ak,j != 0. Lembre-se de que, nesse caso, a coluna k correspondera a variável xj e a coluna j correspondera a variavel xk.
    Se todos os elementos da linha k forem iguais a zero (inclusive o termo independente), a variável xk e livre. Nesse caso, considere seu valor igual a zero e substitua a coluna k da matriz por uma coluna de zeros.
  Se todos os elementos da linha k, menos o termo independente, forem iguais a zero, o sistema será incompatível. Nesse caso, apenas informe que o sistema e incompativel.
 */

/*=== Bibliotecas ====*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
/*=== Defines ========*/
#define VERBOSO 0 /* Verboso exibe passo a passo ao longo da execucao do metodo */
#define INPUT 0 /* 0 leitura do arquivo, 1 leitura do teclado */
#define FILENAME "input.txt" /* nome do arquivo com a entrada*/
#define ERRO_0 0.00000001 /* Erro - 10^-8 */

/*=== Cabecalho =====*/
void exibe_matriz(double **matriz, int tamanho);
void zera_coluna(double **matriz, int index_coluna, int tamanho);
int swap_coluna(int index, double **matriz, double *resposta, int tamanho);
int verifica_sistema_compativel(double **matriz, int tamanho);
int resultado_jordan(double **matriz, double *resposta, int tamanho);
int metodo_jordan(double **matriz, double *resposta, int tamanho);
int carrega_matriz_do_arquivo(double **matriz, double **resposta, int *tamanho);
int carrega_matriz_do_teclado(double **matriz, double **resposta, int *tamanho);
int carrega_array_resposta(double **resposta, int tamanho);

/*=== Funcoes =======*/
void exibe_matriz(double **matriz, int tamanho)
{
    int i,j;
    for (i = 0;i<tamanho;i++){
        for (j = 0;j<tamanho+1;j++)
            printf("%.2lf ",matriz[i][j]);
        printf("\n");
    }
}


/* Seta todos os valores da coluna index a 0 */
void zera_coluna(double **matriz, int index_coluna, int tamanho)
{
    int j;
    for (j=0; j<tamanho; j++) {
        matriz[j][index_coluna] = 0;
    }
}


/* 
 Swap Coluna "tal que Coluna[index] < Coluna[j] ≤ tamanho e a[index][j] != 0"
 1 se a troca foi realizada, 0 caso contrario (variavel livre)
 */
int swap_coluna(int index, double **matriz, double *resposta, int tamanho)
{
    int j,k,temp;
    /*k < j ≤ n*/
    for ( j = index; j<tamanho ; j++ ){
        /*ak,j != 0*/
        if ( matriz[index][j] != 0 ){
            /* Swap */
            for ( k = 0 ; k<tamanho ; k++ ){
                temp = matriz[k][index];
                matriz[k][index]= matriz[k][j];
                matriz[k][j] = temp;
            }

#if VERBOSO
            printf("\nPivo 0!\nTrocando coluna %d por %d\n",index,j);
            exibe_matriz(matriz, tamanho);
            printf("\n");
#endif
            
            /* Swap do marcador de variaveis */
            temp = (int) resposta[index];
            resposta[index] = resposta[j];
            resposta[j] = temp;
            
            return 1;
        }
    }
    /*Variavel livre*/
    return 0;
}


/* 
  Se todos os elementos da linha k, menos o termo independente, forem iguais a zero, o sistema será incompatível. Nesse caso, apenas informe que sistema e incompativel., 1 sistema ok, 0 sistema incompativel
*/
int verifica_sistema_compativel(double **matriz, int tamanho)
{
    int i;
    for (i=0;i<tamanho;i++){
        if (matriz[i][i] == 0 && matriz[i][tamanho] != 0)
            return 0;
    }
    return 1;
}


/*
 Dada a matriz ja transformada, se essa for compativel exibe a resposta
 Retorna 0 caso o sistema seja incompativel
 */
int resultado_jordan(double **matriz, double *resposta, int tamanho)
{
    int i;
    int *indexAux = malloc(sizeof(int)*tamanho);
    if (indexAux == NULL)
        return -1;
    
    /*Recebe valor referente ao indice de cada posicao*/
    for (i=0; i<tamanho; i++)
        indexAux[i] = (int)resposta[i];
    
    
    if(verifica_sistema_compativel(matriz, tamanho)){
        for (i=0; i<tamanho; i++) {
            if(matriz[indexAux[i]][indexAux[i]] != 0){
                resposta[indexAux[i]] = matriz[indexAux[i]][tamanho] / matriz[indexAux[i]][indexAux[i]];
                
#if VERBOSO
                printf("Variavel x%d = %.2lf\n", indexAux[i], resposta[indexAux[i]]);
#endif
            }else{
                resposta[indexAux[i]] = 0;
#if VERBOSO
                printf("Variavel x%d = Livre\n", indexAux[i]);
#endif
            }
        }
        free(indexAux);
        return 1;
    }
    
    free(indexAux);
    return 0;
}


/*
 Executa o metodo de Jordan sobre uma matriz
   ====== Variaveis ========
   int **matrix recebe a matrix sob a qual será realizado o metodo
   int *valores recebe a respostas das variais tais como vieram na matrix
   int tamanho deve indicar o tamanho da matrix de coeficientes e dos valores.
   ====== Retorno ==========
   1 Sucesso, 0 Sistema Incompativel, -1 Erro de Memoria
 */
int metodo_jordan(double **matriz, double *resposta, int tamanho)
{
    int i,j,k;
    
#if VERBOSO
    printf("\n====Iniciando Metodo de Jordan ====\n");
    printf("Matrix Tamanho %d\n",tamanho);
    exibe_matriz(matriz, tamanho);
    printf("\n");
#endif
    
      /* Linhas */
    for (i = 0;i<tamanho;i++){
        double pivo = matriz[i][i];
        
#if VERBOSO
        printf("Iniciando linha com Pivo[%d][%d] = %lf \n",i,i,pivo);
#endif
        
        /* Swap Coluna "tal que k < j ≤ n e ak,j != 0" */
        if ( pivo == 0 ){
            if(swap_coluna(i, matriz, resposta, tamanho))
                i--; /*Troca efetuada, repetir linha*/
            else{
                zera_coluna(matriz, i, tamanho);    /*Variavel livre, zerar coluna*/
            }
             continue;
        }
        
        /*Percorre linhas */
        for (k = 0; k<tamanho; k++) {
            /* Exceto a linha do pivo */
            if (k == i) continue;
            
            /* Valor a ser zerado na linha */
            double valorAlvo = matriz[k][i];
            
            /* Linha Auxiliar*/
            double *auxLinha = malloc( sizeof( double ) * ( tamanho + 1 ) );
            if (auxLinha == NULL) return -1;
            
            /* Preenche linha aux com valores a serem somados com os da linha atual*/
            for (j = 0;j<tamanho+1;j++)
                auxLinha[j] = matriz[i][j] * (valorAlvo/pivo);
            
#if VERBOSO
            printf("\nOperacao : Linha %d - (",k);
            for (j = 0;j<tamanho+1;j++)
                printf("%.2lf ",auxLinha[j]);
            printf(")\n");
#endif
            
            /* Subtrai auxLinha do valor da linha corrente da matriz*/
            for (j = 0;j<tamanho+1;j++)
                matriz[k][j] -= auxLinha[j];
                
            free(auxLinha);
            
#if VERBOSO
            printf("Matriz\n");
            exibe_matriz(matriz, tamanho);
#endif
        }
    }
    
    /*Calcula resultado da matriz*/
    return resultado_jordan(matriz, resposta, tamanho);
}


/*-1 Erro de Memoria, 0 arquivo nao encontrado, 1 leitura feita com sucesso*/
int carrega_matriz_do_arquivo(double **matriz, double **resposta, int *tamanho)
{
    int i,j;
    
    FILE * pFile;
    pFile = fopen (FILENAME,"r");
    
    /*Arquivo nao encontrado ou erro de permissao*/
    if(pFile == NULL)
        return 0;
    
    /*Le tamanho do arquivo*/
    fscanf (pFile, "%d", tamanho);
    
    /*Cria matriz ( Tamanho x Tamanho )*/
    *matriz = malloc(sizeof(double * ) * (*tamanho));
    if ( matriz == NULL )
        return -1;
    
    for ( i = 0 ; i < (*tamanho) ; i++ ){
        matriz[i]  = (double *) malloc(sizeof(double) * ((*tamanho)+1));
        if ( matriz[i] == NULL )
            return -1;
    }

    
    /*Le matriz do arquivo, i = linha, j = coluna*/
    for ( i = 0 ; i < (*tamanho) ; i++ ){
        for ( j = 0 ; j < ((*tamanho)+1) ; j++ ){
            fscanf (pFile, "%lf", &matriz[i][j]);
        }
    }
    
    return carrega_array_resposta(resposta, *tamanho);
}


/*-1 Erro de Memoria, 1 leitura feita com sucesso*/
int carrega_matriz_do_teclado(double **matriz, double **resposta, int *tamanho)
{
    int i,j;
    
    scanf("%d",tamanho);
    
    /*Cria matriz ( Tamanho x Tamanho )*/
    *matriz = malloc(sizeof(double * ) * (*tamanho));
    if ( matriz == NULL )
        return -1;
    
    /*Cria colunas*/
    for ( i = 0 ; i < (*tamanho) ; i++ ){
        matriz[i]  = (double *) malloc( sizeof(double) * ((*tamanho)+1) );
        if ( matriz[i] == NULL )
            return -1;
    }

    /*Le matriz do teclado, i = linha, j = coluna*/
    for ( i = 0 ; i<(*tamanho) ; i++ ){
        for ( j = 0 ; j<(*tamanho)+1 ; j++ ){
            scanf("%lf",&matriz[i][j]);
        }
    }
    return carrega_array_resposta(resposta, *tamanho);
}


int carrega_array_resposta(double **resposta, int tamanho)
{
    int i;
    *resposta =  malloc( sizeof(double) * tamanho );
    
    if (resposta == NULL)
        return -1;

    /* Inicializa indice com posicao corresponde da matrix, para os swaps de coluna, isso simplica a anotacao */
    for (i = 0;i<tamanho;i++)
        (*resposta)[i] = (int)i;
    
    return 1;
    
}


int exibe_double_para_base(double valor,int base){
    int i;
    long int parteInteira;
    double parteFracionaria;
    
     /*Pilha para exibir valores, (maior numero de digitos se for binario), por isso sizeof * 8*/
    int *pilha = malloc((sizeof(valor)*8) + 1);
   
    if (pilha == NULL)
        return -1;
    
    
    /*Dividindo partes*/
    parteInteira = (long int) valor;
    parteFracionaria = valor - parteInteira;
    
    
    i = 0;
    /* Convertendo parte inteira */
    while (parteInteira>0) {
        pilha[i++] =  (int) (parteInteira % base);
        parteInteira /= base;
    }
    
    /* Exibindo valores em ordem inversa */
    for(--i ; i>=0 ; i--)
        printf("%X",pilha[i]);

    printf(",");
    
    i=0;
    /* Convertendo parte fracionaria */
    while (parteFracionaria >= ERRO_0 && i < 20) {
        printf("%X", (int) fabs( parteFracionaria * base));
        parteFracionaria *= base;
        parteFracionaria -= (int)parteFracionaria;
        i++;
    }
    
    printf("\n");
    
    free(pilha);
    return 1;
}

void converte_e_exibe_double_para_todas_bases(double valor){
    exibe_double_para_base(valor,2);
    exibe_double_para_base(valor,8);
    exibe_double_para_base(valor,16);
    printf("\n");

}

int main(int argc, const char * argv[])
{
    int i;
    double *matriz = NULL;
    double *resposta = NULL;
    int tamanho;
    
    char parametro;
    double valorConverter;
    
    do{
        printf("Digite C para Conversao e S para resolucao do Sistema Linear, F para Finalinzar\n");
        scanf("%c", &parametro);
        while ( getchar() != '\n' ); /*Para evitar erro na leitura do bugger pelo \n*/
        switch (parametro) {
            case 'C':
                printf("\n** DIGITE VALOR A SER CONVERTIDO ** \n");
                scanf("%lf", &valorConverter);
                converte_e_exibe_double_para_todas_bases(valorConverter);
                printf("\n");
                break;
            case 'S':
#if INPUT
                carrega_matriz_do_teclado(&matriz, &resposta ,&tamanho);
#else
                carrega_matriz_do_arquivo(&matriz, &resposta ,&tamanho);
#endif
                printf("\n** Matriz ** \n");
                exibe_matriz(&matriz, tamanho);
                
                if(metodo_jordan(&matriz, resposta, tamanho)){
                    for (i = 0;i<tamanho;i++)
                        printf("Var x%d = %.2lf\n",i+1,resposta[i]);
                }else{
                    printf("\n** Sistema Inconpativel ** \n");
                }
                break;
            case 'F':
                break;
            default:
                printf("Comando Invalido\n\n");
                break;
        }
    }while (parametro != 'F');
    
    /*Desalocando variaveis*/
    if(resposta!= NULL)
        free(resposta);
    if(matriz!= NULL)
        free(matriz);
    return 0;
}
