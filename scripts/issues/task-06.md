> Tam analiz: [REQUIREMENT_ANALYSIS.md](../REQUIREMENT_ANALYSIS.md)

## TASK-06 — BPMN Record Completion service task ve delegate

### Amaç

Happy path akışına Inventory sonrası completion kaydını tetikleyen Camunda adımını eklemek.

### Business Requirement

Inventory başarıyla tamamlandıktan sonra, process sonlanmadan önce tamamlanma bilgisi kaydedilmeli.

### Scope

* BPMN: Inventory → Record Completion → End akışını oluştur
* İnce JavaDelegate implementasyonu (mevcut delegate pattern)
* Process variable'lara completion bilgisi yaz
* Compensation / iptal yoluna dokunma

### Out of Scope

* REST API değişikliği
* Entegrasyon testleri
* User Task ekleme

### Acceptance Criteria

- [ ] Başarılı akış: Start → Create Order → Payment → Inventory → Record Completion → End
- [ ] İptal yolu aynen çalışır (`amount > 500` compensation senaryosu)
- [ ] Delegate orchestration yapar; iş mantığı TASK-05 service'inde kalır
- [ ] Process variable'lar set edilir

### Dependencies

TASK-05

### Expected Changes

* Backend
* Camunda/BPMN

### Notes

Camunda orchestration yapar; business logic BPMN'e taşınmaz.
