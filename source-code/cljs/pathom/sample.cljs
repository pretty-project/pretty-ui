
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
              [re-frame.api :as r]))



;; -- Pathom query küldése ----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :send-my-query!
  [:pathom/send-query! :my-query
                       {:query [:my-resolver]}])



;; -- Szerver-válasz validálása -----------------------------------------------
;; ----------------------------------------------------------------------------

;
