package org.krypto.lab1.controller;

import lombok.RequiredArgsConstructor;
import org.krypto.lab1.dto.*;
import org.krypto.lab1.service.AttackService;
import org.krypto.lab1.service.CipherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/caesar")
@RequiredArgsConstructor
public class CaesarController {

    private final CipherService cipherService;
    private final AttackService attackService;

    @PostMapping("/encrypt")
    public ResponseEntity<CipherResponse> encrypt(@RequestBody CipherRequest req) {
        String result = cipherService.encrypt(req.text(), req.key());
        return ResponseEntity.ok(new CipherResponse(req.text(), req.key(), result));
    }

    @PostMapping("/decrypt")
    public ResponseEntity<CipherResponse> decrypt(@RequestBody CipherRequest req) {
        String result = cipherService.decrypt(req.text(), req.key());
        return ResponseEntity.ok(new CipherResponse(req.text(), req.key(), result));
    }

    @PostMapping("/attack/known-plaintext")
    public ResponseEntity<Map<String, Object>> knownPlaintext(@RequestBody KnownPlaintextRequest req) {
        int key = attackService.findKeyKnownPlaintext(req.plainText(), req.cipherText());
        return ResponseEntity.ok(Map.of("key", key, "message", "Ключ успешно найден"));
    }

    @PostMapping("/attack/ciphertext-only")
    public ResponseEntity<BruteForceResponse> ciphertextOnly(@RequestBody CipherRequest req) {
        return ResponseEntity.ok(new BruteForceResponse(attackService.bruteForceAll(req.text())));
    }

    @PostMapping("/attack/auto-detect")
    public ResponseEntity<AutoDetectResponse> autoDetect(@RequestBody CipherRequest req) {
        return ResponseEntity.ok(attackService.autoDetectKey(req.text()));
    }
}
