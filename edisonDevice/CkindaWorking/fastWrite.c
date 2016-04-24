#include "mraa.h"
#include <stdio.h>
#include <unistd.h>
#include <sched.h>


void delayOwn(int n);
int main(int argc, char **argv)  
{  
	int output = 0;

	// Setting the scheduler prio to a higher one
	const struct sched_param priority={99};
 	sched_setscheduler(0,SCHED_FIFO,&priority);

//! [Interesting]  
    fprintf(stdout, "hello mraa\n Version: %s\n", mraa_get_version());  
    mraa_gpio_context clk;
    mraa_gpio_context dat;  
    clk = mraa_gpio_init(2); 
    dat = mraa_gpio_init(4); 
  
    mraa_gpio_use_mmaped(clk, 1);
    mraa_gpio_dir(dat, MRAA_GPIO_IN); 
  	
  	for (;;) {
  		int i;
	    for (i = 0; i < 24; i++) {
	        mraa_gpio_write(clk , 1);
	        // delayOwn(10);
	        	int n = 100;
	        	for (n;n > 0; n--){};
	        // possibly have delay here for settling time
	        output |= (mraa_gpio_read(dat)<< (23-i));
	        mraa_gpio_write(clk, 0); 
	        // delayOwn(10);
	        usleep(1);
	        // possibly another delay
	    }

	    // setting the mode on the hx711
	    mraa_gpio_write(clk , 1);
	    mraa_gpio_read(dat);
	    mraa_gpio_write(clk , 0);

	    

	    printf("%d\n", output);
	    usleep(500000);
    }

    return 0;
}

void delayOwn(int n) {
	int i = n;
	for (i;i > 0; i--){};
}