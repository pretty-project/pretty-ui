
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.3.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.mouse-handler
    (:require [app-fruits.dom :as dom]
              [x.app-core.api :as a]
              [x.app-db.api   :as db]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- mousemove-listener
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-Event) event
  ;
  ; @return (function)
  [event]
  (a/dispatch-once 500 [:environment/update-mouse-position! event]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- update-mouse-position!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; XXX#3345
  ;  Az app inicializalasakor nincs meg a mouse-pos, addig a pillanatig,
  ;  amig nem mozdul meg az egermutato!
  ;
  ; @param (DOM-event) mouse-event
  ;
  ; @return (map)
  [db [_ mouse-event]]
  (assoc-in db (db/meta-item-path ::primary :mouse-position)
               (dom/get-mouse-position mouse-event)))

(a/reg-event-db :environment/update-mouse-position! update-mouse-position!)



;; Side-effect events ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn listen-to-mousemove!
  ; @usage
  ;  (environment/listen-to-mousemove!)
  []
  (dom/add-event-listener! "mousemove" mousemove-listener))

(a/reg-fx :environment/listen-to-mousemove! listen-to-mousemove!)

(defn stop-listen-to-mousemove!
  ; @usage
  ;  (environment/stop-listen-to-mousemove!)
  []
  (dom/remove-event-listener! "mousemove" mousemove-listener))

(a/reg-fx :environment/stop-listen-to-mousemove! stop-listen-to-mousemove!)

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
