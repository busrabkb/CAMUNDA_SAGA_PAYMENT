> Tam analiz: [REQUIREMENT_ANALYSIS.md](../REQUIREMENT_ANALYSIS.md)

## TASK-03 — ProcessCompletion persistence model tanımı

### Amaç

Completion kayıtlarını temsil edecek persistence model yapısını tanımlamak.

### Business Requirement

Completion verisi uygulama katmanları arasında tip güvenli ve tutarlı şekilde taşınabilmeli.

### Scope

* Mevcut `OrderRecord` / `PaymentRecord` pattern'ine uygun record veya entity tanımı
* Completion kaydının temel alanlarını kapsayan immutable yapı
* Naming ve package yapısı mevcut persistence katmanıyla tutarlı olmalı

### Out of Scope

* Repository implementasyonu
* Business validation
* BPMN / API değişikliği

### Acceptance Criteria

- [ ] Completion kaydı temsil eden persistence model mevcut
- [ ] Alanlar TASK-02 migration şemasıyla uyumlu
- [ ] Gereksiz abstraction veya ek katman eklenmemiş

### Dependencies

TASK-02

### Expected Changes

* Backend

### Notes

Coding agent mevcut `infrastructure.persistence` paketindeki convention'ı inceleyerek karar vermelidir.
