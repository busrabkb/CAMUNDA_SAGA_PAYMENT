> Tam analiz: [REQUIREMENT_ANALYSIS.md](../REQUIREMENT_ANALYSIS.md)

## TASK-01 — Docker'da Camunda ve Flyway ayağa kalkma hatasını gider

### Amaç

Geliştirici ortamında `docker compose up --build` komutunun Camunda ve Flyway hataları olmadan tamamlanmasını sağlamak.

### Business Requirement

Proje Docker ile ayağa kalkabilmeli; geliştirici altyapı sorunlarıyla uğraşmadan feature geliştirmeye geçebilmeli.

### Scope

* Camunda 7.23 `historyTimeToLive` zorunluluğunu BPMN process tanımında karşıla
* Flyway'in mevcut `ACT_*` tabloları varken migration'a devam edebilmesi için gerekli yapılandırmayı ekle
* Docker compose ile uygulamanın ayağa kalktığını doğrula

### Out of Scope

* Yeni business feature
* Saga akışına yeni adım ekleme
* Test yazma

### Acceptance Criteria

- [ ] `docker compose up --build` hatasız tamamlanır
- [ ] Uygulama `http://localhost:8080` üzerinde erişilebilir
- [ ] Camunda ENGINE-12018 (`historyTimeToLive`) hatası oluşmaz
- [ ] Flyway migration mevcut şemada takılmaz

### Dependencies

Yok

### Expected Changes

* Backend
* Camunda/BPMN

### Notes

Yerel ortamda commit `b23d158` bu değişiklikleri içerebilir; issue kapatılmadan önce Docker ile doğrulanmalı.
