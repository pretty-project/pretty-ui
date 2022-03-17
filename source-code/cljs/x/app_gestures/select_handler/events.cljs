
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.select-handler.events
    (:require [mid-fruits.vector                        :as vector]
              [x.app-core.api                           :as a]
              [x.app-gestures.select-handler.prototypes :as select-handler.prototypes]))



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
  ;  (r gestures/init-select-handler! db :my-handler {...})
  ;
  ; @return (map)
  [db [_ handler-id handler-props]]
  (let [handler-props (select-handler.prototypes/handler-props-prototype handler-props)]
       (assoc-in db [:gestures :select-handler/data-items handler-id] handler-props)))

(defn empty-select-handler!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/empty-select-handler! :my-handler)
  ;
  ; @return (map)
  [db [_ handler-id]]
  (assoc-in db [:gestures :select-handler/data-items handler-id :selected-items] []))

(defn enable-select-handler!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/enable-select-handler! :my-handler)
  ;
  ; @return (map)
  [db [_ handler-id]]
  (assoc-in db [:gestures :select-handler/data-items handler-id :enabled?] true))

(defn disable-select-handler!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/disable-select-handler! :my-handler)
  ;
  ; @return (map)
  [db [_ handler-id]]
  (assoc-in db [:gestures :select-handler/data-items handler-id :enabled?] false))

(defn select-item!
  ; @param (keyword) handler-id
  ; @param (keyword) item-id
  ;
  ; @usage
  ;  (r gestures/select-item! :my-handler :my-item)
  ;
  ; @return (map)
  [db [_ handler-id item-id]]
  (update-in db [:gestures :select-handler/data-items handler-id :selected-items] vector/conj-item item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:gestures/init-select-handler! :my-handler {...}]
(a/reg-event-db :gestures/init-select-handler! init-select-handler!)

; @usage
;  [:gestures/empty-select-handler! :my-handler]
(a/reg-event-db :gestures/empty-select-handler! empty-select-handler!)

; @usage
;  [:gestures/enable-select-handler! :my-handler]
(a/reg-event-db :gestures/enable-select-handler! enable-select-handler!)

; @usage
;  [:gestures/disable-select-handler! :my-handler]
(a/reg-event-db :gestures/disable-select-handler! disable-select-handler!)

; @usage
;  [:gestures/select-item! :my-handler :my-item]
(a/reg-event-db :gestures/select-item! select-item!)
