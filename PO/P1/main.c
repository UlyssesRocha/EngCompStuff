
/*
Calcular os metodos 20x por tamanho da entrada, retornar media como valor do tempo que demora para o metodo ordenar a lista.
Media utilizada para remover possiveis erros/gargalos por parte de outros programas que compartilhem o processador da maquina
*/

#include "ordena.h"
#include "ext_time.h"
#include <stdio.h>
#include <stdlib.h>
#include <windows.h>

#define MAXSIZE 1000000
#define MICRO_PER_SECOND 1000000

void clearTimes(long int times[20]){
	int x;
	for(x=0;x<20;x++)
		times[x]=0;
}

long int returnAVG(long int times[20]){
	double calcAux=0;
	int x;
	for(x=0;x<20;x++)
		calcAux+=times[x];

	return ((long int) (calcAux/20));
}

long int returnAVG3(long int times[20]){
	double calcAux=0;
	int x;
	for(x=0;x<3;x++)
		calcAux+=times[x];

	return ((long int) (calcAux/3));
}


void makeTheList(long int *list, long int size, int n)
{

    long int i;
    if(n == 1)
    {
        for(i= 0; i< size; i++){
			long int x1,x2;
			x1=rand();
			x2=rand();
            list[i] = ((x1*x1)+(x2*x2)) % (10*size + 1);
		}
    }
     else if(n == 2)
    {
        list[size - 1] = 0;
        for(i= size - 2; i>= 0; i--)
            list[i] = list[i+1] + 1;
    }
     else
    {
        for(i= 0; i< size; i++)
            list[i] = i;
    }
}

int main(){

	long int times[20];
	int listSize;
	int n=5000;
	int auxFor, auxFor2;
	long int *mainList = (long int*) malloc(MAXSIZE * sizeof(long int));

	void (*func[])(long int*, long int, Avaliacao*) = {
        bolhaComFlag,
        cocktailSort,
        selecao,
        insercao,
        shellSort,
        mergeSort,
        quickSortDet,
        quickSortProb,
        countingSort,
        uBucket,
        radixSort_Count,
        radixSort_Bucket,
        heapSort
        };

    char *nomes[] = {
        "Bolha com Flag",
        "CocktailSort",
        "Selecao",
        "Insercao",
        "ShellSort",
        "MergeSort",
        "QuickSort Deterministico",
        "QuickSort Probabilistico",
        "CountingSort",
        "Bucket_Sort",
        "RadixSort_CountingSort",
        "RadixSort_BucketSort",
        "HeapSort"
        };

    long int inicio;
    long int termino;
    long int tempo;
	int funcoes;
	int tipoLista;
	int corte;
	int sofisticados[13];

	 srand(time(NULL));
	 printf("Primeira rodada todos os métodos até 50 mil elementos.\n \n ");

	for(tipoLista=0;tipoLista<3;tipoLista++){
		makeTheList(mainList, MAXSIZE, tipoLista);

		for(funcoes=0;funcoes<13;funcoes++){
			n=500000;
			
			corte=1000010;

			

			printf("Metodo %s tipo %i",nomes[funcoes],tipoLista);
			while(n<corte){
				Avaliacao avaliacao;

				long int *actualList = (long int*) malloc(n * sizeof(long int));
						
				memcpy(actualList, mainList, n * sizeof(long int));

				inicio=GetTickCount64();
				func[funcoes](actualList, n, &avaliacao);
				termino=GetTickCount64();
				times[0]=termino-inicio;

				if(!estaOrdenadoCrescente(actualList,n))
				printf("ERRO \N ERRO!");

				free(actualList);
				
				printf(" \n Elementos %i, Tempo %i MS",n,times[0]);
				//printf("\n Avaliacao Comparacoes %li  Avaliacoes %li",avaliacao.comparacoes, avaliacao.movimentacoes);

				n+=100000;
				fflush(stdout);

			}
			printf(" \n \n");
		}
	}
		system("PAUSE");
		return 0;
	printf("\nsegunda rodada (alguns) métodos sofisticados até 500 mil elementos.\n \n ");
	for(tipoLista=1;tipoLista<4;tipoLista++){

		makeTheList(mainList, MAXSIZE, tipoLista);
		for(funcoes=4;funcoes<13;funcoes++){
			n=50000;
			corte=510000;

			/* Funções que estão apresentando erro*/
			if(funcoes==9|| funcoes==10)
				funcoes+=2;

			printf("Metodo %s",nomes[funcoes]);

			while(n<corte){
				Avaliacao avaliacao;

				for(auxFor2=0;auxFor2<20;auxFor2++){

					long int *actualList = (long int*) malloc(n * sizeof(long int));
					memcpy(actualList, mainList, n * sizeof(long int));

					inicio=GetTickCount64();
					func[funcoes](actualList, n, &avaliacao);
					termino=GetTickCount64();
					times[auxFor2]=termino-inicio;

					 if(!estaOrdenadoCrescente(actualList,n))
							printf("ERRO \N ERRO!");

					free(actualList);
				}

				printf(" \n elementos %i, Tempo %i MS",nomes[funcoes],n,returnAVG(times),tipoLista);
				printf("\n Avaliacao Comparacoes %li  Avaliacoes %li",avaliacao.comparacoes, avaliacao.movimentacoes);

				clearTimes(times);
				n+=50000;

			}
			printf(" \n");
		}
	}

	
}
