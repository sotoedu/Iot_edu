#include <unistd.h>
#include <stdio.h>

int main(void) {
        printf("Start\n");
        execl("/bin/ls","ls","-al",NULL);
        printf("Well done\n");
}
