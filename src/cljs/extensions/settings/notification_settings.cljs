
(ns extensions.settings.notification-settings
    (:require [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [:<> [elements/separator {:orientation :horizontal :size :l}]
       [elements/switch ::warning-bubbles-switch
                        {:helper     "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                         :label      :warning-bubbles
                         :layout     :fit
                         :disabled?  true
                         :default-value true}]
       [elements/separator {:orientation :horizontal :size :s}]
       [elements/switch ::notification-bubbles-switch
                        {:helper     "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                         :label      :notification-bubbles
                         :layout     :fit
                         :value-path [:a1]}]
       [elements/separator {:orientation :horizontal :size :l}]
       [elements/switch ::notification-sounds-switch
                        {:helper     "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                         :label      :notification-sounds
                         :layout     :fit
                         :value-path [:a2]}]
       [elements/separator {:orientation :horizontal :size :s}]])
