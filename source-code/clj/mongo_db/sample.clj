
(ns mongo-db.sample
    (:require [mid-fruits.time :as time]
              [mongo-db.api    :as mongo-db]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-prototype
  [document]
  (merge document
         {:my-namespace/added-at    (time/timestamp-string)
          :my-namespace/modified-at (time/timestamp-string)}))

(defn save-prototype
  [document]
  (merge {:my-namespace/added-at    (time/timestamp-string)}
         document
         {:my-namespace/modified-at (time/timestamp-string)}))



;; -- Actions -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-my-document!
  []
  ; @description (mongo-db/insert-document! ...)
  ; - Ha NEM létezik a megadott azonosítóval rendelkező dokumentum a kollekcióban, akkor létrehozza.
  ; - A függvény visszatérési értéke a dokumentum.
  ; - Ha nem adsz meg :my-namespace/id tulajdonságot a dokumentumnak, akkor a függvény generál
  ;   számára egy string típusú azonosítót.
  (mongo-db/insert-document! "my-collection" {:my-namespace/my-keyword  :my-value
                                              :my-namespace/your-string "your-value"
                                              :my-namespace/id          "my-document"}
                                             {:prototype-f #(insert-prototype %)}))

(defn save-my-document!
  []
  ; @description (mongo-db/save-document! ...)
  ; - Ha NEM létezik a megadott azonosítóval rendelkező dokumentum a kollekcióban, akkor létrehozza.
  ; - Ha létezik a megadott azonosítóval rendelkező dokumentum a kollekcióban, akkor felülírja.
  ; - A függvény visszatérési értéke a dokumentum.
  ; - Ha nem adsz meg :my-namespace/id tulajdonságot a dokumentumnak, akkor a függvény generál
  ;   számára egy string típusú azonosítót.
  (mongo-db/save-document! "my-collection" {:my-namespace/my-keyword  :my-value
                                            :my-namespace/your-string "your-value"
                                            :my-namespace/id          "my-document"}
                                           {:prototype-f #(save-prototype %)}))
