
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.29
; Description:
; Version: v1.2.2
; Compatibility: x4.4.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.background)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [:div#x-app-background
    [:svg {:style {:width "100%" :height "100%"}
           :view-box "0 0 100 100"
           :preserve-aspect-ratio "none"}
          [:polygon {:points "3,0 0,10 0,75 15,15 40,0"
                     :style {:fill "#cdd2de"}}]
          [:polygon {:points "95,92 100,90 100,50 80,80 75,100 92,100"
                     :style {:fill "#cdd2de"}}]]])
