#!/usr/bin/python

import RPi.GPIO as GPIO
import time

#라즈베리파이 GPIO핀 넘버를 사용
GPIO.setmode(GPIO.BCM)
pin_trigger = 4
pin_echo = 17

GPIO.setup(pin_trigger, GPIO.OUT) # trigger signal
GPIO.output(pin_trigger, GPIO.LOW)

GPIO.setup(pin_echo, GPIO.IN) # echo signal

GPIO.setup(18, GPIO.OUT)

time.sleep(.5)
i = 0
print("Measuring Distance...")

try :
    while True:
       i = i + 1
       print(i)
       GPIO.output(pin_trigger, GPIO.HIGH) # shoot the signal for 10 us
       time.sleep(.00001) # 10 us
       GPIO.output(pin_trigger, GPIO.LOW)

       # waiting for the return signal
       while GPIO.input(pin_echo) == GPIO.LOW :
           pass
       start = time.time()

       # receiving signal
       while GPIO.input(pin_echo) == GPIO.HIGH :
           pass
       stop = time.time()

       d = (stop - start) * 170 * 100 # cm, speed of sound 340 m/s in air,  d = 340/2
       print(format(d, ".2f") + " cm")
       
       if (d > 50): 
           GPIO.output(18, GPIO.HIGH)
	   print("HIGH")
           time.sleep(.5)
       else:
           GPIO.output(18, GPIO.LOW)
           print("LOW")
           time.sleep(.5)

       #time.sleep(.5)

except KeyboardInterrupt:
    print("\nInterrupted!")

finally:
    GPIO.cleanup()
