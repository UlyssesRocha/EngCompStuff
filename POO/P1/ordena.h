#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#define MAX(A,B) A>B?A:B

struct avaliacao
{
    long int comparacoes;
    long int movimentacoes;
};
typedef struct avaliacao Avaliacao;

struct nodo
{
    long int info;
    struct nodo *proximo;
};
typedef struct nodo Nodo;

void troca(long int *a, long int *b)
{
    long int aux = *a;
    *a = *b;
    *b = aux;
}

int estaOrdenadoCrescente(long int lista[], long int comprimento)
{
    long int i;
    for(i= 0; i< comprimento-1; i++)
        if(lista[i] > lista[i+1])
            return 0;
    return 1;
}

void selecao_lista(Nodo *lista)
{
    Nodo *no = lista;
    Nodo *no_min, *no_passeio;
	int i=0;

    while(no != NULL)
    {
		i++;
        no_min = no;
        no_passeio = no->proximo;
        while(no_passeio != NULL)
        {
            if(no_min->info > no_passeio->info)
                no_min = no_passeio;
            no_passeio = no_passeio->proximo;
        }
        troca(&(no->info), &(no_min->info));
        no = no->proximo;
    }

}

void bolhaComFlag(long int lista[], long int comprimento, Avaliacao *avaliacao)
{
    long int i, j;
    char flag;

	
    avaliacao->comparacoes = 0;
    avaliacao->movimentacoes = 0;


    i= 1;
    do
    {
        flag = 0;
        for(j= 0; j< comprimento-i; j++)
            if(lista[j] > lista[j+1])
            {
                troca(&lista[j], &lista[j+1]);
                avaliacao->movimentacoes += 3;
                flag = 1;
            }
        //sempre faz 1 comparação por iteração
        avaliacao->comparacoes += comprimento - i;
        i++;
    }
    while(flag);
}

void cocktailSort(long int lista[], long int comprimento, Avaliacao *avaliacao)
{
  
    long int i, min, max;
    char flag;

    avaliacao->comparacoes = 0;
    avaliacao->movimentacoes = 0;

	min= 0;
    max= comprimento - 1;
    do
    {
        flag = 0;
        for(i= min; i< max; i++)
            if(lista[i] > lista[i+1])
            {
                troca(&lista[i], &lista[i+1]);
                avaliacao->movimentacoes += 3;
                flag = 1;
            }

        avaliacao->comparacoes += max - min;
        if(!flag) break;
        max--;

        for(i= max; i> min; i--)
            if(lista[i] < lista[i-1])
            {
                troca(&lista[i], &lista[i-1]);
                avaliacao->movimentacoes += 3;
                flag = 1;
            }

        avaliacao->comparacoes += max - min;
        min++;
    }
    while(flag);
}

void selecao(long int lista[], long int comprimento, Avaliacao *avaliacao)
{
   

    long int i, j, min;
	 //sempre faz 1 comparação por iteração do laço interno
    avaliacao->comparacoes = comprimento*(comprimento - 1)/2;
    //sempre faz 3 movimentação por iteração do laço externo
    avaliacao->movimentacoes = 3*(comprimento - 1);


	for(i= 0; i< comprimento - 1; i++)
    {
        min = i;
        for(j= i+1; j< comprimento; j++)
            if(lista[j]<lista[min])
                min = j;
        troca(&lista[i], &lista[min]);
    }
}

void insercao(long int lista[], long int comprimento, Avaliacao *avaliacao)
{
    long int i, j, pivo;

    avaliacao->comparacoes = 0;
    avaliacao->movimentacoes = 0;
	
	for(i= 1; i< comprimento; i++)
    {
        pivo = lista[i];
        j = i-1;
        while(j>=0 && lista[j]>pivo)
        {
            avaliacao->comparacoes += 1;
            lista[j+1] = lista[j];
            avaliacao->movimentacoes += 1;
            j--;
        }

        if(j>=0) avaliacao->comparacoes += 1;
        lista[j+1] = pivo;
        avaliacao->movimentacoes += 1;
    }
}

void shellSort(long int lista[], long int comprimento, Avaliacao *avaliacao)
{
    long int i, j, pivo;
    long int h= 1;

	avaliacao->comparacoes = 0;
    avaliacao->movimentacoes = 0;


    while(h<comprimento)
        h= 3*h +1;

    do
    {
        h /= 3;
        for(i= h; i< comprimento; i++)
        {
            pivo= lista[i];
            j= i-h;
            while(j>=0 && lista[j]>pivo)
            {
                avaliacao->movimentacoes += 1;
                avaliacao->comparacoes += 1;
                lista[j+h] = lista[j];
                j -= h;
            }
            lista[j+h]= pivo;
            avaliacao->movimentacoes += 2;
            if(j>=0) avaliacao->comparacoes += 1;
        }
    }
    while(h>1);
}

void merge(long int lista[], long int inicio, long int meio,
           long int fim, Avaliacao *avaliacao)
{
    long int i, j, k;
    long int *aux;
    aux = (long int*) malloc(sizeof(long int)*(fim-inicio+1));

    i= inicio;
    j= meio+1;
    k= 0;

    while(i<= meio && j<= fim)
    {
        if(lista[i]<= lista[j])
            aux[k++]= lista[i++];
        else
            aux[k++]= lista[j++];

        avaliacao->comparacoes += 1;
        avaliacao->movimentacoes += 1;
    }

    if(i<= meio)
        for(; i<= meio; i++, k++)
        {
            aux[k]= lista[i];
            avaliacao->movimentacoes += 1;
        }

    for(i= 0; i<= k-1; i++)
    {
        lista[inicio+i]= aux[i];
        avaliacao->movimentacoes += 1;
    }

    free(aux);
}

void _mergeSort(long int lista[], long int inicio,
                long int fim, Avaliacao *avaliacao)
{
    if(inicio< fim)
    {
        long int meio= (inicio + fim)/2;
        if(inicio< meio)
            _mergeSort(lista, inicio, meio, avaliacao);
        if(meio+1< fim)
            _mergeSort(lista, meio+1, fim, avaliacao);
        merge(lista, inicio, meio, fim, avaliacao);
    }
}

void mergeSort(long int lista[], long int comprimento, Avaliacao *avaliacao)
{
    avaliacao->comparacoes = 0;
    avaliacao->movimentacoes = 0;

    _mergeSort(lista, 0, comprimento - 1, avaliacao);
}

long int particao_det(long int lista[], long int inicio,
                      long int fim, Avaliacao *avaliacao)
{
    long int i, j;
    i= inicio+1;
    j= fim;

    while(i<= j)
    {
        while(i<= j && lista[i]<= lista[inicio])
        {
            i++;
            avaliacao->comparacoes += 1;
        }
        if(i<= j) avaliacao->comparacoes += 1;
        while(lista[j]> lista[inicio])
        {
            avaliacao->comparacoes += 1;
            j--;
        }
        if(i< j)
        {
            troca(&(lista[i++]), &(lista[j--]));
            avaliacao->movimentacoes += 3;
        }
    }

    troca(&lista[inicio], &lista[j]);
    avaliacao->movimentacoes += 3;
    return j;
}

long int particao_prob(long int lista[], long int inicio,
                       long int fim, Avaliacao *avaliacao)
{
    long int random = rand()*rand() + rand()*rand();
    long int pivo = inicio + random%(fim - inicio + 1);
    troca(&lista[pivo], &lista[inicio]);
    avaliacao->movimentacoes += 3;
    return particao_det(lista, inicio, fim, avaliacao);
}

void _quickSortDet(long int lista[], long int inicio,
                   long int fim, Avaliacao *avaliacao)
{
    long int j = particao_det(lista, inicio, fim, avaliacao);
    if(inicio < j-1)
        _quickSortDet(lista, inicio, j-1, avaliacao);
    if(j+1 < fim)
        _quickSortDet(lista, j+1, fim, avaliacao);
}

void _quickSortProb(long int lista[], long int inicio,
                    long int fim, Avaliacao *avaliacao)
{
    long int j = particao_prob(lista, inicio, fim, avaliacao);
    if(inicio < j-1)
        _quickSortProb(lista, inicio, j-1, avaliacao);
    if(j+1 < fim)
        _quickSortProb(lista, j+1, fim, avaliacao);
}

void quickSortDet(long int lista[], long int comprimento, Avaliacao *avaliacao)
{
    avaliacao->comparacoes = 0;
    avaliacao->movimentacoes = 0;

    _quickSortDet(lista, 0, comprimento - 1, avaliacao);

}

void quickSortProb(long int lista[], long int comprimento, Avaliacao *avaliacao)
{
    avaliacao->comparacoes = 0;
    avaliacao->movimentacoes = 0;

    _quickSortProb(lista, 0, comprimento - 1, avaliacao);
}

void countingSort(long int lista[], long int comprimento, Avaliacao *avaliacao)
{
    long int i;
    long int max = lista[0];
	

    avaliacao->movimentacoes = 1;

    for(i= 1; i< comprimento; i++)
        if(max < lista[i])
        {
            max= lista[i];
            avaliacao->movimentacoes += 1;
        }

		{
	long int *c = (long int*)(malloc((max+1) * sizeof(long int)));
    long int *aux = (long int*) malloc(comprimento*sizeof(long int));

    //1 para cada iteração
    avaliacao->comparacoes = comprimento - 1;

    

    for(i= 0; i<= max; i++) c[i] = 0;

    for(i= 0; i< comprimento; i++) c[lista[i]]++;

    for(i= 1; i<= max; i++) c[i] += c[i-1];

    for(i= comprimento-1; i>=0; i--)
    {
        c[lista[i]]--;
        aux[c[lista[i]]] = lista[i];
    }

    for(i= 0; i< comprimento; i++)
        lista[i] = aux[i];

    avaliacao->movimentacoes += 2*comprimento;

    free(c);
    free(aux);
		}
}

void bucketSort(long int lista[], long int comprimento, Avaliacao *avaliacao)
{
    long int i, j, max, bucket;
	Nodo **buckets;
    Nodo *reserva;

    avaliacao->movimentacoes = 1;
    avaliacao->comparacoes = comprimento;
	
	
	buckets = (Nodo**) calloc(comprimento,sizeof(Nodo**));
    reserva = (Nodo*) calloc(comprimento,sizeof(Nodo*));

    max = lista[0];
    for(i= 0; i< comprimento; i++)
    {
        buckets[i] = NULL;
        if(lista[i] > max)
        {
            max = lista[i];
            avaliacao->movimentacoes++;
        }
    }


    avaliacao->movimentacoes += comprimento;
    for(i= 0; i< comprimento; i++)
    {
        bucket = (lista[i]*comprimento)/(max+1);
		reserva[i].info = lista[i];
        reserva[i].proximo = buckets[bucket];
        buckets[bucket] = &reserva[i];
    }

    for(i= 0; i< comprimento; i++)
        selecao_lista(buckets[i]);

    avaliacao->movimentacoes += comprimento;
    for(j= 0, i= 0; i< comprimento; i++)
    {
        Nodo *aux = buckets[i];
        while(aux != NULL)
        {
            lista[j] = aux->info;
            j++;
            aux = aux->proximo;
        }
    }

	if(estaOrdenadoCrescente(lista,comprimento)) printf("SIM");else printf("NAO");

   free(buckets);
   free(reserva);
}

void uInsere(Nodo *bucketAlvo, long int valor){
	Nodo *novo;
	Nodo *no_passeio=bucketAlvo;

	novo = malloc(sizeof(Nodo));

	while(no_passeio->proximo!=NULL){
		if(no_passeio->proximo->info>valor){
			novo->proximo=no_passeio->proximo;
			no_passeio->proximo=novo;
			novo->info=valor;
			return;
		}
		no_passeio=no_passeio->proximo;
	}
		novo->proximo=no_passeio->proximo;
		no_passeio->proximo=novo;
		novo->info=valor;
		return;
}


/*
Seguindo 

Pseudo Code
1. Let n be the length of the input list L;
2. For each element i from L
   2.1. If B[i] is not empty
      2.1.1. Put A[i] into B[i] using insertion sort;
      2.1.2. Else B[i] := A[i] 
3. Concatenate B[i .. n] into one sorted list;
*/
void uBucket(long int lista[], long int comprimento, Avaliacao *avaliacao){
	Nodo *bucket;
	Nodo *no_passeio;
	unsigned long long int *listaResposta;
	int i,j, maior=0,auxBucket;

	bucket=malloc(comprimento*sizeof(Nodo));

	if(bucket==NULL)
		printf("faltou memoria"); 

	/*inicializar o array de buckets */
	for(i=0;i<comprimento;i++){
		bucket[i].proximo=NULL;
	}
	
	/*procura maior elemento */
	for(i=0;i<comprimento;i++) 
		maior=MAX(lista[i],maior);
	
	/*Preenche os buckets com os valores (subRotina insercao)*/
	for(i=0;i<comprimento;i++){
		auxBucket=lista[i]*((int)(comprimento/maior+1));
		if(auxBucket<0 ||auxBucket>maior+1)
			printf("%lli", auxBucket);
		uInsere(&bucket[auxBucket],lista[i]);
	}

	i=0;
	for(j=0;j<comprimento;j++){
		if(bucket[j].proximo!=NULL){
			no_passeio=bucket[j].proximo;
			while(no_passeio->proximo!=NULL && i<comprimento){
				lista[i]=no_passeio->info;
				i++;
				no_passeio=no_passeio->proximo;
			}
		}

	}
	for(i=0;i<comprimento;i++)printf("\n %li",lista[i]);

	printf("\n\n\n");

//	free(bucket);

}


void radixSort_Count(long int lista[], long int comprimento, Avaliacao *avaliacao)
{
    long int *c = (long int*) malloc(10*sizeof(long int)); //maior 9
    long int *aux = (long int*) malloc(comprimento*sizeof(long int));
    long int divisor = 1;

   int i, j;
   
    avaliacao->movimentacoes = 20*comprimento;
    avaliacao->comparacoes = 0;
   
   for(j= 0; j< 10; j++) //10 digitos
    {
        for(i= 0; i< 10; i++) c[i] = 0;

        for(i= 0; i< comprimento; i++)
            c[(lista[i]/divisor)%10]++;

        for(i= 1; i< 10; i++)
            c[i] += c[i-1];

        for(i= comprimento-1; i>= 0; i--)
        {
            int a = (lista[i]/divisor)%10;
            c[a]--;
            aux[c[a]] = lista[i];
        }

        for(i= 0; i< comprimento; i++)
            lista[i] = aux[i];

        divisor *= 10;
    }

    free(c);
    free(aux);
}

void radixSort_Bucket(long int lista[], long int comprimento, Avaliacao *avaliacao)
{
    Nodo **buckets = (Nodo**) malloc(10*sizeof(Nodo *));
    Nodo *reserva = (Nodo*) malloc(comprimento*sizeof(Nodo));
    long int i, j, k;
    long int divisor = 1;

    avaliacao->movimentacoes = 20*comprimento;
    avaliacao->comparacoes = 0;

    for(j= 0; j< 10; j++)
    {
        for(i= 0; i< 10; i++)
            buckets[i] = NULL;

        for(i= comprimento-1; i>= 0; i--)
        {
            int aux = (lista[i]/divisor) % 10;

            reserva[i].info = lista[i];
            reserva[i].proximo = buckets[aux];

            buckets[aux] = &reserva[i];
        }

        for(k= 0, i= 0; i< 10; i++)
        {
            Nodo *aux = buckets[i];
            while(aux != NULL)
            {
                lista[k] = aux->info;
                k++;
                aux = aux->proximo;
            }
        }

        divisor *= 10;
    }

    free(buckets);
    free(reserva);
}

void heapSort(long int lista[], long int comprimento, Avaliacao *avaliacao)
{

	 long int i, j;

    avaliacao->movimentacoes = 0;
    avaliacao->comparacoes = 0;

   

    for(i= comprimento - 1; i> 0; i--)
    {
        long int pai= (long int) floor((i-1)/2.0);
        long int maior= i;

        while(pai >= 0)
        {
            avaliacao->comparacoes += 1;
            if(lista[pai] > lista[maior])
                maior = pai;
            pai = (long int) floor((pai-1)/2.0);
        }
        troca(&lista[i], &lista[maior]);
        avaliacao->movimentacoes += 3;
    }

    for(j= 0, i= comprimento - 1; i> 0; i--, j++)
    {
		long int pai = 0;
        long int filho_d = 2;
        long int filho_e = 1;

		 troca(&lista[0], &lista[i]);
        avaliacao->movimentacoes += 3;

        while(filho_e < comprimento - 1 - j)
        {
            if(filho_d < comprimento - 1 - j &&
               lista[filho_d] <= lista[filho_e] &&
               lista[pai]>lista[filho_d])
            {
                avaliacao->comparacoes += 2;
                avaliacao->movimentacoes += 3;
                troca(&(lista[filho_d]), &(lista[pai]));
                pai = filho_d;
            } else if(lista[pai]>lista[filho_e])
            {
                avaliacao->comparacoes += 3;
                avaliacao->movimentacoes += 3;
                troca(&(lista[filho_e]), &(lista[pai]));
                pai = filho_e;
            } else {
                avaliacao->comparacoes += 3;
                break;
            }

            filho_d = 2*pai + 2;
            filho_e = filho_d - 1;
        }
    }

    avaliacao->movimentacoes += 3*((comprimento-1)/2 + 1);
    for(i= 0; i<= (comprimento - 1)/2; i++)
        troca(&(lista[i]), &(lista[comprimento - 1 - i]));
}
