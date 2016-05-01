#include "mraa.h"
#define n_reads 11

void fuckDelay(int time){
	int i=0;
	int dupa=1;
	for(i=0; i<time; i++) {
		dupa++;
	}
}


int main(int argc, char **argv)  
{  
	int i, j, output, outputArray[n_reads], i_reads, tmp;
 
    mraa_gpio_context clk = mraa_gpio_init(4); 
    mraa_gpio_context dat = mraa_gpio_init(2);
    mraa_gpio_dir(clk, MRAA_GPIO_OUT); 
    mraa_gpio_dir(dat, MRAA_GPIO_IN); 
  
    mraa_gpio_use_mmaped(clk, 1);

    i_reads = n_reads;

    while (i_reads > 0) { 

        while(mraa_gpio_read(dat)) {}

    	output = 0; 
        for(i = 0; i < 24; i++) {
        	mraa_gpio_write(clk, 1);
        	fuckDelay(2000);
        	output |= (mraa_gpio_read(dat)<< (23-i));
        	mraa_gpio_write(clk, 0);
        	fuckDelay(2000);
        }
        mraa_gpio_write(clk, 1);
        fuckDelay(2000);
        mraa_gpio_write(clk, 0);

        if(output < 16777215) {
        	//fprintf(stdout, "%d\n", output);
            outputArray[i_reads-1] = output;
            i_reads--; // decrement only if the valid read
        }
    } 


    //sorting the array
    for(i = 1; i < n_reads; i++){
        j = i;
        while ((j > 0) && (outputArray[j-1]>outputArray[j])) {
            tmp = outputArray[j];
            outputArray[j] = outputArray[j-1];
            outputArray[j-1] = tmp;
            j--;
        }
    } 

    fprintf(stdout, "%d\n", outputArray[5]);

} 


