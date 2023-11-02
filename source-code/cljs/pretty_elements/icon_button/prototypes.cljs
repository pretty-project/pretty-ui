
(ns pretty-elements.icon-button.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :cursor (keyword)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :marker-color (keyword)(opt)
  ;  :progress (percent)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-position (keyword)
  ;  :cursor (pointer)
  ;  :hover-color (keyword)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword)
  ;  :marker-position (keyword)
  ;  :progress-color (keyword)
  ;  :progress-direction (keyword)
  ;  :progress-duration (ms)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [{:keys [badge-content border-color cursor disabled? marker-color progress tooltip-content] :as button-props}]
  ; XXX#5603 (source-code/cljs/pretty_elements/button/prototypes.cljs)
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
         (-> button-props)
         (if disabled?       {:cursor      (or cursor :default)
                              :hover-color :none}
                             {:cursor      :pointer})
         (if tooltip-content {:tooltip-content (metamorphic-content/compose tooltip-content)})))
