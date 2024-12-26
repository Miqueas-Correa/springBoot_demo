package ar.edu.utn.frbb.tup.springBoot_demo.service;

import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class BanelcoService {
    public static boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }
}
