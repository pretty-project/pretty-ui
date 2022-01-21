
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.15
; Description:
; Version: v0.8.6
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.map     :as map :refer [dissoc-in]]
              [mid-fruits.map     :as map]
              [mid-fruits.vector  :as vector]
              [x.mid-core.api     :refer [r]]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-path->cofx-path
  ; @param (item-path vector) item-path
  ;
  ; @example
  ;  (db/item-path->cofx-path [:my :item :path])
  ;  =>
  ;  [:db :my :item :path]
  ;
  ; @return (item-path vector)
  [item-path]
  (vector/cons-item item-path :db))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn subscribe-item
  ; @param (item-path vector)
  ;
  ; @usage
  ; (db/subscribe-item [:my :item :path])
  ;
  ; @return (atom)
  [item-path]
  (-> [:db/get-item item-path] re-frame.core/subscribe))

(defn subscribed-item
  ; @param (item-path vector)
  ;
  ; @usage
  ; (db/subscribed-item [:my :item :path])
  ;
  ; @return (*)
  [item-path]
  (-> [:db/get-item item-path] re-frame.core/subscribe deref))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-db
  ; @return (map)
  [db _]
  (return db))

(defn get-item
  ; @param (item-path vector) item-path
  ;
  ; @usage
  ;  (r db/get-item [:my :item :path])
  ;
  ; @return (*)
  [db [_ item-path]]
  (get-in db item-path))

(defn item-exists?
  ; @param (item-path vector) item-path
  ;
  ; @usage
  ;  (r db/item-exists? [:my :item :path])
  ;
  ; @return (boolean)
  [db [_ item-path]]
  (some? (r get-item db item-path)))

(defn get-item-count
  ; @param (item-path vector) item-path
  ;
  ; @usage
  ;  (r db/get-item-count [:my :item :path])
  ;
  ; @return (integer)
  [db [_ item-path]]
  (let [item (get-in db item-path)]
       (count item)))

(defn get-applied-item
  ; @param (item-path vector) item-path
  ; @param (function) f
  ;
  ; @usage
  ;  (r db/get-applied-item [:my :item :path] #(< 5 (count %)))
  ;
  ; @return (integer)
  [db [_ item-path f]]
  (let [item (get-in db item-path)]
       (f item)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-db!
  ; @return (map)
  [_ _]
  (return {}))

(defn copy-item!
  ; @param (item-path vector) from-item-path
  ; @param (item-path vector) to-item-path
  ;
  ; @usage
  ;  (r db/copy-item! [:move :from :path] [:move :to :path])
  ;
  ; @return (map)
  [db [_ from-item-path to-item-path]]
  (if-let [item (get-in db from-item-path)]
          (assoc-in  db to-item-path item)
          (dissoc-in db to-item-path)))

(defn move-item!
  ; @param (item-path vector) from-item-path
  ; @param (item-path vector) to-item-path
  ;
  ; @usage
  ;  (r db/move-item! [:move :from :path] [:move :to :path])
  ;
  ; @return (map)
  [db [_ from-item-path to-item-path]]
  (if-let [item (get-in db from-item-path)]
          (-> db (assoc-in  to-item-path item)
                 (dissoc-in from-item-path))
          (dissoc-in db to-item-path)))

(defn set-item!
  ; @param (item-path vector) item-path
  ; @param (*) item
  ;
  ; @usage
  ;  (r db/set-item! [:my :item :path] :item-value)
  ;
  ; @return (map)
  [db [_ item-path item]]
  (if item (assoc-in  db item-path item)
           (dissoc-in db item-path)))

(defn set-vector-item!
  ; Biztosítja, hogy az item-path Re-Frame adatbázis útvonalon tárolt
  ; érték egy vektorban kapjon helyet.
  ;
  ; @param (item-path vector) item-path
  ;  Az item-path útvonal utolsó eleme integer típusú kell legyen!
  ; @param (*) item
  ;
  ; @example
  ;  (def db {})
  ;  (r db/set-vector-item! [:my :item :path 0] :item-value)
  ;  =>
  ;  {:my {:item {:path [:item-value]}}}
  ;
  ; @example
  ;  (def db {})
  ;  (r db/set-vector-item! [:my :item :path 2] :item-value)
  ;  =>
  ;  {:my {:item {:path [:item-value]}}}
  ;
  ; @example
  ;  (def db {:my :item :path {}})
  ;  (r db/set-vector-item! [:my :item :path 0] :item-value)
  ;  =>
  ;  {:my {:item {:path [:item-value]}}}
  ;
  ; @example
  ;  (def db {:my :item :path [])
  ;  (r db/set-vector-item! [:my :item :path 0] :item-value)
  ;  =>
  ;  {:my {:item {:path [:item-value]}}}
  ;
  ; @example
  ;  (def db {:my :item :path [:first-value :second-value])
  ;  (r db/set-vector-item! [:my :item :path 0] :item-value)
  ;  =>
  ;  {:my {:item {:path [:item-value :second-value]}}}
  ;
  ; @return (map)
  [db [_ item-path item]]
  (let [item-parent-path (vector/pop-last-item item-path)
        item-dex         (vector/last-item     item-path)
        item-parent      (get-in db item-parent-path)]
       (if (vector/nonempty? item-parent)
           (let [updated-item-parent (vector/change-nth-item item-parent item-dex item)]
                (assoc-in db item-parent-path updated-item-parent))
           (let [updated-item-parent [item]]
                (assoc-in db item-parent-path updated-item-parent)))))

(defn remove-item!
  ; @param (item-path vector) item-path
  ;
  ; @usage
  ;  (r db/remove-item! [:my :item :path])
  ;
  ; @return (map)
  [db [_ item-path]]
  (dissoc-in db item-path))

(defn remove-vector-item!
  ; @param (item-path vector) item-path
  ;  Az item-path útvonal utolsó eleme integer típusú kell legyen!
  ;
  ; @usage
  ;  (r db/remove-vector-item! [:my :item :path 0])
  ;
  ; @return (map)
  [db [_ item-path]]
  (let [parent-path         (vector/pop-last-item item-path)
        item-dex            (vector/last-item     item-path)
        parent-item         (get-in db parent-path)
        updated-parent-item (vector/remove-nth-item parent-item item-dex)]
       (assoc-in db parent-path updated-parent-item)))

(defn remove-item-n!
  ; @param (vectors in vector) item-paths
  ;
  ; @usage
  ;  (r db/remove-item-n! [[:my :item :path] [...]])
  ;
  ; @return (map)
  [db [_ & item-paths]]
  (reduce #(r set-item! %1 %2 nil) db item-paths))

(defn inc-item-n!
  ; @param (vectors in vector) item-paths
  ;
  ; @usage
  ;  (r db/inc-item-n! [[:my :item :path] [...]])
  ;
  ; @return (map)
  [db [_ & item-paths]]
  (reduce #(assoc-in %1 %2 (inc (get-in db %2))) db item-paths))

(defn dec-item-n!
  ; @param (vectors in vector) item-paths
  ;
  ; @usage
  ;  (r db/dec-item-n! [[:my :item :path] [...]])
  ;
  ; @return (map)
  [db [_ & item-paths]]
  (reduce #(assoc-in %1 %2 (dec (get-in db %2))) db item-paths))

(defn apply!
  ; @param (item-path vector) item-path
  ; @param (function) f
  ; @param (list of *) params
  ;
  ; @usage
  ;  (r db/apply! db [:my :item :path] not)
  ;
  ; @usage
  ;  (r db/apply! db [:my :item :path] vector/conj-item :apple)
  ;
  ; @return (map)
  [db [_ item-path f & params]]
  (let [item   (get-in db item-path)
        params (cons item params)]
       (assoc-in db item-path (apply f params))))

(defn distribute-items!
  ; @param (map) items
  ; @param (map) item-paths
  ;
  ; @usage
  ;  (r db/distribute-items! {:apple "red" :banana "yellow" :item {:nested-item "Nested value"}}
  ;                          {:apple  [:where :to :store :apple :color]
  ;                           :banana [:where :to :store :banana :color]
  ;                           :item {:nested-item [:where :to :store :nested :value]}})
  ;
  ; @return (map)
  [db [_ items item-paths]]
  ; Ahhoz, hogy a distribute-items! függvény egymásba ágyazott (nested) útvonalak kezelését
  ; is végezze, szükséges a függvényt rekurzívan alkalmazni.
  (letfn [(distribute-items-f!
            ; @param (map) items
            ; @param (map) item-paths
            ; @param (vector) base-path
            ;  Egynél nagyobb mélységű rekurzív futás esetén a source-item-key érték milyen
            ;  útvonalhoz kerüljön hozzáillesztésre.
            ;
            ; @return (map)
            [db [_ items item-paths base-path]]
            (reduce-kv (fn [db source-item-key target-item-path]
                           ; A source-item-key kulcsot a base-path útvonalhoz illeszti,
                           ; hogy az egymásba ágyazott útvonalak kezelésekor a függvény mélységi
                           ; futása esetén az aktuális source-item-key értékéből létrejöjjön
                           ; a source-item-key item-paths térképen belüli útvonala.
                           ; Pl.: []      + :apple       = [:apple]
                           ; Pl.: [:item] + :nested-item = [:item :nested-item]
                           (let [source-item-path (vector/conj-item base-path source-item-key)]
                                      ; Ha a target-item-path értéke egy vektor, akkor a target-item-path
                                      ; útvonalra tárolja a source-item-path útvonalon talált értéket.
                                (cond (vector/nonempty? target-item-path)
                                      (assoc-in db target-item-path (get-in items source-item-path))
                                      ; Ha a target-item-path értéke egy térkép, akkor tovább
                                      ; mélyíti a rekurziót.
                                      (map/nonempty?    target-item-path)
                                      (let [base-path  (param source-item-path)
                                            item-paths (get item-paths source-item-key)]
                                           (r distribute-items! db items item-paths base-path))
                                      ; Ha a target-item-path értéke nem vektor vagy térkép, akkor
                                      ; az kezeletlen hibának minősül.
                                      :else (return db))))
                       (param db)
                       (param item-paths)))]

         ; distribute-items!
         (if (and (map/nonempty? items)
                  (map/nonempty? item-paths))
             (r distribute-items-f! db items item-paths [])
             (return db))))
