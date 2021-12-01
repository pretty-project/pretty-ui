
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.11
; Description:
; Version: v0.5.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-ui.graphics
    (:require [mid-fruits.svg :as svg]))



;; -- Graphics ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn app-logo
  ; @return (hiccup)
  []
  [:div.x-app-logo])

(defn mt-logo
  ; @return (hiccup)
  []
  [:div.x-mt-logo])

(defn app-title
  ; @param (string) title
  ;
  ; @return (hiccup)
  [title]
  [:div.x-app-title title])

(defn loading-animation-a
  ; @return (hiccup)
  []
  [:div.x-loading-animation-a "Loading"])

(defn loading-animation-b
  ; @param (map) animation-props
  ;  {:hide-logo? (boolean)(opt)
  ;   Default: false}
  ;
  ; @return (hiccup)
  [{:keys [hide-logo?]}]
  [:div.x-loading-animation-b
    (if-not hide-logo? [:div.x-mt-logo])
    (svg/svg {:elements [(svg/circle {:x 30 :y 30 :r 29
                                      :class "x-loading-animation-b--primary-circle"})
                         (svg/circle {:x 30 :y 30 :r 29
                                      :class "x-loading-animation-b--secondary-circle"})
                         (svg/circle {:x 30 :y 30 :r 29
                                      :class "x-loading-animation-b--tertiary-circle"})]
              :height 60 :width 60})])

(defn loading-animation-c
  ; @return (hiccup)
  []
  [:div.x-loading-animation-c
    [:div.x-mt-logo]
    (svg/svg {:elements [(svg/circle {:x 60 :y 60 :r 30
                                      :class "x-loading-animation-c--primary-circle"})
                         (svg/circle {:x 60 :y 60 :r 30
                                      :class "x-loading-animation-c--secondary-circle"})]
              :height 120 :width 120})])

(defn loading-animation-d
  ; @return (hiccup)
  []
  [:div.x-loading-animation-d [:i.x-loading-animation-d--icon "hourglass_empty"]])

(defn success-animation-a
  ; @return (hiccup)
  []
  [:div.x-success-animation-a [:i.x-success-animation-a--icon "done"]])

(defn failure-animation-a
  ; @return (hiccup)
  []
  [:div.x-failure-animation-a [:i.x-failure-animation-a--icon "close"]])
