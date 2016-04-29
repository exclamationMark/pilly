#include "mraa.h"

int main(int argc, char **argv)  
{  
	int i, output;
    fprintf(stdout, "hello mraa\n Version: %s\n", mraa_get_version());  
    mraa_gpio_context clk = mraa_gpio_init(4); 
    mraa_gpio_context dat = mraa_gpio_init(2);
    mraa_gpio_dir(clk, MRAA_GPIO_OUT); 
    mraa_gpio_dir(dat, MRAA_GPIO_IN); 
  
    mraa_gpio_use_mmaped(clk, 1);  
  
    for (;;) { 
    	output = 0; 
        for(i = 0; i < 24; i++) {
        	mraa_gpio_write(clk, 1);
        	usleep(1);
        	output |= (mraa_gpio_read(dat)<< (23-i));
        	mraa_gpio_write(clk, 0);
        	usleep(1);
        }
        mraa_gpio_write(clk, 1);
        usleep(1);
        mraa_gpio_write(clk, 0);
        usleep(1000000);
        fprintf(stdout, "%d\n", output);  
    }  
} 