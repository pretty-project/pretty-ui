
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.selection.events
    (:require [mid-fruits.candy                      :refer [return]]
              [mid-fruits.map                        :refer [dissoc-in]]
              [mid-fruits.vector                     :as vector]
              [plugins.plugin-handler.core.subs      :as core.subs]
              [plugins.plugin-handler.items.events   :as items.events]
              [plugins.plugin-handler.selection.subs :as selection.subs]
              [x.app-core.api                        :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (let [downloaded-items (r core.subs/get-downloaded-items db plugin-id)
        item-selections  (vector/dex-range downloaded-items)]
       (assoc-in db [:plugins :plugin-handler/meta-items plugin-id :selected-items] item-selections)))

(defn select-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (integer) item-dex
  ;
  ; @return (map)
  [db [_ plugin-id item-dex]]
  (update-in db [:plugins :plugin-handler/meta-items plugin-id :selected-items] vector/conj-item-once item-dex))

(defn toggle-item-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (integer) item-dex
  ;
  ; @return (map)
  [db [_ plugin-id item-dex]]
  (update-in db [:plugins :plugin-handler/meta-items plugin-id :selected-items] vector/toggle-item item-dex))

(defn toggle-single-item-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (integer) item-dex
  ;
  ; @return (map)
  [db [_ plugin-id item-dex]]
  ; Azokban az esetekben, amikor legfeljebb egy elemet lehetséges kiválasztani a listából, ...
  (let [selected-items (get-in db [:plugins :plugin-handler/meta-items plugin-id :selected-items])]
       (if (= selected-items [item-dex])
           (dissoc-in db [:plugins :plugin-handler/meta-items plugin-id :selected-items])
           (assoc-in  db [:plugins :plugin-handler/meta-items plugin-id :selected-items] [item-dex]))))

(defn toggle-limited-item-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (integer) item-dex
  ; @param (integer) selection-limit
  ;
  ; @return (map)
  [db [_ plugin-id item-dex selection-limit]]
  ; Azokban az esetekben, amikor legfeljebb X elemet lehetséges kiválasztani a listából, ...
  (let [selected-items      (get-in db [:plugins :plugin-handler/meta-items plugin-id :selected-items])
        selected-item-count (r selection.subs/get-selected-item-count db plugin-id)]
       (if (< selected-item-count selection-limit)
           (r toggle-item-selection! db plugin-id item-dex)
           (return db))))

(defn discard-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (dissoc-in db [:plugins :plugin-handler/meta-items plugin-id :selected-items]))

(defn disable-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (let [selected-items (r core.subs/get-meta-item db plugin-id :selected-items)]
       (r items.events/disable-items! db plugin-id selected-items)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-imported-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  ; XXX#8891
  ; Az apply-imported-selection! függvény ...
  ; ... elveti az eddigi elem-kijelöléseket (r discard-selection! ...)
  ; ... az imported-selection vektorban tárolt elem-azonosítók alapján kijelöli
  ;     azokat a letöltött elemeket, amelyek azonosítói az importált kijelölésben
  ;     szerepelnek (r select-item! ...)
  (let [downloaded-items      (r core.subs/get-downloaded-items        db plugin-id)
        downloaded-item-count (r core.subs/get-downloaded-item-count   db plugin-id)
        imported-selection    (r selection.subs/get-imported-selection db plugin-id)]
       (letfn [(dex-out-of-bounds? [item-dex] (= item-dex downloaded-item-count))
               (select-item?       [item-dex] (let [{:keys [id]} (r core.subs/get-downloaded-item db plugin-id item-dex)]
                                                   (vector/contains-item? imported-selection id)))
               (f [db item-dex]
                  (cond ; If item-dex out of bounds ...
                        (dex-out-of-bounds? item-dex) (return         db)
                        ; If the current item has to be selected ...
                        (select-item?       item-dex) (f (r select-item! db plugin-id item-dex)
                                                         (inc item-dex))
                        ; If the current item has NOT to be selected ...
                        :else                         (f db (inc item-dex))))]
              ; ...
              (as-> db % (r discard-selection! db plugin-id)
                         (f % 0)))))

(defn import-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (strings in vector) selected-item-ids
  ;
  ; @return (map)
  [db [_ plugin-id selected-item-ids]]
  ; XXX#8891
  ; Az import-selected-items függvény eltárolja az ún. importált kijelölést,
  ; ami a kijelölt/letöltés után kijelölendő elemek azonosítóit tartalmazza.
  (assoc-in db [:plugins :plugin-handler/meta-items plugin-id :imported-selection] selected-item-ids))

(defn import-single-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) selected-item-id
  ;
  ; @return (map)
  [db [_ plugin-id selected-item-id]]
  ; XXX#8891
  (assoc-in db [:plugins :plugin-handler/meta-items plugin-id :imported-selection] [selected-item-id]))
