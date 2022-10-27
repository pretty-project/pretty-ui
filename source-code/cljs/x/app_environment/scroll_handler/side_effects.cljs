
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.scroll-handler.side-effects
    (:require [dom.api                                        :as dom]
              [re-frame.api                                   :as r]
              [x.app-environment.element-handler.side-effects :as element-handler.side-effects]
              [x.app-environment.scroll-handler.config        :as scroll-handler.config]
              [x.app-environment.scroll-handler.helpers       :as scroll-handler.helpers]
              [x.app-environment.scroll-handler.state         :as scroll-handler.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-scroll-y!
  ; @param (n) integer
  ;
  ; @usage
  ;  (set-scroll-y! 100)
  [n]
  (dom/set-scroll-y! n))

(defn reset-scroll-y!
  ; @usage
  ;  (reset-scroll-y!)
  [_]
  (dom/set-scroll-y! 0))

(defn scroll-to-element-top!
  ; @param (string) element-id
  ; @param (integer)(opt) offset
  ;
  ; @usage
  ;  (scroll-to-element-top! "my-element" 50)
  [element-id offset]
  (dom/scroll-to-element-top! (dom/get-element-by-id element-id) (or offset 0)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn listen-to-scroll!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (dom/add-event-listener! "scroll" scroll-handler.helpers/scroll-listener))

(defn initialize-scroll-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [scrolled-to-top? (<= (dom/get-scroll-y) scroll-handler.config/SCROLLED-TO-TOP-THRESHOLD)]
       (reset! scroll-handler.state/SCROLLED-TO-TOP? scrolled-to-top?)
       (element-handler.side-effects/set-element-attribute! "x-body-container" "data-scrolled-to-top" scrolled-to-top?)))
      ;(r/dispatch [:db/set-item! [:environment :sroll-handler/meta-items :scrolled-to-top?] scrolled-to-top?])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/set-scroll-y! 100]
(r/reg-fx :environment/set-scroll-y! set-scroll-y!)

; @usage
;  [:environment/reset-scroll-y!]
(r/reg-fx :environment/reset-scroll-y! reset-scroll-y!)

; @usage
;  [:environment/scroll-to-element-top! "my-element" 50]
(r/reg-fx :environment/scroll-to-element-top! scroll-to-element-top!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :environment/listen-to-scroll! listen-to-scroll!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :environment/initialize-scroll-handler! initialize-scroll-handler!)
