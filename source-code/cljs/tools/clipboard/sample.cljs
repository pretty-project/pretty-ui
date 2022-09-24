
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.clipboard.sample
    (:require [clipboard.api  :as clipboard]
              [x.app-core.api :as a]))




;; -- Szöveg másolása vágólapra mellékhatás eseménnyel ------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :copy-my-text-to-clipboard!
  {:fx [:clipboard/copy-text! "My text"]})



;; -- Szöveg másolása vágólapra (felugró értesítéssel) ------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :copy-my-text-to-clipboard!
  [:clipboard/copy-text! "My text"])



;; -- Szöveg másolása vágólapra függvénnyel -----------------------------------
;; ----------------------------------------------------------------------------

(defn copy-my-text-to-clipboard!
  []
  (clipboard/copy-text! "My text"))
