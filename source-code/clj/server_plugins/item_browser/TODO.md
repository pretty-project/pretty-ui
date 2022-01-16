
# server-plugins.item-browser.engine
- Az env->get-pipeline és env->count-pipeline függvény is kiolvassák az adatbázisból
  az aktuálisan böngészett elem :namespace/items értékét. Ezt a két olvasást le lehet
  csökkenteni egy olvasásra is.

- Az env->pipeline-props függvény filter-pattern értékeként az aktuálisan böngészett
  elem :namespace/items értékét használja és nem fűzi össze a kliensről érkezett
  filter-pattern értékével (figyelmen kívül hagyja), mert a mongo-db pipelines névtere
  még nem kezeli az egymásba ágyazott filter-pattern értékeket (a filter-query függvény nem rekurzív)
