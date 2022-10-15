
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v1.2.2
; Compatibility: x4.2.5



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.data-range-handler
    (:require [mid-fruits.math            :as math]
              [mid-fruits.vector          :as vector]
              [re-frame.api               :refer [r]]
              [x.mid-db.partition-handler :as partition-handler]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name High kurzor
;
; @name Low kurzor
;
; @name Range
;  A low és a high kurzor közötti elemek
;
; @name Post range
;
; @name Pre range
;
; Kurzorok viselkedése: Csak azok a viselkedési minták vannak a particio kezelő
; által szabályozva, amik a megfelelő működésért felelnek, tehát nem engedi, hogy
; valamelyik kurzor hibás tartományba kerüljön, egy másik kurzor, vagy a partíció
; elemeinek változása kapcsán. Ezen felül minden más kurzor viselkedési logika
; a ranged partititiont használó modul által kell szabályozva legyen.



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.partition-handler
(def get-data-item-count partition-handler/get-data-item-count)
(def get-data-items      partition-handler/get-data-items)
(def get-data-order      partition-handler/get-data-order)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-cursor-value-in-threshold?
  ; @param (namespaced keyword) partition-id
  ; @param (integer) n
  ;
  ; @usage
  ;  (db/data-cursor-value-in-threshold? ::my-partition 7)
  ;
  ; @return (boolean)
  ;  A megadott n érték az elfogadható határértékeken belül van-e?
  ;  Egyik kurzor értéke sem lehet kevesebb, mint 0 és nem lehet több, mint
  ;  a partíció elemeinek száma
  [db [_ partition-id n]]
  (let [data-item-count (r get-data-item-count db partition-id)]
       (and (<= n data-item-count)
            (>  n 0))))

(defn get-data-cursor-high
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-data-cursor-high ::my-partition)
  ;
  ; @return (integer)
  [db [_ partition-id]]
  (get-in db [partition-id :data-cursor-high]))

(defn get-data-cursor-low
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-data-cursor-low ::my-partition)
  ;
  ; @return (integer)
  [db [_ partition-id]]
  (get-in db [partition-id :data-cursor-low]))

(defn data-range-passable?
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/data-range-passable? ::my-partition)
  ;
  ; @return (boolean)
  ;  A low és a high kurzorok között van-e különbség, tehát a két kurzor
  ;  meghatároz-e egy bejárható szakaszt?
  [db [_ partition-id]]
  (let [data-cursor-high (r get-data-cursor-high db partition-id)
        data-cursor-low  (r get-data-cursor-low  db partition-id)]
       (< 0 (- data-cursor-high data-cursor-low))))

(defn get-first-data-item-id-in-range
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-first-data-item-id-in-range ::my-partition)
  ;
  ; @return (keyword)
  ;  A low kurzor utáni elem azonosítójával tér vissza
  [db [_ partition-id]]
  (if (r data-range-passable? db partition-id)
      (let [data-order      (r get-data-order      db partition-id)
            data-cursor-low (r get-data-cursor-low db partition-id)]
           (nth data-order data-cursor-low))))

(defn get-first-data-item-in-range
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-first-data-item-in-range ::my-partition)
  ;
  ; @return (*)
  ;  A low kurzor utáni elemmel tér vissza
  [db [_ partition-id]]
  (let [data-item-id (r get-first-data-item-id-in-range db partition-id)]
       (get-in db [partition-id :data-items data-item-id])))

(defn get-last-data-item-id-in-range
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-last-data-item-id-in-range ::my-partition)
  ;
  ; @return (keyword)
  ;  A high kurzor előtti elem azonosítójával tér vissza
  [db [_ partition-id]]
  (if (r data-range-passable? db partition-id)
      (let [data-order       (r get-data-order       db partition-id)
            data-cursor-high (r get-data-cursor-high db partition-id)]
           (nth data-order (dec data-cursor-high)))))

(defn get-last-data-item-in-range
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-last-data-item-in-range ::my-partition)
  ;
  ; @return (*)
  ;  A high kurzor előtti elemmel tér vissza
  [db [_ partition-id]]
  (let [data-item-id (r get-last-data-item-id-in-range db partition-id)]
       (get-in db [partition-id :data-items data-item-id])))

(defn get-first-data-item-id-post-range
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-first-data-item-id-post-range ::my-partition)
  ;
  ; @return (keyword)
  ;  A high kurzor utáni elem azonosítójával tér vissza
  [db [_ partition-id]]
  (let [data-order       (r get-data-order       db partition-id)
        data-item-count  (r get-data-item-count  db partition-id)
        data-cursor-high (r get-data-cursor-high db partition-id)]
       (if (<= data-cursor-high data-item-count)
           (nth data-order data-cursor-high))))

(defn get-first-data-item-post-range
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-first-data-item-post-range ::my-partition)
  ;
  ; @return (*)
  ;  A high kurzor utáni elemmel tér vissza
  [db [_ partition-id]]
  (let [data-item-id (r get-first-data-item-id-post-range db partition-id)]
       (get-in db [partition-id :data-items data-item-id])))

(defn get-last-data-item-id-pre-range
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-last-data-item-id-pre-range ::my-partition)
  ;
  ; @return (keyword)
  ;  A low kurzor előtti elem azonosítójával tér vissza
  [db [_ partition-id]]
  (let [data-order      (r get-data-order      db partition-id)
        data-cursor-low (r get-data-cursor-low db partition-id)]
       (if (>= data-cursor-low 1)
           (nth data-order (dec data-cursor-low)))))

(defn get-last-data-item-pre-range
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-last-data-item-pre-range ::my-partition)
  ;
  ; @return (*)
  ;  A low kurzor előtti elemmel tér vissza
  [db [_ partition-id]]
  (let [data-item-id (r get-last-data-item-id-pre-range db partition-id)]
       (get-in db [partition-id :data-items data-item-id])))

(defn get-in-range-data-order
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-in-range-data-order ::my-partition)
  ;
  ; @return (vector)
  ;  A két kurzor közötti elemek azonosítóival tér vissza
  [db [_ partition-id]]
  (let [data-order       (r get-data-order       db partition-id)
        data-cursor-high (r get-data-cursor-high db partition-id)
        data-cursor-low  (r get-data-cursor-low  db partition-id)]
       (vector/ranged-items data-order data-cursor-low data-cursor-high)))

(defn get-in-range-data-items
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-in-range-data-items ::my-partition)
  ;
  ; @return (map)
  ;  A két kurzor közötti elemekkel tér vissza
  [db [_ partition-id]]
  (let [data-items (r get-data-items db partition-id)]
       (select-keys data-items (r get-in-range-data-order db partition-id))))

(defn get-pre-range-data-order
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-pre-range-data-order ::my-partition)
  ;
  ; @return (vector)
  ;  A low kurzor előtti elemek azonosítóival tér vissza
  [db [_ partition-id]]
  (let [data-order      (r get-data-order      db partition-id)
        data-cursor-low (r get-data-cursor-low db partition-id)]
       (vector/ranged-items data-order 0 data-cursor-low)))

(defn get-pre-range-data-items
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-pre-range-data-items ::my-partition)
  ;
  ; @return (map)
  ;  A low kurzor előtti elemekkel tér vissza
  [db [_ partition-id]]
  (let [data-items (r get-data-items db partition-id)]
       (select-keys data-items (r get-pre-range-data-order db partition-id))))

(defn get-post-range-data-order
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-post-range-data-order ::my-partition)
  ;
  ; @return (vector)
  ;  A high kurzor utáni elemek azonosítóival tér vissza
  [db [_ partition-id]])
  ; TODO ...

(defn get-post-range-data-items
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/get-post-range-data-items ::my-partition)
  ;
  ; @return (map)
  ;  A high kurzor utáni elemekkel tér vissza
  [db [_ partition-id]]
  (let [data-items (r get-data-items db partition-id)]
       (select-keys data-items (r get-post-range-data-order db partition-id))))

(defn partition-ranged?
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (db/partition-ranged? ::my-partition)
  ;
  ; @return (boolean)
  [db [_ partition-id]]
  (integer? (r get-data-cursor-high db partition-id)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-data-cursor-high!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (integer) n
  ;
  ; @return (map)
  [db [_ partition-id n]]
  (let [data-cursor-high (r get-data-cursor-high db partition-id)
        data-cursor-low  (r get-data-cursor-low  db partition-id)]
       (cond-> db
        ; Ha az új high érték kisebb, mint a low értéke, akkor a low értékét
        ; beállítja az új high értékre és a high értékét beállítja a low értékre
        ; (A két kurzor megcserélődik)
        (< n data-cursor-low)
        (-> (assoc-in [partition-id :data-cursor-low]  n)
            (assoc-in [partition-id :data-cursor-high] data-cursor-low))
        ; Ha az új high érték nagyobb vagy egyenlő, mint a low értéke, akkor
        ; beállítja az új high értéket
        (>= n data-cursor-low)
        (assoc-in [partition-id :data-cursor-high] n))))

(defn set-data-cursor-low!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (integer) n
  ;
  ; @return (map)
  [db [_ partition-id n]]
  (let [data-cursor-high (r get-data-cursor-high db partition-id)
        data-cursor-low  (r get-data-cursor-low  db partition-id)]
       (cond-> db
        ; Ha az új low érték nagyobb, mint a high értéke, akkor a high értékét
        ; beállítja az új low értékre és a low értékét beállítja a high értékre
        ; (A két kurzor megcserélődik)
        (> n data-cursor-high)
        (-> (assoc-in [partition-id :data-cursor-high] n)
            (assoc-in [partition-id :data-cursor-low]  data-cursor-high))
        ; Ha az új low érték kisebb vagy egyenlő, mint a high értéke, akkor
        ; beállítja az új low értéket
        (<= n data-cursor-high) (assoc-in [partition-id :data-cursor-low] n))))

(defn update-data-cursor-high!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; XXX#0988
  ;
  ; @param (namespaced keyword) partition-id
  ;
  ; @return (map)
  [db [_ partition-id]]
  (let [data-cursor-high (r get-data-cursor-high db partition-id)
        data-item-count  (r get-data-item-count  db partition-id)]
       (cond-> db
         ; A high érték nem lehet kisebb, mint a particióban tárolt elemek száma
         (> data-cursor-high data-item-count)
         (assoc-in [partition-id :data-cursor-high] data-item-count))))

(defn update-data-cursor-low!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; XXX#0988
  ;
  ; @param (namespaced keyword) partition-id
  ;
  ; @return (map)
  [db [_ partition-id]]
  (let [data-cursor-low (r get-data-cursor-low  db partition-id)
        data-item-count (r get-data-item-count  db partition-id)]
       (cond-> db
         ; A low érték nem lehet kisebb, mint a particióban tárolt elemek száma
         (> data-cursor-low data-item-count)
         (assoc-in [partition-id :data-cursor-low] data-item-count))))

(defn update-data-cursor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; XXX#0988
  ;  Ha a particióban tárolt elemek száma megváltozik, akkor az update-data-cursor!
  ;  függvény aktualizálja a kurzorok értékét.
  ;  Nem engedi, hogy a kurzorok értéke magasabb legyen, mint az elemek száma.
  ;
  ; @param (namespaced keyword) partition-id
  ;
  ; @return (map)
  [db [event-id partition-id]]
  (-> db (update-data-cursor-high! [event-id partition-id])
         (update-data-cursor-low!  [event-id partition-id])))

(defn reinit-data-cursor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Ha a partíció pre-range és post-range elemeit a trim-partition! függvény
  ; eltávolítja, akkor szükséges a kurzorokat újra inicializálni
  ;
  ; @param (namespaced keyword) partition-id
  ;
  ; @return (map)
  [db [_ partition-id]]
  (let [data-item-count (r get-data-item-count db partition-id)]
       (-> db (assoc-in [partition-id :data-cursor-high] data-item-count)
              (assoc-in [partition-id :data-cursor-low]  0))))

(defn step-data-cursor-high-bwd!
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/step-data-cursor-high-bwd! db ::my-partition)
  ;
  ; @return (map)
  [db [_ partition-id]]
  (let [data-cursor-high (r get-data-cursor-high db partition-id)]
       (r set-data-cursor-high! db partition-id (dec data-cursor-high))))

(defn step-data-cursor-high-fwd!
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/step-data-cursor-high-fwd! db ::my-partition)
  ;
  ; @return (map)
  [db [_ partition-id]]
  db
  (let [data-cursor-high (r get-data-cursor-high db partition-id)]
       (r set-data-cursor-high! db partition-id (inc data-cursor-high))))

(defn step-data-cursor-low-bwd!
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/step-data-cursor-low-bwd! db ::my-partition)
  ;
  ; @return (map)
  [db [_ partition-id]]
  (let [data-cursor-low (r get-data-cursor-low db partition-id)]
       (r set-data-cursor-low! db partition-id (dec data-cursor-low))))

(defn step-data-cursor-low-fwd!
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/step-data-cursor-low-fwd! db ::my-partition)
  ;
  ; @return (map)
  [db [_ partition-id]]
  (let [data-cursor-low (r get-data-cursor-low db partition-id)]
       (r set-data-cursor-low! db partition-id (inc data-cursor-low))))

(defn trim-partition!
  ; Törli a partíció pre-range és post-range elemeit
  ;
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/trim-partition! db ::my-partition)
  ;
  ; @return (map)
  [db [event-id partition-id]]
  (-> db (assoc-in [partition-id :data-items]
                   (r get-in-range-data-items db partition-id))
         (assoc-in [partition-id :data-order]
                   (r get-in-range-data-order db partition-id))
         (reinit-data-cursor! [event-id partition-id])))
