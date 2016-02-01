/*
 * IFCE - Instituto Federal do Ceará
 * Engenharia De Computação - 2013.2
 * Sistemas Embarcados
 * 
 * Autores:
 *   Ulysses Rocha e Marcelo Silveira
 * Data desta versão:
 *   12/05/2014
 * 
 * Código adaptado da plataforma intel x86 para PIC 18.
 * Baseado na implementação original de Ulysses Rocha.
 *
 * Código implementado originalmente durante o estagio realizando em Julho de 2013 
 * com o tema ?Exact Algorithms for the Cable-Trench Problem? no
 * Departamento de Engenharia Informática da Universidade de Coimbra,
 * sob a supervisão do Professor Dr. Carlos M. Fonseca.
 * 
 * Bibliografia de apoio consultada:
 *  Francis J. Vasko, Robert S. Barbieri, Brian Q. Rieksts, Kenneth L. Reitmeyer, and Kenneth L.
 *  Stott, Jr.. 2002. The cable trench problem: combining the shortest path and minimum spanning tree
 *  problems. Comput. Oper. Res. 29, 5 (April 2002), 441-458. doi: 10.1016/S0305-0548(00)00083-6
 */

#include <p18f4550.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <usart.h>

#pragma config FOSC     = HS // (8 MHz)
#pragma config PLLDIV   = 1
#pragma config IESO     = OFF
#pragma config PWRT     = OFF
#pragma config BORV     = 3
#pragma config WDT      = OFF
#pragma config WDTPS    = 32768
#pragma config MCLRE    = OFF
#pragma config LPT1OSC  = OFF
#pragma config PBADEN   = OFF
#pragma config STVREN   = ON
#pragma config LVP      = OFF

#define MAXI 12

#define MIN(a,b) (((a)<(b))?(a):(b))
#define MAX(a,b) (((a)>(b))?(a):(b))

void config();
void iniciarUART();
double GreedTCP();
void carregarTest();
void printDouble(double, unsigned char);

const far rom char msg1[] = "\n\n\rProblemas cabos e trincheiras.\r\nQual caso de teste(1-6): ";
ram int numberOfPoints;
ram double cableCost;
ram double trenchCost;

//http://www.xargs.com/pic/c18large.html
//#pragma udata large udata ... #pragma udata
#pragma udata gpr2
ram double values[MAXI][MAXI];
#pragma udata

/*Seta parametros iniciais do PIC
 * Entrada: Caso de teste a ser carregado em memoria
 * Saida : Variavel global Values[][] alterada com o caso de teste selecionado
 */
void carregaTest(int numbTest) {
  
    switch (numbTest) {
        case 1:
            cableCost = 0.200;
            trenchCost = 1.8000;
            numberOfPoints = 6;
            values [1][0] = 365.382043;
            values [2][1] = 323.184390;
            values [2][0] = 687.578311;
            values [3][2] = 241.477182;
            values [3][1] = 528.901385;
            values [3][0] = 889.977850;
            values [4][3] = 791.224934;
            values [4][2] = 557.127418;
            values [4][1] = 475.768275;
            values [4][0] = 587.904416;
            values [5][4] = 638.705067;
            values [5][3] = 298.612448;
            values [5][2] = 279.720689;
            values [5][1] = 585.079211;
            values [5][0] = 937.963853;
            break;
        case 2:
            cableCost = 0.600;
            trenchCost = 1.1000;
            numberOfPoints = 7;
            values [1][0] = 365.382043;
            values [2][1] = 323.184390;
            values [2][0] = 655.578311;
            values [3][2] = 241.477182;
            values [3][1] = 528.901385;
            values [3][0] = 889.977850;
            values [4][3] = 111.224934;
            values [4][2] = 123.127418;
            values [4][1] = 475.768275;
            values [4][0] = 587.904416;
            values [5][4] = 638.705067;
            values [5][3] = 298.612448;
            values [5][2] = 25.720689;
            values [5][1] = 585.079211;
            values [5][0] = 937.963853;
            values [6][5] = 157.577185;
            values [6][4] = 312.557245;
            values [6][3] = 211.511285;
            values [6][2] = 77.2712115;
            values [6][1] = 7.46521345;
            values [6][0] = 110.657424;
            break;
        case 3:
            cableCost = 1.2000;
            trenchCost = 2.8000;
            numberOfPoints = 5;
            values [1][0] = 5.654;
            values [2][1] = 3.874;
            values [2][0] = 7.112;
            values [3][2] = 1.365;
            values [3][1] = 8.966;
            values [3][0] = 9.100;
            values [4][3] = 1.306;
            values [4][2] = 7.770;
            values [4][1] = 5.115;
            values [4][0] = 7.193;
            break;
        case 4:
            cableCost = 0.2000;
            trenchCost = 2.8000;
            numberOfPoints = 4;
            values [1][0] = 125.654;
            values [2][1] = 431.874;
            values [2][0] = 187.112;
            values [3][2] = 91.365;
            values [3][1] = 628.966;
            values [3][0] = 90.110;
            break;
        case 5:
            cableCost = 0.2000;
            trenchCost = 0.8000;
            numberOfPoints = 4;
            values [1][0] = 125.654;
            values [2][1] = 431.874;
            values [2][0] = 187.112;
            values [3][2] = 91.365;
            values [3][1] = 628.966;
            values [3][0] = 90.110;
            break;

        case 6:
            cableCost = 0.1000;
            trenchCost = 1.9000;
            numberOfPoints = 8;
            values [1][0]= 367.201337;
            values [2][1]= 776.571605;
            values [2][0]= 636.924035;
            values [3][2]= 712.092521;
            values [3][1]= 709.298790;
            values [3][0]= 910.077037;
            values [4][3]= 925.176877;
            values [4][2]= 341.121834;
            values [4][1]= 685.376944;
            values [4][0]= 401.910133;
            values [5][4]= 480.212403;
            values [5][3]= 590.437280;
            values [5][2]= 151.941641;
            values [5][1]= 790.462984;
            values [5][0]= 720.069644;
            values [6][5]= 225.268967;
            values [6][4]= 518.246865;
            values [6][3]= 799.254876;
            values [6][2]= 216.596851;
            values [6][1]= 985.450841;
            values [6][0]= 850.750877;
            values [7][6]= 510.982889;
            values [7][5]= 286.393179;
            values [7][4]= 548.014376;
            values [7][3]= 378.319749;
            values [7][2]= 361.736431;
            values [7][1]= 554.048662;
            values [7][0]= 606.249770;   
            break;

            case 7:
            cableCost = 0.900;
            trenchCost = 1.200;
            numberOfPoints = 12;
            values [1][0]= 367.201337;
            values [2][1]= 776.571605;
            values [2][0]= 636.924035;
            values [3][2]= 712.092521;
            values [3][1]= 709.298790;
            values [3][0]= 910.077037;
            values [4][3]= 925.176877;
            values [4][2]= 341.121834;
            values [4][1]= 685.376944;
            values [4][0]= 401.910133;
            values [5][4]= 480.212403;
            values [5][3]= 590.437280;
            values [5][2]= 151.941641;
            values [5][1]= 790.462984;
            values [5][0]= 720.069644;
            values [6][5]= 225.268967;
            values [6][4]= 518.246865;
            values [6][3]= 799.254876;
            values [6][2]= 216.596851;
            values [6][1]= 985.450841;
            values [6][0]= 850.750877;
            values [7][6]= 510.982889;
            values [7][5]= 286.393179;
            values [7][4]= 548.014376;
            values [7][3]= 378.319749;
            values [7][2]= 361.736431;
            values [7][1]= 554.048662;
            values [7][0]= 606.249770;
            values [8][7]= 657.961365;
            values [8][6]= 724.438107;
            values [8][5]= 660.953945;
            values [8][4]= 208.376247;
            values [8][3]= 1017.374378;
            values [8][2]= 535.339094;
            values [8][1]= 612.262711;
            values [8][0]= 261.396324;
            values [9][8]= 275.042226;
            values [9][7]= 814.271005;
            values [9][6]= 985.272659;
            values [9][5]= 890.187935;
            values [9][4]= 479.538689;
            values [9][3]= 1128.805904;
            values [9][2]= 783.298674;
            values [9][1]= 548.885522;
            values [9][0]= 219.135441;
            values [10][9]= 540.256149;
            values [10][8]= 270.772454;
            values [10][7]= 519.141400;
            values [10][6]= 455.521022;
            values [10][5]= 425.906286;
            values [10][4]= 62.792814;
            values [10][3]= 897.460238;
            values [10][2]= 282.722834;
            values [10][1]= 710.903860;
            values [10][0]= 450.080762;
            values [11][10]= 539.217154;
            values [11][9]= 649.999529;
            values [11][8]= 570.023882;
            values [11][7]= 242.407745;
            values [11][6]= 701.193760;
            values [11][5]= 491.472983;
            values [11][4]= 540.619554;
            values [11][3]= 479.321143;
            values [11][2]= 507.720432;
            values [11][1]= 311.783268;
            values [11][0]= 431.000867;
            values [12][11]= 585.447669;
            values [12][10]= 481.313444;
            values [12][9]= 70.265505;
            values [12][8]= 224.839333;
            values [12][7]= 744.741595;
            values [12][6]= 921.130715;
            values [12][5]= 821.255346;
            values [12][4]= 422.116371;
            values [12][3]= 1063.286149;
            values [12][2]= 716.861524;
            values [12][1]= 507.196996;
            values [12][0]= 157.322886;
            break;

            case 8:
            cableCost = 2.900;
            trenchCost = 1.200;
            numberOfPoints = 12;
            values [1][0]= 67.201337;
            values [2][1]= 76.571605;
            values [2][0]= 36.924035;
            values [3][2]= 12.092521;
            values [3][1]= 09.298790;
            values [3][0]= 10.077037;
            values [4][3]= 25.176877;
            values [4][2]= 41.121834;
            values [4][1]= 85.376944;
            values [4][0]= 01.910133;
            values [5][4]= 80.212403;
            values [5][3]= 90.437280;
            values [5][2]= 51.941641;
            values [5][1]= 90.462984;
            values [5][0]= 20.069644;
            values [6][5]= 25.268967;
            values [6][4]= 18.246865;
            values [6][3]= 99.254876;
            values [6][2]= 16.596851;
            values [6][1]= 85.450841;
            values [6][0]= 50.750877;
            values [7][6]= 10.982889;
            values [7][5]= 86.393179;
            values [7][4]= 48.014376;
            values [7][3]= 78.319749;
            values [7][2]= 61.736431;
            values [7][1]= 54.048662;
            values [7][0]= 06.249770;
            values [8][7]= 57.961365;
            values [8][6]= 24.438107;
            values [8][5]= 60.953945;
            values [8][4]= 08.376247;
            values [8][3]= 017.374378;
            values [8][2]= 35.339094;
            values [8][1]= 12.262711;
            values [8][0]= 61.396324;
            values [9][8]= 75.042226;
            values [9][7]= 14.271005;
            values [9][6]= 85.272659;
            values [9][5]= 90.187935;
            values [9][4]= 79.538689;
            values [9][3]= 128.805904;
            values [9][2]= 83.298674;
            values [9][1]= 48.885522;
            values [9][0]= 19.135441;
            values [10][9]= 40.256149;
            values [10][8]= 70.772454;
            values [10][7]= 19.141400;
            values [10][6]= 55.521022;
            values [10][5]= 25.906286;
            values [10][4]= 2.792814;
            values [10][3]= 97.460238;
            values [10][2]= 82.722834;
            values [10][1]= 10.903860;
            values [10][0]= 50.080762;
            values [11][10]= 39.217154;
            values [11][9]= 49.999529;
            values [11][8]= 70.023882;
            values [11][7]= 42.407745;
            values [11][6]= 01.193760;
            values [11][5]= 91.472983;
            values [11][4]= 40.619554;
            values [11][3]= 79.321143;
            values [11][2]= 07.720432;
            values [11][1]= 11.783268;
            values [11][0]= 31.000867;
            values [12][11]= 85.447669;
            values [12][10]= 81.313444;
            values [12][9]= 1.265505;
            values [12][8]= 24.839333;
            values [12][7]= 44.741595;
            values [12][6]= 21.130715;
            values [12][5]= 21.255346;
            values [12][4]= 22.116371;
            values [12][3]= 063.286149;
            values [12][2]= 16.861524;
            values [12][1]= 07.196996;
            values [12][0]= 57.322886;
            break;
        case 9:
            cableCost = 2.0;
            trenchCost = 3.0;
            numberOfPoints = 10;
            values [1][0]= 67.0;
            values [2][1]= 76.0;
            values [2][0]= 36.0;
            values [3][2]= 12.0;
            values [3][1]= 09.0;
            values [3][0]= 10.0;
            values [4][3]= 25.0;
            values [4][2]= 41.0;
            values [4][1]= 85.0;
            values [4][0]= 01.0;
            values [5][4]= 80.0;
            values [5][3]= 90.0;
            values [5][2]= 51.0;
            values [5][1]= 90.0;
            values [5][0]= 20.0;
            values [6][5]= 25.0;
            values [6][4]= 18.0;
            values [6][3]= 99.0;
            values [6][2]= 16.0;
            values [6][1]= 85.0;
            values [6][0]= 50.0;
            values [7][6]= 10.0;
            values [7][5]= 86.0;
            values [7][4]= 48.0;
            values [7][3]= 78.0;
            values [7][2]= 61.0;
            values [7][1]= 54.0;
            values [7][0]= 06.0;
            values [8][7]= 57.0;
            values [8][6]= 24.0;
            values [8][5]= 60.0;
            values [8][4]= 08.0;
            values [8][3]= 017.0;
            values [8][2]= 35.0;
            values [8][1]= 12.0;
            values [8][0]= 61.0;
            values [9][8]= 75.0;
            values [9][7]= 14.0;
            values [9][6]= 85.0;
            values [9][5]= 90.0;
            values [9][4]= 79.0;
            values [9][3]= 128.0;
            values [9][2]= 83.0;
            values [9][1]= 48.0;
            values [9][0]= 19.0;
            break;
        default:
            printf((rom far char*) "ERRO\n");
    }



}

/*Seta parametros iniciais do PIC
 * Entrada : Nenhuma
 * Saida : Nenhuma
 */
void config() {
    LATA = 0;
    LATB = 0;
    LATC = 0;
    LATD = 0;
    LATE = 0;

    TRISA = 0;
    TRISB = 0;
    TRISC = 0;
    TRISD = 0;
    TRISE = 0;
}

/* Asynchronous mode, high speed:
 *  FOSC / (16 * (spbrg + 1))
 * Asynchronous mode, low speed:
 *  FOSC / (64 * (spbrg + 1))
 */
/*
 * Configura UART
 */
void iniciarUART() {
    OpenUSART(USART_TX_INT_OFF & USART_RX_INT_OFF & USART_ASYNCH_MODE &
            USART_EIGHT_BIT & USART_CONT_RX & USART_BRGH_LOW, 50);
}

/*
 * Função para exibir valores do tipo double
 */
void printDouble(double valor, unsigned char casasDecimais) {
    unsigned long parteInteira = 0;
    unsigned long parteFracionaria = 0;
    unsigned long mult = 1;

    if (valor < 0) {
        printf((const far rom char *) "-");
        valor *= -1;
        while (BusyUSART());
    }

    parteInteira = (unsigned long) valor;
    printf((const far rom char *) "%lu", parteInteira);
    valor -= parteInteira;

    if (casasDecimais > 0) {
        if (casasDecimais > 8)
            mult = 1000000000;
        else
            while (casasDecimais > 0) {
                mult *= 10;
                casasDecimais--;
            }

        parteFracionaria = (unsigned long) (valor * mult);
        while (BusyUSART());
        printf((const far rom char *) ".%lu", parteFracionaria);
    }
    while (BusyUSART());
    ;
}


/*
* Descrição do algoritmo heuristico:
* Abordagem Heurística Buscando convergir rapidamente a um valor aproximado da resposta,
* foi implementada uma estratégia que sempre seguia pelo caminho de menor custo relativo.
* A estratégia é semelhante a do algoritmo de Dijkstra para caminho mais curto entre dois nós em um grafo,
* calcula-se a partir da origem o custo dos cabos e das trincheiras a todos os nós,
* a aresta do grafo com menor custo total será adicionada a solução,
* contabiliza-se a partir da nova aresta o custo do nó aos outros vértices,
* caso já haja algum custo registrado para o vértice de destino, prevalecerá o menor,
* assim segue até que todos os nós estejam conectados a solução.
 *
 * Entrada: Nenhuma (Utiliza matriz variavel global values[][] sem efeitos colaterais)
 * Saida: Custo calculado da estrutura.
 *
*/
double GreedTCP() {
    char marked[MAXI];
    static int sourceNode[MAXI];
    static double cost[MAXI];
    static double cable[MAXI];
    int nextNode;
    double finalCost = 0;
    int auxFor;
    int numbMarked = 1;
    char valEntrada;
    //Preparando dados e inicializando variaveis.
    for (auxFor = 0; auxFor < numberOfPoints; auxFor++) {
        marked[auxFor] = 'o';
        cost[auxFor] = HUGE_VAL;
        cable[auxFor] = HUGE_VAL;
    }
    //marcando nó fonte
    marked[0] = 'x';
    cable[0] = 0;
    nextNode = 0;

    while (numbMarked < numberOfPoints) {
        double auxCost;
        double auxCableCost;
        double auxSmaller = HUGE_VAL;
        //Atualiza tabela com o novo nó que agora possui ligação com o mainframe
        for (auxFor = 0; auxFor < numberOfPoints; auxFor++) {
            if (values[nextNode][auxFor] != 0) {
                auxCost = ((values[nextNode][auxFor]*(cableCost + trenchCost)) + cable[nextNode]);
                auxCableCost = ((values[nextNode][auxFor] * cableCost) + cable[nextNode]);
                //Se o novo custo for menor que o atual, ou, igual porem com valor de cabo menor, atualize
                //debug
                if ((marked[auxFor] == 'o') && (cost[auxFor] > auxCost) || (cost[auxFor] == auxCost && cable[auxFor] < auxCableCost)) {
                    //atualizando valores, no de origem, custo, cabo, numero de vertices em contato.
                    sourceNode[auxFor] = nextNode;
                    cost[auxFor] = auxCost;
                    cable[auxFor] = auxCableCost;
                }
            }
        }

        //Procura melhor valor para fazer ligação.
        for (auxFor = 0; auxFor < numberOfPoints; auxFor++) {
            //Dentre os ainda disponiveis, escolha o com menor custo
            if ((marked[auxFor] == 'o') && (cost[auxFor] < auxSmaller)) {
                auxSmaller = cost[auxFor];
                nextNode = auxFor;
            }
        }

        marked[nextNode] = 'x';
        numbMarked++;
    }

    for (auxFor = 1; auxFor < numberOfPoints; auxFor++)
        finalCost = cost[auxFor] + finalCost;

    return finalCost;
}

void main() {
    int i, j, teste;
    double custo = 0;

    config();
    iniciarUART();

    while (1) {
        putrsUSART(msg1);
        /*Ler caso da serial e passar pra funcao carregaTest(int numbTest) de 1 a 9*/
        while (!DataRdyUSART()) {}
        teste = (int) getcUSART() - 48;
        

        for(i= 0; i< MAXI; i++)
            for(j= 0; j< MAXI; j++)
                values[i][j] = 0;

        carregaTest(teste);
       
        /*Espelhar */
        for(i=0;i<MAXI;i++){
             for(j=0;j<MAXI;j++){
                 if( values [i][j] > 0){
                      values [j][i] = values [i][j];
                 }
             }
        }
        custo = GreedTCP();

        printf((const far rom char *)"\n\rMenor Custo: ");
        printDouble(custo, 3);
        printf((const far rom char *)"\n\r");
    }
}
