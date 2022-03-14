
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.events
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-restart-target!
  ; @param (string) restart-target
  ;
  ; @usage
  ;  (r set-restart-target! db "/my-route?var=value")
  ;
  ; @return (map)
  [db [_ restart-target]]
  (assoc-in db [:boot-loader :restart-handler/meta-items :restart-target] restart-target))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:boot-loader/set-restart-target! "/my-route?var=value"]
(a/reg-event-db :boot-loader/set-restart-target! set-restart-target!)
