
(ns elements.button.prototypes
    (:require [candy.api        :refer [param]]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :border-color (keyword)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword)(opt)
  ;  :icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword)(opt)
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
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [{:keys [badge-content border-color disabled? font-size icon label marker-color tooltip-content] :as button-props}]
  (merge {:font-size        :s
          :font-weight      :medium
          :horizontal-align :center
          :line-height      :text-block}
         (if badge-content   {:badge-color :primary :badge-position :tr})
         (if border-color    {:border-position :all
                              :border-width    :xxs})
         (if marker-color    {:marker-position :tr})
         (if icon            {:icon-family   :material-symbols-outlined
                              :icon-position :left
                              :icon-size (or font-size :s)})
         (if tooltip-content {:tooltip-position :right})
         (param button-props)
         (if badge-content   {:badge-content     (x.components/content badge-content)})
         (if label           {:label             (x.components/content label)})
         (if tooltip-content {:tooltip-content (x.components/content tooltip-content)})
         (if disabled?       {:hover-color :none})))
