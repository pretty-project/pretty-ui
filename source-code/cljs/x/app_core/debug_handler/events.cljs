
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.debug-handler.events
    (:require [re-frame.api :as r]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-debug-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) debug-mode
  ;
  ; @return (map)
  [db [_ debug-mode]]
  (assoc-in db [:core :debug-handler/meta-items :debug-mode] debug-mode))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :core/set-debug-mode! set-debug-mode!)
