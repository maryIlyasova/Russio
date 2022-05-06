package com.example.russian.utils;

import java.util.Random;

public class RandomGenerationUtils {

        public static Integer generateRandomNumber(int size) {
            Random number=new Random();
           return number.nextInt(size);
        }
    }


