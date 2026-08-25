> Tam analiz: [REQUIREMENT_ANALYSIS.md](../REQUIREMENT_ANALYSIS.md)

## TASK-05 — CompletionService iş mantığı

### Amaç

Process tamamlanma kaydının business kurallarını service katmanında tanımlamak.

### Business Requirement

Happy path'te saga tamamlandığında tamamlanma bilgisi tek seferlik olarak kaydedilmeli.

### Scope

* Completion kayıt oluşturma servisi (mevcut domain yapısına uygun konumlandırma)
* Girdi: `orderId`, `processInstanceId`, `completedBy`
* Aynı `process_instance_id` için tekrar kayıt oluşturulmaması kuralı
* Servis için unit test'ler

### Out of Scope

* BPMN / delegate implementasyonu
* REST API genişletmesi
* Compensation yolu

### Acceptance Criteria

- [ ] İlk kayıt başarıyla oluşturulur
- [ ] Duplicate `process_instance_id` güvenli şekilde yönetilir
- [ ] Unit test'ler happy path ve duplicate senaryosunu kapsar
- [ ] Business logic delegate içinde değil service'te

### Dependencies

TASK-04

### Expected Changes

* Backend
* Test

### Notes

`completedBy`, siparişi başlatan `POST /orders` endpoint'inden gelen kullanıcı bilgisidir (`customerId` process variable). Record Completion adımında bu değer DB'ye yazılır.
