> Tam analiz: [REQUIREMENT_ANALYSIS.md](../REQUIREMENT_ANALYSIS.md)

## TASK-07 — Order API'de completion bilgisini göster

### Amaç

Tamamlanma bilgisinin sipariş detay API'si üzerinden okunabilmesini sağlamak.

### Business Requirement

Sipariş sorgulandığında, başarıyla tamamlanan process'ler için tamamlanma bilgisi görülebilmeli.

### Scope

* GET /orders/{id} yanıtına completion alanları ekle
* Completion kaydı yoksa davranışı netleştir
* Mevcut response yapısını bozmadan genişlet

### Out of Scope

* Yeni endpoint
* BPMN değişikliği
* Entegrasyon testleri (TASK-08)

### Acceptance Criteria

- [ ] Happy path'te tamamlanan siparişlerde completion bilgisi response'da görünür
- [ ] Completion kaydı olmayan siparişlerde davranış tutarlı
- [ ] Mevcut API contract'ı breaking change olmadan genişler

### Dependencies

TASK-06

### Expected Changes

* Backend
* API

### Notes

`completedBy` ve `completedAt` response'da her zaman bulunur. Completion kaydı yoksa (compensation path) her iki alan `null` döner.
