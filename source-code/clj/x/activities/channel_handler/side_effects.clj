
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.activities.channel-handler.side-effects
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn notifiy-client!
  ; @param (keyword) channel-id
  ; @param (map) ...
  ;  {...}
  ;
  ; @usage
  ;  (r notifiy-client! db :my-channel {...})
  ;
  ; @return (db)
  [channel-id])
  ; TODO



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.activities/notifiy-client! :my-channel {...}]
(r/reg-fx :x.activities/notifiy-client! notifiy-client!)
