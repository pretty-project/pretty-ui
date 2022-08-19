

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-ui.graphics.views)



;; ----------------------------------------------------------------------------
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
