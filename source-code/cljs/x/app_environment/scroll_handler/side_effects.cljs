
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
              [x.app-core.api                                 :as a]
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
  ;  (environment/set-scroll-y! 100)
  [n]
  (dom/set-scroll-y! n))

(defn scroll-to-top!
  ; @usage
  ;  (environment/scroll-to-top!)
  [_]
  (dom/set-scroll-y! 0))

(defn scroll-to-element-top!
  ; @param (string) element-id
  ; @param (integer)(opt) offset
  ;
  ; @usage
  ;  (environment/scroll-to-element-top! "my-element" 50)
  [element-id offset]
  (dom/scroll-to-element-top! (dom/get-element-by-id element-id) offset))



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
      ;(a/dispatch [:db/set-item! [:environment :sroll-handler/meta-items :scrolled-to-top?] scrolled-to-top?])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/set-scroll-y! 100]
(a/reg-fx :environment/set-scroll-y! set-scroll-y!)

; @usage
;  [:environment/scroll-to-top!]
(a/reg-fx :environment/scroll-to-top! scroll-to-top!)

; @usage
;  [:environment/scroll-to-element-top! "my-element" 50]
(a/reg-fx :environment/scroll-to-element-top! scroll-to-element-top!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :environment/listen-to-scroll! listen-to-scroll!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :environment/initialize-scroll-handler! initialize-scroll-handler!)
