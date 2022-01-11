
; WARNING#9904
; Az egérmutató pozícióját és más egérmutatóval kapcsolatos gyorsan változó
; adatot nem célszerű a Re-Frame adatbásisban tárolni, mivel az nem alkalmas
; a gyors egymás-utáni írások hatékony kezelésére, ugyanis minden Re-Frame
; adatbázis-írás következménye az összes aktív feliratkozás (subscription)
; újbóli kiértékelődése.



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.3.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.mouse-handler
    (:require [app-fruits.dom :as dom]
              [x.app-core.api :as a]))



;; Side-effect events ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- prevent-selecting!
  ; Letiltja a mousedown eventet, a nem kivant szovegkijelolesek
  ; megakadalyozasa vegett
  ;
  ; @usage
  ;  (environment/prevent-selecting!)
  []
  (dom/add-event-listener! "mousedown" dom/select-preventer))
  ; + (add-event-listener! "touchstart" #(.preventDefault %)) ?

(a/reg-handled-fx :environment/prevent-selecting! prevent-selecting!)

(defn- enable-selecting!
  ; @usage
  ;  (environment/enable-selecting!)
  []
  (dom/remove-event-listener! "mousedown" dom/select-preventer))

(a/reg-handled-fx :environment/enable-selecting! enable-selecting!)
