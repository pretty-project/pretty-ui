
(ns app-extensions.settings.appearance-settings
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings.appearance-settings/set-theme!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) theme-props
  ;  {:id (keyword)
  ;   :name (metamorphic-content)}
  (fn [_ [_ theme-props]]
      (let [theme-id (get theme-props :id)]
          ;[:store-the-change-on-server-side! ...]
           [:ui/change-theme! theme-id])))



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
