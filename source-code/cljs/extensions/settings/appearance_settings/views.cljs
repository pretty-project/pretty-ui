
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.settings.appearance-settings.views
    (:require [x.app-elements.api :as elements]))



;; -- View components ---------------------------------------------------------
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
