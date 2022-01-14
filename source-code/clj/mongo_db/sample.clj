
(ns mongo-db.sample
    (:require [mid-fruits.time :as time]
              [mongo-db.api    :as mongo-db]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-prototype
  [document]
  (merge document
         {:namespace/added-at    (time/timestamp-string)
          :namespace/modified-at (time/timestamp-string)}))

(defn update-prototype
  [document]
  (merge {:namespace/added-at    (time/timestamp-string)}
         document
         {:namespace/modified-at (time/timestamp-string)}))



;; -- Actions -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-my-document!
  []
  ; @description (mongo-db/insert-document! ...)
  ; - Ha NEM létezik a megadott azonosítóval rendelkező dokumentum a kollekcióban, akkor létrehozza.
  ; - Ha létezik a megadott azonosítóval rendelkező dokumentum a kollekcióban, akkor NEM végez műveletet!
  ; - A függvény visszatérési értéke sikeres mentés esetén a dokumentum.
  ; - Ha nem adsz meg :namespace/id tulajdonságot a dokumentumnak, akkor a függvény generál
  ;   számára egy string típusú azonosítót.
  ; - Az {:ordered? true} tulajdonság beállításával az újonnan hozzáadott dokumentum
  ;   {:namespace/order ...} tulajdonsága a dokumentum kollekción belül sorszámát (utolsó)
  ;   kapja meg értékként.
  (mongo-db/insert-document! "my-collection" {:namespace/my-keyword  :my-value
                                              :namespace/your-string "your-value"
                                              :namespace/id          "MyObjectId"}
                                             {:prototype-f #(insert-prototype %)}))

(defn save-my-document!
  []
  ; @description (mongo-db/save-document! ...)
  ; - Saving: upserting by ID
  ; - Ha NEM létezik a megadott azonosítóval rendelkező dokumentum a kollekcióban, akkor létrehozza.
  ; - Ha létezik a megadott azonosítóval rendelkező dokumentum a kollekcióban, akkor felülírja.
  ; - A függvény visszatérési értéke sikeres mentés esetén a dokumentum.
  ; - Ha nem adsz meg :namespace/id tulajdonságot a dokumentumnak, akkor a függvény generál
  ;   számára egy string típusú azonosítót.
  ; - Az {:ordered? true} tulajdonság beállításával az újonnan hozzáadott dokumentum
  ;   {:namespace/order ...} tulajdonsága a dokumentum kollekción belül sorszámát (utolsó)
  ;   kapja meg értékként.
  (mongo-db/save-document! "my-collection" {:namespace/my-keyword  :my-value
                                            :namespace/your-string "your-value"
                                            :namespace/id          "MyObjectId"}
                                           {:prototype-f #(update-prototype %)}))

(defn update-my-document!
  []
  ; @description (mongo-db/update-document! ...)
  ; - Ha NEM létezik a megadott feltételeknek megfelelő dokumentum a kollekcióban, akkor NEM hozza létre!
  ; - Ha létezik a megadott feltételeknek megfelelő dokumentum a kollekcióban, akkor felülírja.
  ; - A függvény visszatérési értéke sikeres írás esetén true, minden más esetben false.
  ; - A feltételek tartalmazhat :namespace/id tulajdonságot!
  ; - A dokumentum NEM tartalmazhat :namespace/id tulajdonságot!
  (mongo-db/update-document! "my-collection" {:namespace/my-color "red"}
                                             {:namespace/my-color "blue"}
                                             {:prototype-f #(update-prototype %)}))

(defn upsert-my-document!
  []
  ; @description (mongo-db/upsert-document! ...)
  ; - Ha NEM létezik a megadott feltételeknek megfelelő dokumentum a kollekcióban, akkor létrehozza!
  ; - Ha létezik a megadott feltételeknek megfelelő dokumentum a kollekcióban, akkor felülírja.
  ; - A függvény visszatérési értéke sikeres írás esetén true, minden más esetben false.
  ; - A feltételek tartalmazhat :namespace/id tulajdonságot!
  ; - A dokumentum NEM tartalmazhat :namespace/id tulajdonságot!
  (mongo-db/upsert-document! "my-collection" {:namespace/my-color "red"}
                                             {:namespace/my-color "blue"}
                                             {:prototype-f #(update-prototype %)}))

(defn merge-my-document!
  []
  ; @description (mongo-db/merge-document! ...)
  ; - Ha NEM létezik a megadott azonsítóval rendelkező dokumentum a kollekcióban, akkor NEM hozza létre!
  ; - Ha létezik a megadott azonsítóval rendelkező dokumentum a kollekcióban, akkor összefésüli.
  ; - A függvény visszatérési értéke sikeres írás esetén true, minden más esetben false.
  (mongo-db/merge-document! "my-collection" {:namespace/my-keyword  :my-value
                                             :namespace/your-string "your-value"
                                             :namespace/id          "MyObjectId"}
                                            {:prototype-f #(update-prototype %)}))

(defn remove-my-document!
  []
  ; @description (mongo-db/remove-document! ...)
  ; - A függvény visszatérési értéke sikeres törlés esetén a dokumentum azonsítója.
  (mongo-db/remove-document! "my-collection" "MyObjectId"))

(defn duplicate-my-document!
  []
  ; @description (mongo-db/duplicate-document! ...)
  ; - A függvény visszatérési értéke sikeres duplikálás esetén a másolat dokumentum.
  (mongo-db/duplicate-document! "my-collection" "MyObjectId" {:prototype-f #(insert-prototype %)}))
