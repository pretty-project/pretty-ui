
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.view-handler.events
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-view-handler!
  ; @param (keyword) handler-id
  ; @param (map) handler-props
  ;  {:default-view-id (keyword)
  ;   :reinit? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  (r gestures/init-view-handler! db :my-view-handler {:default-view-id :my-view})
  ;
  ; @return (map)
  [db [_ handler-id {:keys [default-view-id reinit?]}]]
  (if reinit? (assoc-in db [:gestures :view-handler/data-items handler-id :view-id] default-view-id)
              (assoc-in db [:gestures :view-handler/data-items handler-id :view-id]
                           (or (get-in db [:gestures :view-handler/data-items handler-id :view-id])
                               default-view-id))))

(defn change-view!
  ; @param (keyword) handler-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  (r gestures/change-view! db :my-view-handler :my-view)
  ;
  ; @return (map)
  [db [_ handler-id view-id]]
  (assoc-in db [:gestures :view-handler/data-items handler-id :view-id] view-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:gestures/init-view-handler! :my-view-handler {...}]
(a/reg-event-db :gestures/init-view-handler! init-view-handler!)

; @usage
;  [:gestures/change-view! :my-view-handler :my-view]
(a/reg-event-db :gestures/change-view! change-view!)
