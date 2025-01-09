#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <time.h>
#include <signal.h>

pthread_mutex_t vida_intruso_mutex;
pthread_mutex_t mel_mutex;
pthread_mutex_t n_op_mutex;


int intruso_perto = 0;
int n_operarias;
int vida_intruso;
long long mel = 0;

void atacar(){
    pthread_mutex_lock(&vida_intruso_mutex);
    vida_intruso--;
    printf("ataca intruso | HP = %d\n", vida_intruso);
    pthread_mutex_unlock(&vida_intruso_mutex);
}

void faz_mel(){
    pthread_mutex_lock(&mel_mutex);
    if(mel < 100){
        mel++;
        printf("faz mel\n");
        
    }
    else{
        printf("mel cheio\n");
    }
    pthread_mutex_unlock(&mel_mutex);
}

int come_mel(){
    pthread_mutex_lock(&mel_mutex);
    if(mel > 0){
        mel--;
        pthread_mutex_unlock(&mel_mutex);
        return 1;
    }
    pthread_mutex_unlock(&mel_mutex);
    printf("fome\n");
    return 0;
}

void* operaria(){
    time_t origem = time(NULL);
    while(time(NULL) < origem + 10){
        if(intruso_perto)
            atacar();
        else
            faz_mel();
        sleep(1);
    }
    printf("morreu operaria\n");
    pthread_mutex_lock(&n_op_mutex);
    n_operarias--;
    pthread_mutex_unlock(&n_op_mutex);

    
    return NULL;
}


void* larva(){
    time_t nasc = time(NULL);
    int mel_comido = 0;
    while(time(NULL) < nasc + 10 && mel_comido < 5){
        mel_comido += come_mel();
        sleep(1);
    }
    pthread_t nova_op;
    pthread_create(&nova_op, NULL, operaria, NULL);
    pthread_mutex_lock(&n_op_mutex);
    n_operarias++;
    pthread_mutex_lock(&n_op_mutex);
    printf("nova operaria\n");
    pthread_join(nova_op, NULL);
    return NULL;
}

void* rainha(void* args){
    int max = ((int*) args)[0];
    pthread_t novas_larvas[max];
    int i = 0;
    while(i < max){
        pthread_create(&novas_larvas[i], NULL, larva, NULL);
        i++;
        printf("nasceu a larva %d\n", i);
        sleep(5);    
    }
    printf("rainha cansou de parir\n");
    for(int j = 0; j < max; j++){
        pthread_join(novas_larvas[j], NULL);
    }
    return NULL;
}

void* intruso(){
    while(1){
        if(intruso_perto == 0){
            sleep(10);
            intruso_perto = 1;
            vida_intruso = 20;
        }
        else if(vida_intruso <= 0)
            intruso_perto = 0;
        
        
        if(n_operarias == 0)
            return NULL;
        sleep(1);
    }
}

void fim(){
    printf("Fim da colmeia\n");
    exit(0);
}

int main(){
    signal(SIGINT, fim);
    pthread_mutex_init(&vida_intruso_mutex, NULL);
    pthread_mutex_init(&mel_mutex, NULL);
    pthread_mutex_init(&n_op_mutex, NULL);
    
    int tam, larvas;
    n_operarias = tam;
    scanf("%d %d", &tam, &larvas);
    pthread_t* op = malloc(sizeof(pthread_t)*tam);
    for(int i = 0; i < tam; i++){
        pthread_create(&op[i], NULL, operaria, NULL);
    }
    pthread_t r;
    pthread_create(&r, NULL, rainha, &larvas);
    pthread_t intr;
    pthread_create(&intr, NULL, intruso, NULL); 
    pthread_join(r, NULL);
    printf("voltou rainha\n");
    printf("----\n");
    printf("fim\n");

    return 0;
}