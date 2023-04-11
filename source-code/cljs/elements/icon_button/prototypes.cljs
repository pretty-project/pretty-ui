
(ns elements.icon-button.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]
              [noop.api                :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :marker-color (keyword)(opt)
  ;  :progress (percent)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-position (keyword)
  ;  :hover-color (keyword)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword)
  ;  :marker-position (keyword)
  ;  :progress-color (keyword)
  ;  :progress-direction (keyword)
  ;  :progress-duration (ms)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [{:keys [badge-content border-color disabled? marker-color progress tooltip-content] :as button-props}]
  (merge {:icon-family :material-symbols-outlined
          :icon-size   :m}
         (if badge-content   {:badge-color        :primary
                              :badge-position     :tr})
         (if border-color    {:border-position    :all
                              :border-width       :xxs})
         (if marker-color    {:marker-position    :tr})
         (if progress        {:progress-color     :muted
                              :progress-direction :ltr
                              :progress-duration  250})
         (if tooltip-content {:tooltip-position   :right})
         (param button-props)
         (if disabled?       {:hover-color :none})
         (if tooltip-content {:tooltip-content (metamorphic-content/compose tooltip-content)})))
