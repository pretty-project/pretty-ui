
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.loading-screen.side-effects
    (:require [re-frame.api          :as a]
              [x.app-environment.api :as environment]
              [x.app-ui.renderer     :as renderer]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hide-loading-screen!
  ; @usage
  ;  (ui/hide-loading-screen!)
  []
  (environment/hide-element-animated! renderer/HIDE-ANIMATION-TIMEOUT "x-loading-screen"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:ui/hide-loading-screen!]
(a/reg-fx :ui/hide-loading-screen! hide-loading-screen!)
