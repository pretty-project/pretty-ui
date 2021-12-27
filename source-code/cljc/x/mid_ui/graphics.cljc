
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.11
; Description:
; Version: v0.6.0
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-ui.graphics)



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

(defn loading-animation
  ; @return (hiccup)
  []
  [:div.x-loading-animation "Loading"])
