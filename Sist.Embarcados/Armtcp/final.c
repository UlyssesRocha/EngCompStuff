/* IFCE - Instituto Federal do Ceará
 * Engenharia De Computação - 2013.2
 * Sistemas Embarcados
 *
 * Autores:
 *   Ulysses Rocha e Marcelo Silveira
 * Data desta versão:
 *   12/05/2014
 *
 * Código adaptado da plataforma intel x86 para ARM 9.
 * Baseado na implementação original de Ulysses Rocha.
 *
 * Código implementado originalmente durante o estagio realizando em Julho de 2013
 * com o tema "Exact Algorithms for the Cable-Trench Problem" no
 * Departamento de Engenharia Informática da Universidade de Coimbra,
 * sob a supervisão do Professor Dr. Carlos M. Fonseca.
 *
 * Bibliografia de apoio consultada:
 *  Francis J. Vasko, Robert S. Barbieri, Brian Q. Rieksts, Kenneth L. Reitmeyer, and Kenneth L.
 *  Stott, Jr.. 2002. The cable trench problem: combining the shortest path and minimum spanning tree
 *  problems. Comput. Oper. Res. 29, 5 (April 2002), 441-458. doi: 10.1016/S0305-0548(00)00083-6 */

#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#define MAXI 25
#define INFINITY 99999
#define MIN(a,b) (((a)<(b))?(a):(b))
#define MAX(a,b) (((a)>(b))?(a):(b))
#define DEBUG 1

float values[MAXI][MAXI];
float bestCostDBB=INFINITY;
short int numberOfPoints;
float cableCost, trenchCost;
/*
Funcao principal
source  = Nó de origem da chamada
target = Nó de destino da chamada
numberOfMarkeds = Numero de nós já marcados
localBound = Bound do menor custo restante atual
boundCable[MAXI] = Tabela com os custos minimos dos cabos
boundTrench[MAXI] = Tabela com os custos minimos das trincheiras
bestsCost[MAXI] = Tabela com os custos minimos para um nó
bestsCable[MAXI] = Tabela com os custos minimos para um nó
actualCost = Custo atual da arvore
numberOfPoints = numero de pontos total do grafo
cost[MAXI][MAXI] = Tabela com os custos atual dos nós do grafo
cable[MAXI][MAXI] = Tabela com custo dos cabos
values[MAXI][MAXI] = Tabela com o grafo
marked[MAXI] = vetor indicando elementos já visitados
markedSource[MAXI] = Nó que o "marcou" (para rastrar origem)
cableCost = Peso do cabo
trenchCost = Peso da trincheira
*/

void TCP(short int source, short int target, short int numberOfMarkeds, float actualCost, float cost[MAXI][MAXI], float cable[MAXI][MAXI], char marked[MAXI]){
	short int auxFor, auxFor2;
	float minValue=INFINITY;
	short int nextSource, nextTarget;
	float auxTempCost,auxTempValue;

	/*Cortes*/
	if (numberOfMarkeds>numberOfPoints && actualCost>bestCostDBB)
		return;
	/*Atualiza melhor custo*/
	if(numberOfMarkeds==numberOfPoints){
#if DEBUG
		if(bestCostDBB>actualCost)
			printf("Nova Melhor Resposta %f \n", actualCost);
#endif
		bestCostDBB=MIN(bestCostDBB,actualCost);
		return;
	}
	/*Atualizando linha da matriz de valores do TCP
      Tabela principal de custos atuais.
	*/
	for(auxFor=0;auxFor<numberOfPoints;auxFor++){
		if(values[target][auxFor]!=0 ){
			cost[target][auxFor]=(values[target][auxFor]*(trenchCost+cableCost))+cable[source][target];
			cable[target][auxFor]=(values[target][auxFor]*(cableCost))+cable[source][target];
		}
	}

	/*Procura proximo elemento a ser visitado*/
	for(auxFor=0;auxFor<numberOfPoints;auxFor++){
		if(marked[auxFor]=='x'){
			for(auxFor2=0;auxFor2<numberOfPoints;auxFor2++){
				if(cost[auxFor][auxFor2]<minValue && marked[auxFor2]=='o' && cost[auxFor][auxFor2]>0 && cost[auxFor][auxFor2]!=INFINITY ){
					minValue=cost[auxFor][auxFor2];
					nextSource=auxFor;
					nextTarget=auxFor2;
				}
			}
		}
	}
    /*Se o melhor caminho encontrado for um com custo infinito, portanto essa solução não é desejavel, logo valor descartado*/
	if(minValue==INFINITY)
		return;

	/*Seleciona novo elemento*/
	actualCost+=minValue;
	numberOfMarkeds++;
	marked[nextTarget]='x';

	TCP(nextSource,nextTarget, numberOfMarkeds, actualCost,cost, cable, marked);

   	/*Salva variaveis de custo e valores, para serem atualizados por pesos "infinitos"*/
	auxTempCost=cost[nextSource][nextTarget];
	auxTempValue=values[nextSource][nextTarget];

	/*Deseleciona aresta e custo infinito*/
	cost[nextSource][nextTarget]=INFINITY;
	cost[nextTarget][nextSource]=INFINITY;

	values[nextSource][nextTarget]=INFINITY;
	values[nextTarget][nextSource]=INFINITY;

	actualCost-=minValue;
	marked[nextTarget]='o';
	numberOfMarkeds--;

	/*Verifica se proxima solução é viavel*/
	/*Corte branch and bound*/
	TCP(source,target, numberOfMarkeds, actualCost, cost, cable, marked);

	/*Retorna arestas ao valor original*/
	cost[nextSource][nextTarget]=cost[nextTarget][nextSource]=auxTempCost;
	values[nextTarget][nextSource]=values[nextSource][nextTarget]=auxTempValue;
}


int main(){

	short int numberOfEdges;
	float cost[MAXI][MAXI], cable[MAXI][MAXI];
	char marked[MAXI];
	short int auxValueP1, auxValueP2;
	float auxCost;
	short int auxFor, auxFor2;

	scanf("%f %f",&cableCost,&trenchCost);
	scanf("%hd %hd",&numberOfPoints,&numberOfEdges);

	//zerando variaveis cm valor "infinito"
	for(auxFor=0;auxFor<numberOfPoints;auxFor++){
		for(auxFor2=0;auxFor2<numberOfPoints;auxFor2++){
			values[auxFor][auxFor2]=0;
			cost[auxFor][auxFor2]=0;
			cable[auxFor][auxFor2]=0;
		}
		marked[auxFor]='o';
	}

	for(auxFor=0;auxFor<numberOfEdges;auxFor++){
		scanf("%hd %hd %f",&auxValueP1,&auxValueP2,&auxCost);
		values[auxValueP1][auxValueP2]=auxCost;
		values[auxValueP2][auxValueP1]=auxCost;
	}

    marked[0]='x';
	TCP(0, 0, 1, 0, cost, cable, marked);
	printf("\n%.3f \n\n",bestCostDBB);
	system("pause");
	return 0;

}
