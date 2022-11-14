
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.mouse-handler.side-effects
    (:require [dom.api      :as dom]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn prevent-selecting!
  ; Letiltja a mousedown eventet, a nem kivant szovegkijelolesek
  ; megakadalyozasa vegett
  ;
  ; @usage
  ;  (prevent-selecting!)
  [_]
 ;(dom/add-event-listener! "touchstart" #(.preventDefault %)
  (dom/add-event-listener! "mousedown" dom/select-preventer))

(defn enable-selecting!
  ; @usage
  ;  (enable-selecting!)
  [_]
  (dom/remove-event-listener! "mousedown" dom/select-preventer))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.environment/prevent-selecting!]
(r/reg-fx :x.environment/prevent-selecting! prevent-selecting!)

; @usage
;  [:x.environment/enable-selecting!]
(r/reg-fx :x.environment/enable-selecting! enable-selecting!)
