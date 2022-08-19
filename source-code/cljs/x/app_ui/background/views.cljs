
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.background.views)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div#x-app-background [:svg {:style {:width "100%" :height "100%"}
                                :preserve-aspect-ratio "none"
                                :view-box              "0 0 100 100"}
                               [:polygon {:points "0,10 0,30 5,75 0,100 10,100 10,70 0,10 5,0 0,0"
                                          :style  {:fill "var( --app-background-pattern-color )"}}]
                               [:polygon {:points "100,90 100,50 95,80 90,100 95,100"
                                          :style  {:fill "var( --app-background-pattern-color )"}}]
                               [:polygon {:points "100,0 100,10 95,5 80,0"
                                          :style  {:fill "var( --app-background-pattern-color )"}}]]])
