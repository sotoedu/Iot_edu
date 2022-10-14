#-*- coding: utf-8 -*-     
import RPi.GPIO as GPIO    
import time        
    
#라즈베리파이 보드핀 넘버를 사용     
GPIO.setmode( GPIO.BOARD )    
    
#12번 핀을 출력모드로 설정한다.     
GPIO.setup( 12, GPIO.OUT )    
        
for i in range (0,3):    
    GPIO.output( 12, GPIO.HIGH )    
    time.sleep(1)    
    
    GPIO.output( 12, GPIO.LOW )    
    time.sleep(1)    
    
GPIO.cleanup() 