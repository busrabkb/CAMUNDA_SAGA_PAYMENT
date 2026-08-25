> Tam analiz: [REQUIREMENT_ANALYSIS.md](../REQUIREMENT_ANALYSIS.md)

## TASK-02 — process_completions veritabanı migration'ı

### Amaç

Başarıyla tamamlanan process'lerin completion bilgisini kalıcı olarak saklayacak veritabanı şemasını oluşturmak.

### Business Requirement

Sistem, happy path'te tamamlanan her order saga için tamamlanma bilgisini veritabanında tutabilmeli.

### Scope

* Flyway `V2__create_process_completions.sql` migration dosyası oluştur
* Tablo: `process_completions`
* Alanlar: `id`, `order_id`, `process_instance_id`, `completed_by`, `completed_at`
* `process_instance_id` üzerinde unique constraint
* Gerekli foreign key / index kararlarını mevcut tablo convention'ına göre uygula

### Out of Scope

* Java kodu
* BPMN değişikliği
* API değişikliği

### Acceptance Criteria

- [ ] Migration mevcut Flyway naming convention'ına uygun
- [ ] Tablo happy path completion kayıtlarını tutacak yapıda
- [ ] Aynı `process_instance_id` için ikinci kayıt engellenir
- [ ] Uygulama ayağa kalktığında migration hatasız uygulanır

### Dependencies

TASK-01

### Expected Changes

* Database

### Notes

`orders` tablosundaki naming ve timestamp convention'ını takip et.
