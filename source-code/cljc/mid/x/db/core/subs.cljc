
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.db.core.subs
    (:require [candy.api    :refer [return]]
              [re-frame.api :as r :refer [r]]))



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
   (-> [:x.db/get-item item-path] re-frame.core/subscribe))

  ([item-path default-value]
   (-> [:x.db/get-item item-path default-value] re-frame.core/subscribe)))

(defn subscribed-item
  ; @param (vector) item-path
  ; @param (*)(opt) default-value
  ;
  ; @usage
  ; (subscribed-item [:my-item])
  ;
  ; @return (*)
  ([item-path]
   (-> [:x.db/get-item item-path] re-frame.core/subscribe deref))

  ([item-path default-value]
   (-> [:x.db/get-item item-path default-value] re-frame.core/subscribe deref)))



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
  ; @param (list of *) params
  ;
  ; @usage
  ;  (r get-applied-item [:my-item] inc)
  ;
  ; @usage
  ;  (r get-applied-item [:my-item] + 42)
  ;
  ; @return (integer)
  [db [_ item-path f & params]]
  (let [item   (get-in db item-path)
        params (cons item params)]
       (apply f params)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.db/get-db]
(r/reg-sub :x.db/get-db get-db)

; @usage
;  [:x.db/get-item [:my-item]]
(r/reg-sub :x.db/get-item get-item)

; @usage
;  [:x.db/item-exists? [:my-item]]
(r/reg-sub :x.db/item-exists? item-exists?)

; @usage
;  [:x.db/get-item-count [:my-item]]
(r/reg-sub :x.db/get-item-count get-item-count)

; @usage
;  [:x.db/get-applied-item [:my-item] inc]
;
; @usage
;  [:x.db/get-applied-item [:my-item] + 42]
(r/reg-sub :x.db/get-applied-item get-applied-item)
