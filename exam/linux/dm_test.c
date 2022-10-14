#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

int main(void)
{
    int pid;
    int i;
    i = 1000;
    pid = fork();
    if (pid == -1)
    {
        perror("fork error ");
        exit(0);
    }
    // �ڽ����μ����� �����Ű�� �ڵ�
    else if (pid == 0)
    {
        printf("Child process : my PID is %d\n", getpid());

        //input
        close(0);
        //output
        close(1);
        //error
        close(2);

        //new session, tty  closed
        setsid();

        while(1)
        {
            printf("deamon counter : %d\n", i);
            i++;
            sleep(10);
        }
    }
    // �θ����μ����� �����Ű�� �ڵ�
    else
    {
        printf("Parent Process: my PID is %d\n", pid);
        sleep(1);
        printf("Parent process is killed\n");
        exit(0);
    }
}
