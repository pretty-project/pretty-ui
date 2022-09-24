
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.items.subs
    (:require [mid-fruits.candy                     :refer [return]]
              [mid-fruits.loop                      :refer [some-indexed]]
              [mid-fruits.map                       :as map]
              [mid-fruits.vector                    :as vector]
              [plugins.plugin-handler.core.subs     :as core.subs]
              [plugins.plugin-handler.transfer.subs :as transfer.subs]
              [x.app-core.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ plugin-id item-id]]
  (letfn [(f [{:keys [id] :as item}] (if (= id item-id) item))]
         (let [downloaded-items (r core.subs/get-downloaded-items db plugin-id)]
              (some f downloaded-items))))

(defn export-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ plugin-id item-id]]
  (let [item-namespace (r transfer.subs/get-transfer-item db plugin-id :item-namespace)
        item           (r get-item                        db plugin-id item-id)]
       (map/add-namespace item item-namespace)))

(defn item-downloaded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (boolean)
  [db [_ plugin-id item-id]]
  (let [item (r get-item db plugin-id item-id)]
       (boolean item)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (integer)
  [db [_ plugin-id item-id]]
  ; A get-item-dex függvény visszatérési értéke az item-id paraméterként átadott azonosítójú elem indexe.
  (letfn [(f [item-dex {:keys [id]}] (if (= id item-id) item-dex))]
         (let [downloaded-items (r core.subs/get-downloaded-items db plugin-id)]
              (some-indexed f downloaded-items))))

(defn get-item-dexes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-ids
  ;
  ; @return (integers in vector)
  [db [_ plugin-id item-ids]]
  ; A get-item-dexes függvény visszatérési értéke az item-ids paraméterként átadott vektorban felsorolt
  ; azonosítójú elemek indexei egy vektorban felsorolva.
  (let [downloaded-items (r core.subs/get-downloaded-items db plugin-id)]
       (letfn [(f [item-dexes item-ids dex]
                  (let [item-id (get-in downloaded-items [dex :id])]
                       (cond ; Ha a vizsgált index magasabb, mint az utolsó listaelem indexe, ...
                             (= dex (count downloaded-items))
                             ; ... akkor a vizsgálat a lista végéhez ért.
                             (return item-dexes)
                             ; Ha a vizsgált indexű elem azonosítója szerepel az item-ids vektorban, ...
                             (vector/contains-item? item-ids item-id)
                             ; ... akkor az item-dexes vektorhoz adja a vizsgált indexet,
                             ;     eltávolítja az elem azonosítóját az item-ids vektorból és folytaja a keresést.
                             (f (conj item-dexes dex)
                                (vector/remove-item item-ids item-id)
                                (inc dex))
                             ; Ha a vizsgált indexű elem azonosítója NEM szerepel az item-ids vektorban, ...
                             ; ... akkor folytaja a keresést.
                             :else (f item-dexes item-ids (inc dex)))))]
              (f [] item-ids 0))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (integer) item-dex
  ;
  ; @return (boolean)
  [db [_ plugin-id item-dex]]
  (let [disabled-items (r core.subs/get-meta-item db plugin-id :disabled-items)]
       (vector/contains-item? disabled-items item-dex)))

(defn item-last?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (integer) item-dex
  ;
  ; @return (boolean)
  [db [_ plugin-id item-dex]]
  (let [downloaded-item-count (r core.subs/get-downloaded-item-count db plugin-id)]
       (= downloaded-item-count (inc item-dex))))
