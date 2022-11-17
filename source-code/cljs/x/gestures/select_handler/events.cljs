
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.gestures.select-handler.events 
    (:require [re-frame.api                         :as r]
              [vector.api                           :as vector]
              [x.gestures.select-handler.prototypes :as select-handler.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-select-handler!
  ; @param (keyword) handler-id
  ; @param (map) handler-props
  ;  {:enabled? (boolean)(opt)
  ;    Default: true
  ;   :max-selected-count (integer)(opt)
  ;    Default: 256
  ;   :selected-items (vector)(opt)
  ;    Default: []}
  ;
  ; @usage
  ;  (r init-select-handler! db :my-handler {...})
  ;
  ; @return (map)
  [db [_ handler-id handler-props]]
  (let [handler-props (select-handler.prototypes/handler-props-prototype handler-props)]
       (assoc-in db [:x.gestures :select-handler/data-items handler-id] handler-props)))

(defn empty-select-handler!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r empty-select-handler! :my-handler)
  ;
  ; @return (map)
  [db [_ handler-id]]
  (assoc-in db [:x.gestures :select-handler/data-items handler-id :selected-items] []))

(defn enable-select-handler!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r enable-select-handler! :my-handler)
  ;
  ; @return (map)
  [db [_ handler-id]]
  (assoc-in db [:x.gestures :select-handler/data-items handler-id :enabled?] true))

(defn disable-select-handler!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r disable-select-handler! :my-handler)
  ;
  ; @return (map)
  [db [_ handler-id]]
  (assoc-in db [:x.gestures :select-handler/data-items handler-id :enabled?] false))

(defn select-item!
  ; @param (keyword) handler-id
  ; @param (keyword) item-id
  ;
  ; @usage
  ;  (r select-item! :my-handler :my-item)
  ;
  ; @return (map)
  [db [_ handler-id item-id]]
  (update-in db [:x.gestures :select-handler/data-items handler-id :selected-items] vector/conj-item item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.gestures/init-select-handler! :my-handler {...}]
(r/reg-event-db :x.gestures/init-select-handler! init-select-handler!)

; @usage
;  [:x.gestures/empty-select-handler! :my-handler]
(r/reg-event-db :x.gestures/empty-select-handler! empty-select-handler!)

; @usage
;  [:x.gestures/enable-select-handler! :my-handler]
(r/reg-event-db :x.gestures/enable-select-handler! enable-select-handler!)

; @usage
;  [:x.gestures/disable-select-handler! :my-handler]
(r/reg-event-db :x.gestures/disable-select-handler! disable-select-handler!)

; @usage
;  [:x.gestures/select-item! :my-handler :my-item]
(r/reg-event-db :x.gestures/select-item! select-item!)
