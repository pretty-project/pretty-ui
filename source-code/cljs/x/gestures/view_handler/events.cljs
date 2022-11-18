
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.gestures.view-handler.events
    (:require [logical.api  :refer [nonfalse?]]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-view-handler!
  ; @param (keyword) handler-id
  ; @param (map) handler-props
  ;  {:default-view-id (keyword)
  ;   :reinit? (boolean)(opt)
  ;    Default: true}
  ;
  ; @usage
  ;  (r init-view-handler! db :my-view-handler {:default-view-id :my-view})
  ;
  ; @return (map)
  [db [_ handler-id {:keys [default-view-id reinit?]}]]
  ; A {:reinit? false} beállítás használatával, az init-view-handler! függvény megtartja,
  ; esetlegesen beállított view-id értékét és nem írja felül a {:default-view-id ...}
  ; tulajdonság értékével.
  (if (nonfalse? reinit?)
      (assoc-in db [:x.gestures :view-handler/data-items handler-id :view-id] default-view-id)
      (assoc-in db [:x.gestures :view-handler/data-items handler-id :view-id]
                   (or (get-in db [:x.gestures :view-handler/data-items handler-id :view-id])
                       default-view-id))))

(defn change-view!
  ; @param (keyword) handler-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  (r change-view! db :my-view-handler :my-view)
  ;
  ; @return (map)
  [db [_ handler-id view-id]]
  (assoc-in db [:x.gestures :view-handler/data-items handler-id :view-id] view-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.gestures/init-view-handler! :my-view-handler {...}]
(r/reg-event-db :x.gestures/init-view-handler! init-view-handler!)

; @usage
;  [:x.gestures/change-view! :my-view-handler :my-view]
(r/reg-event-db :x.gestures/change-view! change-view!)