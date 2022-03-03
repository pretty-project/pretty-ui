
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
  ; @usage
  ;  [ui/app-logo]
  []
  [:div.x-app-logo])

(defn mt-logo
  ; @usage
  ;  [ui/mt-logo]
  []
  [:div.x-mt-logo])

(defn app-title
  ; @param (string) title
  ;
  ; @usage
  ;  [ui/app-title]
  [title]
  [:div.x-app-title title])

(defn loading-animation
  ; @usage
  ;  [ui/loading-animation]
  []
  [:div.x-loading-animation "Loading"])
