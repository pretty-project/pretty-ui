

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns settings.appearance-settings.views
    (:require [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [:<> [elements/radio-button ::selected-theme-radio-button
                              {:helper       "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                               :label        :selected-theme
                               :layout       :fit
                               :get-label-f  :name
                               ;:options-path (a/app-detail-path :app-themes)
                               :on-select    [:settings/set-theme!]}]
       [elements/horizontal-separator {:size :s}]])
