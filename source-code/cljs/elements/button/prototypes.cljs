
(ns elements.button.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]
              [noop.api                :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :border-color (keyword)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword)(opt)
  ;  :icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword)(opt)
  ;  :progress (percent)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-content (string)
  ;  :badge-position (keyword)
  ;  :border-position (keyword)
  ;  :border-width (keyword)
  ;  :font-size (keyword)
  ;  :font-weight (keyword)
  ;  :horizontal-align (keyword)
  ;  :hover-color (keyword)
  ;  :icon-family (keyword)
  ;  :icon-position (keyword)
  ;  :icon-size (keyword)
  ;  :label (string)
  ;  :line-height (keyword)
  ;  :progress-color (keyword)
  ;  :progress-direction (keyword)
  ;  :progress-duration (ms)
  ;  :text-overflow (keyword)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)
  ;  :width (keyword)}
  [{:keys [badge-content border-color disabled? font-size icon label marker-color progress tooltip-content] :as button-props}]
  (merge {:font-size        :s
          :font-weight      :medium
          :horizontal-align :center
          :line-height      :text-block
          :text-overflow    :no-wrap
          :width            :content}
         (if badge-content   {:badge-color        :primary
                              :badge-position     :tr})
         (if border-color    {:border-position    :all
                              :border-width       :xxs})
         (if marker-color    {:marker-position    :tr})
         (if icon            {:icon-family        :material-symbols-outlined
                              :icon-position      :left
                              :icon-size          (or font-size :s)})
         (if progress        {:progress-color     :muted
                              :progress-direction :ltr
                              :progress-duration  250})
         (if tooltip-content {:tooltip-position   :right})
         (param button-props)
         (if badge-content   {:badge-content   (metamorphic-content/compose badge-content)})
         (if label           {:label           (metamorphic-content/compose label)})
         (if tooltip-content {:tooltip-content (metamorphic-content/compose tooltip-content)})
         (if disabled?       {:hover-color :none})))
