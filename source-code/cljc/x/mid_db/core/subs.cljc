
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.core.subs
    (:require [mid-fruits.candy :refer [return]]
              [re-frame.api     :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn subscribe-item
  ; @param (vector) item-path
  ; @param (*)(opt) default-value
  ;
  ; @usage
  ; (subscribe-item [:my-item])
  ;
  ; @return (atom)
  ([item-path]
   (-> [:db/get-item item-path] re-frame.core/subscribe))

  ([item-path default-value]
   (-> [:db/get-item item-path default-value] re-frame.core/subscribe)))

(defn subscribed-item
  ; @param (vector) item-path
  ; @param (*)(opt) default-value
  ;
  ; @usage
  ; (subscribed-item [:my-item])
  ;
  ; @return (*)
  ([item-path]
   (-> [:db/get-item item-path] re-frame.core/subscribe deref))

  ([item-path default-value]
   (-> [:db/get-item item-path default-value] re-frame.core/subscribe deref)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-db
  ; @return (map)
  [db _]
  (return db))

(defn get-item
  ; @param (vector) item-path
  ; @param (*)(opt) default-value
  ;
  ; @usage
  ;  (r get-item [:my-item])
  ;
  ; @usage
  ;  (r get-item [:my-item] "Default value")
  ;
  ; @return (*)
  [db [_ item-path default-value]]
  (get-in db item-path default-value))

(defn item-exists?
  ; @param (vector) item-path
  ;
  ; @usage
  ;  (r item-exists? [:my-item])
  ;
  ; @return (boolean)
  [db [_ item-path]]
  (some? (r get-item db item-path)))

(defn get-item-count
  ; @param (vector) item-path
  ;
  ; @usage
  ;  (r get-item-count [:my-item])
  ;
  ; @return (integer)
  [db [_ item-path]]
  (let [item (get-in db item-path)]
       (count item)))

(defn get-applied-item
  ; @param (vector) item-path
  ; @param (function) f
  ;
  ; @usage
  ;  (r get-applied-item [:my-item] #(< 5 (count %)))
  ;
  ; @return (integer)
  [db [_ item-path f]]
  (let [item (get-in db item-path)]
       (f item)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:db/get-db]
(r/reg-sub :db/get-db get-db)

; @usage
;  [:db/get-item [:my-item]]
(r/reg-sub :db/get-item get-item)

; @usage
;  [:db/item-exists? [:my-item]]
(r/reg-sub :db/item-exists? item-exists?)
