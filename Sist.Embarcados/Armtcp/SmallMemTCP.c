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
#include <windows.h>
#include <math.h>

#define MAXI 20
#define INFINITY 99999
#define MIN(a,b) (((a)<(b))?(a):(b))
#define MAX(a,b) (((a)>(b))?(a):(b))
#define DEBUG 1

float values[MAXI][MAXI];
float cost[MAXI][MAXI], cable[MAXI][MAXI];
float bestCostDBB = INFINITY;
short int numberOfPoints;
float cableCost, trenchCost;
char marked[MAXI];
short int auxFor, auxFor2;

/*Funcao principal - Força Bruta
Calcula-se a partir da origem o custo a todos os nós,
ele computa o custo da rota utilizando a aresta de menor custo encontrado,
e também não utilizando esta aresta, buscando uma rota alternativa pelo novo melhor caminho,
isso repete-se para todos as arestas, até que ele tenha testado todas as possibilidades.
Com isso, ao fim da execução todos os caminhos possíveis serão explorados, chegando assim a solução da melhor rota.
source  = Nó de origem da chamada
target = Nó de destino da chamada
numberOfMarkeds = Numero de nós já marcados
actualCost = Custo atual da arvore
marked[MAXI] = vetor indicando elementos já visitados*/
void TCP(short int source, short int target, short int numberOfMarkeds, float actualCost){
	float minValue = INFINITY;
	short int nextSource, nextTarget;
	float auxTempCost, auxTempValue;

	/*Cortes e backtrack*/
	if (numberOfMarkeds>numberOfPoints && actualCost>bestCostDBB)
		return;

	/*Atualiza melhor custo*/
	if (numberOfMarkeds == numberOfPoints){
#if DEBUG
		if (bestCostDBB>actualCost)
			printf("Nova Melhor Resposta %f \n", actualCost);
#endif
		bestCostDBB = MIN(bestCostDBB, actualCost);
		return;
	}

	/*Atualizando linha da matriz de valores do TCP
	Tabela principal de custos atuais. O(N)
	*/
	for (auxFor = 0; auxFor<numberOfPoints; auxFor++){
		if (values[target][auxFor] != 0){
			cost[target][auxFor] = (values[target][auxFor] * (trenchCost + cableCost)) + cable[source][target];
			cable[target][auxFor] = (values[target][auxFor] * (cableCost)) + cable[source][target];
		}
	}

	/*Procura proximo elemento a ser visitado O(N^2) */
	for (auxFor = 0; auxFor<numberOfPoints; auxFor++){
		if (marked[auxFor] == 'x'){
			for (auxFor2 = 0; auxFor2<numberOfPoints; auxFor2++){
				if (cost[auxFor][auxFor2]<minValue && marked[auxFor2] == 'o' && cost[auxFor][auxFor2]>0 && cost[auxFor][auxFor2] != INFINITY){
					minValue = cost[auxFor][auxFor2];
					nextSource = auxFor;
					nextTarget = auxFor2;
				}
			}
		}
	}

	/*Se o melhor caminho encontrado for um com custo infinito, essa solução não é desejavel, logo valor descartado*/
	if (minValue == INFINITY)
		return;

	/*Seleciona novo elemento*/
	actualCost += minValue;
	numberOfMarkeds++;
	marked[nextTarget] = 'x';

	TCP(nextSource, nextTarget, numberOfMarkeds, actualCost);

	/*Salva variaveis de custo e valores, para serem atualizados por pesos "infinitos"*/
	auxTempCost = cost[nextSource][nextTarget];
	auxTempValue = values[nextSource][nextTarget];

	/*Deseleciona aresta e seta custo infinito*/
	cost[nextSource][nextTarget] = INFINITY;
	cost[nextTarget][nextSource] = INFINITY;

	values[nextSource][nextTarget] = INFINITY;
	values[nextTarget][nextSource] = INFINITY;

	actualCost -= minValue;
	marked[nextTarget] = 'o';
	numberOfMarkeds--;

	/*Verifica se proxima solução é viavel*/
	TCP(source, target, numberOfMarkeds, actualCost);

	/*Retorna arestas ao valor original*/
	cost[nextSource][nextTarget] = cost[nextTarget][nextSource] = auxTempCost;
	values[nextTarget][nextSource] = values[nextSource][nextTarget] = auxTempValue;
}

void carregaTeste(int n){
	//zerando variaveis
	for (auxFor = 0; auxFor<20; auxFor++){
		for (auxFor2 = 0; auxFor2<20; auxFor2++){
			values[auxFor][auxFor2] = 0;
			cost[auxFor][auxFor2] = 0;
			cable[auxFor][auxFor2] = 0;
		}
		marked[auxFor] = 'o';
	}
	bestCostDBB = INFINITY;

	switch (n) {
	case 1:
		cableCost = 0.200;
		trenchCost = 1.8000;
		numberOfPoints = 6;
		values[1][0] = 365.382043;
		values[2][1] = 323.184390;
		values[2][0] = 687.578311;
		values[3][2] = 241.477182;
		values[3][1] = 528.901385;
		values[3][0] = 889.977850;
		values[4][3] = 791.224934;
		values[4][2] = 557.127418;
		values[4][1] = 475.768275;
		values[4][0] = 587.904416;
		values[5][4] = 638.705067;
		values[5][3] = 298.612448;
		values[5][2] = 279.720689;
		values[5][1] = 585.079211;
		values[5][0] = 937.963853;
		break;
	case 2:
		cableCost = 0.300;
		trenchCost = 1.7000;
		numberOfPoints = 9;
		values[1][0] = 484.373629;
		values[2][1] = 414.130529;
		values[2][0] = 894.007277;
		values[3][2] = 732.669466;
		values[3][1] = 538.708381;
		values[3][0] = 579.789105;
		values[4][3] = 170.730745;
		values[4][2] = 828.215914;
		values[4][1] = 694.658179;
		values[4][0] = 739.329012;
		values[5][4] = 288.387889;
		values[5][3] = 264.503686;
		values[5][2] = 552.106624;
		values[5][1] = 524.753134;
		values[5][0] = 759.581404;
		values[6][5] = 838.059457;
		values[6][4] = 919.388466;
		values[6][3] = 748.659510;
		values[6][2] = 758.970140;
		values[6][1] = 370.113147;
		values[6][0] = 316.019256;
		values[7][6] = 497.368298;
		values[7][5] = 362.920875;
		values[7][4] = 430.064498;
		values[7][3] = 261.998926;
		values[7][2] = 591.744413;
		values[7][1] = 292.423090;
		values[7][0] = 401.518477;
		values[8][7] = 587.848285;
		values[8][6] = 117.453208;
		values[8][5] = 939.107769;
		values[8][4] = 997.825122;
		values[8][3] = 828.031200;
		values[8][2] = 875.568689;
		values[8][1] = 486.694388;
		values[8][0] = 322.511626;
		break;
	case 3:
		cableCost = 1.000;
		trenchCost = 4.000;
		numberOfPoints = 10;
		values[1][0] = 317.895516;
		values[2][1] = 999.504387;
		values[2][0] = 908.132046;
		values[3][2] = 675.919209;
		values[3][1] = 565.136164;
		values[3][0] = 303.697724;
		values[4][3] = 270.319411;
		values[4][2] = 600.499480;
		values[4][1] = 832.297538;
		values[4][0] = 569.404818;
		values[5][4] = 242.771856;
		values[5][3] = 222.474776;
		values[5][2] = 454.391012;
		values[5][1] = 678.347667;
		values[5][0] = 489.640359;
		values[6][5] = 636.628162;
		values[6][4] = 870.929430;
		values[6][3] = 790.550926;
		values[6][2] = 463.047934;
		values[6][1] = 810.288721;
		values[6][0] = 878.038480;
		values[7][6] = 275.013539;
		values[7][5] = 408.002743;
		values[7][4] = 650.767092;
		values[7][3] = 526.392476;
		values[7][2] = 457.871616;
		values[7][1] = 582.676984;
		values[7][0] = 606.007674;
		values[8][7] = 260.597787;
		values[8][6] = 272.784745;
		values[8][5] = 660.929159;
		values[8][4] = 902.607477;
		values[8][3] = 742.437174;
		values[8][2] = 667.662205;
		values[8][1] = 585.488488;
		values[8][0] = 732.254187;
		values[9][8] = 171.437746;
		values[9][7] = 315.286804;
		values[9][6] = 432.914928;
		values[9][5] = 654.724466;
		values[9][4] = 885.287293;
		values[9][3] = 683.292155;
		values[9][2] = 768.895145;
		values[9][1] = 422.060464;
		values[9][0] = 610.281788;
		break;
	case 4:
		cableCost = 0.5000;
		trenchCost = 2.000;
		numberOfPoints = 11;
		values[1][0] = 383.312675;
		values[2][1] = 728.695925;
		values[2][0] = 816.617490;
		values[3][2] = 514.357501;
		values[3][1] = 316.594833;
		values[3][0] = 309.899933;
		values[4][3] = 505.359939;
		values[4][2] = 305.688607;
		values[4][1] = 804.322566;
		values[4][0] = 740.650374;
		values[5][4] = 410.195663;
		values[5][3] = 548.882703;
		values[5][2] = 687.052412;
		values[5][1] = 851.880151;
		values[5][0] = 598.706420;
		values[6][5] = 940.245774;
		values[6][4] = 725.709767;
		values[6][3] = 431.656361;
		values[6][2] = 522.279065;
		values[6][1] = 356.639889;
		values[6][0] = 673.430969;
		values[7][6] = 480.269561;
		values[7][5] = 875.055437;
		values[7][4] = 882.057938;
		values[7][3] = 378.027383;
		values[7][2] = 836.626229;
		values[7][1] = 124.907977;
		values[7][0] = 335.459790;
		values[8][7] = 513.007411;
		values[8][6] = 500.188551;
		values[8][5] = 442.928230;
		values[8][4] = 373.765759;
		values[8][3] = 135.412630;
		values[8][2] = 432.584016;
		values[8][1] = 449.663119;
		values[8][0] = 391.004632;
		values[9][8] = 412.406249;
		values[9][7] = 699.614438;
		values[9][6] = 889.789285;
		values[9][5] = 257.105249;
		values[9][4] = 577.148309;
		values[9][3] = 459.135684;
		values[9][2] = 789.271917;
		values[9][1] = 708.646365;
		values[9][0] = 382.572949;
		values[10][9] = 491.538680;
		values[10][8] = 405.958530;
		values[10][7] = 916.449320;
		values[10][6] = 828.589780;
		values[10][5] = 285.999984;
		values[10][4] = 142.397137;
		values[10][3] = 541.042601;
		values[10][2] = 446.882845;
		values[10][1] = 855.012837;
		values[10][0] = 724.144487;
		break;
	case 5:
		cableCost = 0.1000;
		trenchCost = 0.9000;
		numberOfPoints = 12;
		values[1][0] = 317.996196;
		values[2][1] = 239.356702;
		values[2][0] = 200.091489;
		values[3][2] = 480.051822;
		values[3][1] = 337.303629;
		values[3][0] = 388.356768;
		values[4][3] = 541.982731;
		values[4][2] = 150.605804;
		values[4][1] = 223.811834;
		values[4][0] = 344.259132;
		values[5][4] = 602.783602;
		values[5][3] = 608.934680;
		values[5][2] = 453.202088;
		values[5][1] = 615.059757;
		values[5][0] = 297.078385;
		values[6][5] = 812.485776;
		values[6][4] = 209.718545;
		values[6][3] = 659.058510;
		values[6][2] = 359.738621;
		values[6][1] = 323.957363;
		values[6][0] = 546.589425;
		values[7][6] = 586.051518;
		values[7][5] = 719.895361;
		values[7][4] = 514.242970;
		values[7][3] = 147.830774;
		values[7][2] = 494.478055;
		values[7][1] = 290.868789;
		values[7][0] = 461.642753;
		values[8][7] = 967.728968;
		values[8][6] = 407.598315;
		values[8][5] = 933.857516;
		values[8][4] = 469.157921;
		values[8][3] = 1010.295393;
		values[8][2] = 571.668950;
		values[8][1] = 680.605176;
		values[8][0] = 763.578147;
		values[9][8] = 570.742731;
		values[9][7] = 645.572119;
		values[9][6] = 480.194073;
		values[9][5] = 367.392034;
		values[9][4] = 281.012360;
		values[9][3] = 605.662858;
		values[9][2] = 169.847018;
		values[9][1] = 408.248435;
		values[9][0] = 238.387254;
		values[10][9] = 261.480143;
		values[10][8] = 784.799353;
		values[10][7] = 448.480214;
		values[10][6] = 560.150523;
		values[10][5] = 297.171176;
		values[10][4] = 360.221595;
		values[10][3] = 370.036185;
		values[10][2] = 218.754155;
		values[10][1] = 319.973585;
		values[10][0] = 23.212757;
		values[11][10] = 348.253668;
		values[11][9] = 106.260194;
		values[11][8] = 568.617245;
		values[11][7] = 750.764471;
		values[11][6] = 552.156908;
		values[11][5] = 373.951487;
		values[11][4] = 367.287276;
		values[11][3] = 705.403500;
		values[11][2] = 273.949813;
		values[11][1] = 513.204811;
		values[11][0] = 326.154632;
		break;
	case 6:
		cableCost = 0.1000;
		trenchCost = 1.9000;
		numberOfPoints = 15;
		values[1][0] = 367.201337;
		values[2][1] = 776.571605;
		values[2][0] = 636.924035;
		values[3][2] = 712.092521;
		values[3][1] = 709.298790;
		values[3][0] = 910.077037;
		values[4][3] = 925.176877;
		values[4][2] = 341.121834;
		values[4][1] = 685.376944;
		values[4][0] = 401.910133;
		values[5][4] = 480.212403;
		values[5][3] = 590.437280;
		values[5][2] = 151.941641;
		values[5][1] = 790.462984;
		values[5][0] = 720.069644;
		values[6][5] = 225.268967;
		values[6][4] = 518.246865;
		values[6][3] = 799.254876;
		values[6][2] = 216.596851;
		values[6][1] = 985.450841;
		values[6][0] = 850.750877;
		values[7][6] = 510.982889;
		values[7][5] = 286.393179;
		values[7][4] = 548.014376;
		values[7][3] = 378.319749;
		values[7][2] = 361.736431;
		values[7][1] = 554.048662;
		values[7][0] = 606.249770;
		values[8][7] = 657.961365;
		values[8][6] = 724.438107;
		values[8][5] = 660.953945;
		values[8][4] = 208.376247;
		values[8][3] = 1017.374378;
		values[8][2] = 535.339094;
		values[8][1] = 612.262711;
		values[8][0] = 261.396324;
		values[9][8] = 275.042226;
		values[9][7] = 814.271005;
		values[9][6] = 985.272659;
		values[9][5] = 890.187935;
		values[9][4] = 479.538689;
		values[9][3] = 1128.805904;
		values[9][2] = 783.298674;
		values[9][1] = 548.885522;
		values[9][0] = 219.135441;
		values[10][9] = 540.256149;
		values[10][8] = 270.772454;
		values[10][7] = 519.141400;
		values[10][6] = 455.521022;
		values[10][5] = 425.906286;
		values[10][4] = 62.792814;
		values[10][3] = 897.460238;
		values[10][2] = 282.722834;
		values[10][1] = 710.903860;
		values[10][0] = 450.080762;
		values[11][10] = 539.217154;
		values[11][9] = 649.999529;
		values[11][8] = 570.023882;
		values[11][7] = 242.407745;
		values[11][6] = 701.193760;
		values[11][5] = 491.472983;
		values[11][4] = 540.619554;
		values[11][3] = 479.321143;
		values[11][2] = 507.720432;
		values[11][1] = 311.783268;
		values[11][0] = 431.000867;
		values[12][11] = 585.447669;
		values[12][10] = 481.313444;
		values[12][9] = 70.265505;
		values[12][8] = 224.839333;
		values[12][7] = 744.741595;
		values[12][6] = 921.130715;
		values[12][5] = 821.255346;
		values[12][4] = 422.116371;
		values[12][3] = 1063.286149;
		values[12][2] = 716.861524;
		values[12][1] = 507.196996;
		values[12][0] = 157.322886;
		values[13][12] = 215.855237;
		values[13][11] = 417.209172;
		values[13][10] = 532.206777;
		values[13][9] = 261.558311;
		values[13][8] = 353.246163;
		values[13][7] = 619.700787;
		values[13][6] = 913.211290;
		values[13][5] = 766.321001;
		values[13][4] = 487.450663;
		values[13][3] = 893.368921;
		values[13][2] = 697.154727;
		values[13][1] = 291.359577;
		values[13][0] = 91.983714;
		values[14][13] = 225.612164;
		values[14][12] = 406.623080;
		values[14][11] = 191.760369;
		values[14][10] = 499.850758;
		values[14][9] = 467.281887;
		values[14][8] = 440.315285;
		values[14][7] = 408.500637;
		values[14][6] = 783.289687;
		values[14][5] = 601.968207;
		values[14][4] = 478.941451;
		values[14][3] = 668.379446;
		values[14][2] = 570.354259;
		values[14][1] = 214.062498;
		values[14][0] = 249.361995;
		break;
	}

	for (auxFor = 0; auxFor<numberOfPoints; auxFor++){
		for (auxFor2 = 0; auxFor2<numberOfPoints; auxFor2++){
			if (values[auxFor][auxFor2] != 0)
				values[auxFor2][auxFor] = values[auxFor][auxFor2];
		}
	}


}



int main(){
	//struct timeval inicio, final;
	int n;
	unsigned long long int auxTimeBegin, auxTimeEnd;

	for (n = 1; n<7; n++){
		printf("Teste numero %d\n", n);
		carregaTeste(n);
		marked[0] = 'x';
		auxTimeBegin = GetTickCount64();
		TCP(0, 0, 1, 0);
		auxTimeEnd = GetTickCount64();

		printf("\nTime BruteForce %I64u ms \n", auxTimeEnd - auxTimeBegin);
	}
	system("pause");
	return 0;

}
