
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.query-handler.sample
    (:require [x.app-core.api :as a]))



;; -- Pathom query küldése ----------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :send-my-query!
  [:sync/send-query! :my-query
                     {}])
