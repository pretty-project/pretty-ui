
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.mouse-handler.side-effects
    (:require [dom.api        :as dom]
              [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn prevent-selecting!
  ; Letiltja a mousedown eventet, a nem kivant szovegkijelolesek
  ; megakadalyozasa vegett
  ;
  ; @usage
  ;  (environment/prevent-selecting!)
  [_]
 ;(dom/add-event-listener! "touchstart" #(.preventDefault %)
  (dom/add-event-listener! "mousedown" dom/select-preventer))

(defn enable-selecting!
  ; @usage
  ;  (environment/enable-selecting!)
  [_]
  (dom/remove-event-listener! "mousedown" dom/select-preventer))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/prevent-selecting!]
(a/reg-fx :environment/prevent-selecting! prevent-selecting!)

; @usage
;  [:environment/enable-selecting!]
(a/reg-fx :environment/enable-selecting! enable-selecting!)
