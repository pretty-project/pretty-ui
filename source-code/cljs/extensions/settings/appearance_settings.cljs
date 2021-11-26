
(ns extensions.settings.appearance-settings
    (:require [x.app-elements.api :as elements]))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [:<> [elements/radio-button ::selected-theme-radio-button
                              {:helper      "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                               :label       :selected-theme
                               :layout      :fit
                               :get-label-f :label
                               :get-value-f :value
                               :initial-options [{:label :dark-theme  :value "dark"}
                                                 {:label :light-theme :value "light"}]
                               :on-select  [:ui/set-theme!]}]
       [elements/separator {:orientation :horizontal :size :s}]])
