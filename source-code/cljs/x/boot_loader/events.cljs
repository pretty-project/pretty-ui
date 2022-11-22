
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.events
    (:require [re-frame.api :as r]))



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
  (assoc-in db [:x.boot-loader :restart-handler/meta-items :restart-target] restart-target))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.boot-loader/set-restart-target! "/my-route?var=value"]
(r/reg-event-db :x.boot-loader/set-restart-target! set-restart-target!)
