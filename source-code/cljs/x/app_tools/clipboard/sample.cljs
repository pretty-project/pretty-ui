
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.clipboard.sample
    (:require [x.app-core.api  :as a]
              [x.app-tools.api :as tools]))



;; -- Szöveg másolása vágólapra mellékhatás eseménnyel ------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :copy-my-text-to-clipboard!
  {:fx [:tools/copy-to-clipboard! "My text"]})



;; -- Szöveg másolása vágólapra (felugró értesítéssel) ------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :copy-my-text-to-clipboard!
  [:tools/copy-to-clipboard! "My text"])



;; -- Szöveg másolása vágólapra függvénnyel -----------------------------------
;; ----------------------------------------------------------------------------

(defn copy-my-text-to-clipboard!
  []
  (tools/copy-to-clipboard! "My text"))
