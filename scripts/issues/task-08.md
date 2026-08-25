> Tam analiz: [REQUIREMENT_ANALYSIS.md](../REQUIREMENT_ANALYSIS.md)

## TASK-08 — Completion feature entegrasyon testleri

### Amaç

Completion feature'ının uçtan uca doğru çalıştığını otomatik testlerle kanıtlamak.

### Business Requirement

Hem başarılı saga hem compensation senaryolarında completion davranışının doğru olduğu garanti altına alınmalı.

### Scope

* Happy path entegrasyon testi: completion kaydı oluşur, API'den okunabilir
* Compensation path entegrasyon testi: completion kaydı oluşmaz
* Mevcut testlerin bozulmadığını doğrula

### Out of Scope

* Yeni feature geliştirme
* BPMN / service kodu değişikliği (yalnızca test)

### Acceptance Criteria

- [ ] amount <= 500: completion kaydı DB'de ve API response'da görünür
- [ ] amount > 500: completion kaydı oluşmaz
- [ ] Tüm mevcut testler geçer
- [ ] Testler mevcut OrderSagaIntegrationTest pattern'ini takip eder

### Dependencies

TASK-07

### Expected Changes

* Test
