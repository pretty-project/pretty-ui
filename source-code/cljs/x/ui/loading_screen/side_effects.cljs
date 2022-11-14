
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.loading-screen.side-effects
    (:require [re-frame.api      :as r]
              [x.environment.api :as x.environment]
              [x.ui.renderer     :as renderer]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hide-loading-screen!
  ; @usage
  ;  (hide-loading-screen!)
  []
  (x.environment/hide-element-animated! renderer/HIDE-ANIMATION-TIMEOUT "x-loading-screen"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.ui/hide-loading-screen!]
(r/reg-fx :x.ui/hide-loading-screen! hide-loading-screen!)
