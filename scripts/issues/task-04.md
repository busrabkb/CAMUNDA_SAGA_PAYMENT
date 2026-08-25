> Tam analiz: [REQUIREMENT_ANALYSIS.md](../REQUIREMENT_ANALYSIS.md)

## TASK-04 — ProcessCompletion repository katmanı

### Amaç

`process_completions` tablosuna okuma/yazma erişimini sağlayan repository katmanını oluşturmak.

### Business Requirement

Completion kayıtları veritabanına yazılabilmeli; process instance veya order ile sorgulanabilmeli.

### Scope

* Insert metodu
* `process_instance_id` ile lookup metodu
* Mevcut `OrderRepository` yapısına paralel, ince persistence katmanı

### Out of Scope

* Business kuralları (duplicate kontrolü service'te)
* BPMN / delegate kodu
* API değişikliği

### Acceptance Criteria

- [ ] Completion kaydı insert edilebilir
- [ ] `process_instance_id` ile kayıt bulunabilir
- [ ] Kod mevcut repository pattern'i ile tutarlı
- [ ] JDBC/SQL yaklaşımı projedeki diğer repository'lerle aynı

### Dependencies

TASK-03

### Expected Changes

* Backend
