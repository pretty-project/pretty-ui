
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.view-handler.subs
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-view-id
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/get-selected-view-id db :my-view-handler)
  ;
  ; @return (keyword)
  [db [_ handler-id]]
  (get-in db [:gestures :view-handler/data-items handler-id :view-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:gestures/get-selected-view-id]
(a/reg-sub :gestures/get-selected-view-id get-selected-view-id)
