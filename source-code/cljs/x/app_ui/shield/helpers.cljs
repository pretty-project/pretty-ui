

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.shield.helpers
    (:require [dom.api :as dom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn shield-hidden?
  ; @usage
  ;  (ui/shield-hidden?)
  ;
  ; @return (boolean)
  []
  (let [shield-element       (dom/get-element-by-id       "x-app-shield")
        shield-display-value (dom/get-element-style-value shield-element "display")]
       (= "none" shield-display-value)))

(defn shield-visible?
  ; @usage
  ;  (ui/shield-visible?)
  ;
  ; @return (boolean)
  []
  (let [shield-element       (dom/get-element-by-id       "x-app-shield")
        shield-display-value (dom/get-element-style-value shield-element "display")]
       (not= "none" shield-display-value)))
