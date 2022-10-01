
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.clipboard.sample
    (:require [re-frame.api        :as r]
              [tools.clipboard.api :as clipboard]))




;; -- Szöveg másolása vágólapra mellékhatás eseménnyel ------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :copy-my-text-to-clipboard!
  {:fx [:clipboard/copy-text! "My text"]})



;; -- Szöveg másolása vágólapra (felugró értesítéssel) ------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :copy-my-text-to-clipboard!
  [:clipboard/copy-text! "My text"])



;; -- Szöveg másolása vágólapra függvénnyel -----------------------------------
;; ----------------------------------------------------------------------------

(defn copy-my-text-to-clipboard!
  []
  (clipboard/copy-text! "My text"))
