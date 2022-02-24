
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.29
; Description:
; Version: v1.2.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.background.views)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
;(def FILL-COLOR "#dde2ee")
(def FILL-COLOR "#e5e9f6")



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [:div#x-app-background [:svg {:style {:width "100%" :height "100%"}
                                :preserve-aspect-ratio "none"
                                :view-box              "0 0 100 100"}
;                              [:polygon {:points "3,0 0,10 0,75 15,15 40,0"}
;                                         :style  {:fill FILL-COLOR}}]
                               [:polygon {:points "0,10 0,75 10,100 30,100 10,70 0,10 5,0 0,0"
                                          :style  {:fill FILL-COLOR}}]
                               [:polygon {:points "95,90 100,90 100,50 80,80 75,100 90,100"
                                          :style  {:fill FILL-COLOR}}]
                               [:polygon {:points "100,0 100,10 95,5 80,0"
                                          :style  {:fill FILL-COLOR}}]]])
