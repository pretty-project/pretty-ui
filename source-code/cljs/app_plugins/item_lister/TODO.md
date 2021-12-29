
# app-plugins.item-lister

# XXX#6508
- Ha az elemek törlése után megjelenő felugró értesítésen a "Visszaállítás" gombra kattintasz,
  akkor az item-lister plugin elküldi a visszaállításhoz szükséges adatokat a szervernek,
  majd a válasz megérkezése után újratölti az elemek listáját, amihez feleslegesen küld
  kettő request-et, egy helyett.
