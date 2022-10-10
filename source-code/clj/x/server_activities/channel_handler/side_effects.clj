
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-activities.channel-handler.side-effects
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn notifiy-client!
  ; @param (keyword) channel-id
  ; @param (map) ...
  ;  {...}
  [channel-id])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-fx :activities/notifiy-client! notifiy-client!)
