package org.krypto.lab1.service;

public interface CipherService {
    String encrypt(String text, int key);
    String decrypt(String text, int key);
}
