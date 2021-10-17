
; WARNING! EXPIRED! DO NOT USE!



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.21
; Description:
; Version: v0.1.6
; Compatibility:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-specials.image-preloader
    (:require [app-fruits.reagent :as reagent]
              [x.app-core.api     :as a]))
              


;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (string) uri
  ;
  ; @return (component)
  [uri]
  (reagent/lifecycles
    {:component-will-mount
     #(a/dispatch [:x.app-core.load-handler/synchronize-loading!])
     :reagent-render
     [:img {:on-load #(a/dispatch [:x.app-core.load-handler/->synchron-signal])
            :src uri
            :style {:display "none"}}]}))
