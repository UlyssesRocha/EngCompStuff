#include <Eng.h>
#define MAXI 8
#define INFINITY 99999
#define MIN(a,b) (((a)<(b))?(a):(b))
#define MAX(a,b) (((a)>(b))?(a):(b))

int numberOfPoints;
double values[MAXI][MAXI],cableCost,trenchCost;

/*
Algoritmo guloso para speedup o código principal.
Heuristico!
*/
double GreedTCP(){
   char marked[MAXI];
   int sourceNode[MAXI];
   double cost[MAXI];
   double cable[MAXI];
   int nextNode;
   double finalCost=0;

   int auxFor;
   int numbMarked=1;

   //Preparando dados e inicializando variaveis.
   for(auxFor=0;auxFor<numberOfPoints;auxFor++){
      marked[auxFor]='o';
      cost[auxFor]=INFINITY;
      cable[auxFor]=INFINITY;
   }
   //marcando nó fonte
   marked[0]='x';
   cable[0]=0;
   nextNode=0;

   while(numbMarked<numberOfPoints){
      double auxCost;
      double auxCableCost;
      double auxSmaller=INFINITY;
      //Atualiza tabela com o novo nó que agora possui ligação com o mainframe
      for(auxFor=0;auxFor<numberOfPoints;auxFor++){
         if(values[nextNode][auxFor]!=0 ){
            auxCost=((values[nextNode][auxFor]*(cableCost+trenchCost)) + cable[nextNode]);
            auxCableCost=((values[nextNode][auxFor]*cableCost)+cable[nextNode]);
            //Se o novo custo for menor que o atual, ou, igual porem com valor de cabo menor, atualize
            //debug
            if((marked[auxFor]=='o') && (cost[auxFor]>auxCost) || (cost[auxFor]==auxCost && cable[auxFor]<auxCableCost)){
               //atualizando valores, no de origem, custo, cabo, numero de vertices em contato.
               sourceNode[auxFor]=nextNode;
               cost[auxFor]=auxCost;
               cable[auxFor]=auxCableCost;
            }
         }
      }

      //Procura melhor valor para fazer ligação.
      for(auxFor=0;auxFor<numberOfPoints;auxFor++){
         //Dentre os ainda disponiveis, escolha o com menor custo
         if((marked[auxFor]=='o') && (cost[auxFor]<auxSmaller )){
            auxSmaller=cost[auxFor];
            nextNode=auxFor;
         }
      }

      marked[nextNode]='x';
      numbMarked++;
   }

   for(auxFor=1;auxFor<numberOfPoints;auxFor++)
      finalCost=cost[auxFor]+finalCost;

   return finalCost;

}


int main(){
   int auxFor,auxFor2,numberOfEdges,auxValueP1,auxValueP2,auxCost;
  //scanf("%lf %lf",&cableCost,&trenchCost);
  // scanf("%d %d",&numberOfPoints,&numberOfEdges);
   
   //zerando variaveis cm valor "infinito"
   for(auxFor=0;auxFor<numberOfPoints;auxFor++){
      for(auxFor2=0;auxFor2<numberOfPoints;auxFor2++){
         values[auxFor][auxFor2]=0;
      }
    }

   for(auxFor=0;auxFor<numberOfEdges;auxFor++){
      //scanf("%d %d %f",&auxValueP1,&auxValueP2,&auxCost);
      values[auxValueP1][auxValueP2]=auxCost;
      values[auxValueP2][auxValueP1]=auxCost;

   }

 
   GreedTCP();

   //printf("\n%.3lf \n\n",bestCostDBB);

   return 0;

}
  return 0;
}
