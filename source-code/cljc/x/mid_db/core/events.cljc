
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.core.events
    (:require [mid-fruits.candy  :refer [return]]
              [mid-fruits.map    :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [re-frame.api      :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-db!
  ; @return (map)
  [_ _]
  (return {}))

(defn toggle-item!
  ; @param (vector) item-path
  ;
  ; @usage
  ;  (r db/toggle-item! [:my-item])
  ;
  ; @return (map)
  [db [_ item-path]]
  (update-in db item-path not))

(defn toggle-item-value!
  ; @param (vector) item-path
  ; @param (*) item-value
  ;
  ; @usage
  ;  (r db/toggle-item-value! [:my-item] :my-value)
  ;
  ; @return (map)
  [db [_ item-path item-value]]
  (let [stored-value (get-in db item-path)]
       (if (= stored-value item-value)
           (dissoc-in db item-path)
           (assoc-in  db item-path item-value))))

(defn copy-item!
  ; @param (vector) from-item-path
  ; @param (vector) to-item-path
  ;
  ; @usage
  ;  (r db/copy-item! [:move-from] [:move-to])
  ;
  ; @return (map)
  [db [_ from-item-path to-item-path]]
  (if-let [item (get-in db from-item-path)]
          (assoc-in  db to-item-path item)
          (dissoc-in db to-item-path)))

(defn move-item!
  ; @param (vector) from-item-path
  ; @param (vector) to-item-path
  ;
  ; @usage
  ;  (r db/move-item! [:move-from] [:move-to])
  ;
  ; @return (map)
  [db [_ from-item-path to-item-path]]
  (if-let [item (get-in db from-item-path)]
          (-> db (assoc-in  to-item-path item)
                 (dissoc-in from-item-path))
          (dissoc-in db to-item-path)))

(defn set-item!
  ; @param (vector) item-path
  ; @param (*) item
  ;
  ; @usage
  ;  (r db/set-item! [:my-item] :item-value)
  ;
  ; @return (map)
  [db [_ item-path item]]
  (if item (assoc-in  db item-path item)
           (dissoc-in db item-path)))

(defn set-vector-item!
  ; Biztosítja, hogy az item-path Re-Frame adatbázis útvonalon tárolt
  ; érték egy vektorban kapjon helyet.
  ;
  ; @param (vector) item-path
  ;  Az item-path útvonal utolsó eleme integer típusú kell legyen!
  ; @param (*) item
  ;
  ; @example
  ;  (def db {})
  ;  (r db/set-vector-item! [:my-item 0] :item-value)
  ;  =>
  ;  {:my-item [:item-value]}
  ;
  ; @example
  ;  (def db {})
  ;  (r db/set-vector-item! [:my-item 2] :item-value)
  ;  =>
  ;  {:my-item [:item-value]}
  ;
  ; @example
  ;  (def db {:my-item {}})
  ;  (r db/set-vector-item! [:my-item 0] :item-value)
  ;  =>
  ;  {:my-item [:item-value]}
  ;
  ; @example
  ;  (def db {:my-item [])
  ;  (r db/set-vector-item! [:my-item 0] :item-value)
  ;  =>
  ;  {:my-item [:item-value]}
  ;
  ; @example
  ;  (def db {:my-item [:first-value :second-value])
  ;  (r db/set-vector-item! [:my-item 0] :item-value)
  ;  =>
  ;  {:my-item [:item-value :second-value]}
  ;
  ; @return (map)
  [db [_ item-path item]]
  (let [item-parent-path (vector/pop-last-item item-path)
        item-dex         (vector/last-item     item-path)
        item-parent      (get-in db item-parent-path)]
       (if (vector/nonempty? item-parent)
           (let [updated-item-parent (vector/replace-nth-item item-parent item-dex item)]
                (assoc-in db item-parent-path updated-item-parent))
           (let [updated-item-parent [item]]
                (assoc-in db item-parent-path updated-item-parent)))))

(defn remove-item!
  ; @param (vector) item-path
  ;
  ; @usage
  ;  (r db/remove-item! [:my-item])
  ;
  ; @return (map)
  [db [_ item-path]]
  (dissoc-in db item-path))

(defn remove-vector-item!
  ; @param (vector) item-path
  ;  Az item-path útvonal utolsó eleme integer típusú kell legyen!
  ;
  ; @usage
  ;  (r db/remove-vector-item! [:my-item 0])
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
  ;  (r db/remove-item-n! [[:my-item] [...]])
  ;
  ; @return (map)
  [db [_ & item-paths]]
  (letfn [(f [db item-path] (dissoc-in db item-path))]
         (reduce f db item-paths)))

(defn inc-item-n!
  ; @param (vectors in vector) item-paths
  ;
  ; @usage
  ;  (r db/inc-item-n! [[:my-item] [...]])
  ;
  ; @return (map)
  [db [_ & item-paths]]
  (letfn [(f [db item-path] (update-in db item-path inc))]
         (reduce f db item-paths)))

(defn dec-item-n!
  ; @param (vectors in vector) item-paths
  ;
  ; @usage
  ;  (r db/dec-item-n! [[:my-item] [...]])
  ;
  ; @return (map)
  [db [_ & item-paths]]
  (letfn [(f [db item-path] (update-in db item-path dec))]
         (reduce f db item-paths)))

(defn apply-item!
  ; @param (vector) item-path
  ; @param (function) f
  ; @param (list of *) params
  ;
  ; @usage
  ;  (r db/apply-item! db [:my-item] not)
  ;
  ; @usage
  ;  (r db/apply-item! db [:my-item] vector/conj-item :apple)
  ;
  ; @return (map)
  [db [_ item-path f & params]]
  (let [item   (get-in db item-path)
        params (cons item params)]
       (assoc-in db item-path (apply f params))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:db/empty-db!]
(r/reg-event-db :db/empty-db! empty-db!)

; @usage
;  [:db/toggle-item! [:my-item]]
(r/reg-event-db :db/toggle-item! toggle-item!)

; @usage
;  [:db/toggle-item-value! [:my-item] :my-value]
(r/reg-event-db :db/toggle-item-value! toggle-item-value!)

; @usage
;  [:db/move-item! [:move-from] [:move-to]]
(r/reg-event-db :db/move-item! move-item!)

; @usage
;  [:db/set-item! [:my-item] "My value"]
(r/reg-event-db :db/set-item! set-item!)

; @usage
;  [:db/set-vector-item! [:my-item :0] "My value"]
(r/reg-event-db :db/set-vector-item! set-vector-item!)

; @usage
;  [:db/remove-item! [:my-item]]
(r/reg-event-db :db/remove-item! remove-item!)

; @usage
;  [:db/remove-vector-item! [:my-item 0]]
(r/reg-event-db :db/remove-vector-item! remove-vector-item!)

; @usage
;  [:db/remove-item-n! [[:my-item ] [...]]]
(r/reg-event-db :db/remove-item-n! remove-item-n!)

; @usage
;  [:db/inc-item-n! [[:my-item] [...]]]
(r/reg-event-db :db/inc-item-n! inc-item-n!)

; @usage
;  [:db/dec-item-n! [[:my-item] [...]]]
(r/reg-event-db :db/dec-item-n! dec-item-n!)

; @usage
;  [:db/apply-item! [:my-item] merge {}]
(r/reg-event-db :db/apply-item! apply-item!)
