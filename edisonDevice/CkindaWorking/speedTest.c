#include "mraa.h"
#include "stdint.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include <sys/mman.h>
#include <sys/stat.h>

uint8_t* load_memmap();

int main()
{
 // system("echo out > /sys/class/gpio/gpio128/direction");
 system("echo in > /sys/class/gpio/gpio129/direction");

 printf("after init\n");

uint8_t* mmap_reg = load_memmap();
 int pin= 129; //128; 
printf("after mmap\n");
 uint32_t mask=(uint32_t)(1 << (pin % 32));
 uint8_t valoffsetOn = 0x34;
 uint8_t valoffsetOff = 0x4c;
 
 uint8_t* loc=(pin / 32) * sizeof(uint32_t)+ mmap_reg;

 for (;;) {
    //printf("inside for\n");
  *(volatile uint32_t*) (loc + valoffsetOn) = mask;
  *(volatile uint32_t*) (loc + valoffsetOff) = mask;
 }
 return MRAA_SUCCESS;
}

// returns a pointer to the mem map of the pins
uint8_t* load_memmap(){
 int mmap_fd;
 struct stat fd_stat;
 uint8_t* mmap_reg;
 mmap_fd = open(
  "/sys/devices/pci0000:00/0000:00:0c.0/resource0",
  O_RDWR);
 fstat(mmap_fd, &fd_stat);
 mmap_reg =(uint8_t*) mmap(NULL, fd_stat.st_size,
   PROT_READ | PROT_WRITE, MAP_FILE | MAP_SHARED,
   mmap_fd, 0);
 return mmap_reg;
}