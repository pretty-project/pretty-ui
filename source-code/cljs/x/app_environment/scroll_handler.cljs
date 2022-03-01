
; WARNING#9904
; Az görgetés pozícióját és más görgetéssel kapcsolatos gyorsan változó
; adatot nem célszerű a Re-Frame adatbásisban tárolni, mivel az nem alkalmas
; a gyors egymás-utáni írások hatékony kezelésére, ugyanis minden Re-Frame
; adatbázis-írás következménye az összes aktív feliratkozás (subscription)
; újbóli kiértékelődése.



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.8.0
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.scroll-handler
    (:require [app-fruits.dom :as dom]
              [x.app-core.api :as a :refer [r]]
              [x.app-db.api   :as db]
              [x.app-environment.element-handler.side-effects :as element-handler.side-effects]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (px)
(def SCROLLED-TO-TOP-THRESHOLD 50)



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (boolean)
;  A scrolled-to-top? változásának megállapításához ...
(def SCROLLED-TO-TOP? (atom nil))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- scroll-listener
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-event) event
  [_]
  (let [scrolled-to-top? (<= (dom/get-scroll-y) SCROLLED-TO-TOP-THRESHOLD)]
       (if (not= @SCROLLED-TO-TOP? scrolled-to-top?)
           ; If scrolled-to-top? changed ...
           (do (element-handler.side-effects/set-element-attribute! "x-body-container" "data-scrolled-to-top" scrolled-to-top?)
               (reset! SCROLLED-TO-TOP? scrolled-to-top?)))))
              ;(a/dispatch-sync [:db/set-item! [:environment/sroll-data :scrolled-to-top?] scrolled-to-top?])



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scrolled-to-top?
  ; @usage
  ;  (r environment/scrolled-to-top? db)
  ;
  ; @return (boolean)
  [db _]
  (let [scrolled-to-top? (get-in db [:environment/sroll-data :scrolled-to-top?])]
       (boolean scrolled-to-top?)))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-scroll-y!
  ; @param (n) integer
  ;
  ; @usage
  ;  (environment/set-scroll-y! 100)
  [n]
  (dom/set-scroll-y! n))

; @usage
;  [:environment/set-scroll-y! 100]
(a/reg-fx :environment/set-scroll-y! set-scroll-y!)

(defn scroll-to-top!
  ; @usage
  ;  (environment/scroll-to-top!)
  [_]
  (dom/set-scroll-y! 0))

; @usage
;  [:environment/scroll-to-top!]
(a/reg-fx :environment/scroll-to-top! scroll-to-top!)

(defn- scroll-to-element-top!
  ; @param (string) element-id
  ; @param (integer)(opt) offset
  ;
  ; @usage
  ;  (environment/scroll-to-element-top! "my-element" 50)
  [element-id offset]
  (dom/scroll-to-element-top! (dom/get-element-by-id element-id) offset))

; @usage
;  [:environment/scroll-to-element-top! "my-element" 50]
(a/reg-fx :environment/scroll-to-element-top! scroll-to-element-top!)

(defn- listen-to-scroll!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (dom/add-event-listener! "scroll" scroll-listener))

(a/reg-fx :environment/listen-to-scroll! listen-to-scroll!)

(defn- initialize-scroll-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [scrolled-to-top? (<= (dom/get-scroll-y) SCROLLED-TO-TOP-THRESHOLD)]
       (reset! SCROLLED-TO-TOP? scrolled-to-top?)
       (element-handler.side-effects/set-element-attribute! "x-body-container" "data-scrolled-to-top" scrolled-to-top?)))
      ;(a/dispatch [:db/set-item! [:environment/sroll-data :scrolled-to-top?] scrolled-to-top?])

(a/reg-fx :environment/initialize-scroll-handler! initialize-scroll-handler!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-boot {:fx-n [[:environment/listen-to-scroll!]
                        [:environment/initialize-scroll-handler!]]}})
