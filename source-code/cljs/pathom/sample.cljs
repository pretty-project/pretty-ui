
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.sample
    (:require [pathom.api]
              [x.app-core.api :as a]))



;; -- Pathom query küldése ----------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :send-my-query!
  [:pathom/send-query! :my-query
                       {:query [:my-resolver]}])
