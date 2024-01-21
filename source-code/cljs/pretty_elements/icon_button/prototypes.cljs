
(ns pretty-elements.icon-button.prototypes
    (:require [dom.api                 :as dom]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-build-kit.api    :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :badge-color (keyword or string)(opt
  ;  :href (string)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :progress (percent)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword or string)
  ;  :badge-position (keyword)
  ;  :click-effect (keyword)
  ;  :focus-id (keyword)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword, px or string)
  ;  :marker-position (keyword)
  ;  :on-mouse-up-f (function)
  ;  :progress-color (keyword or string)
  ;  :progress-direction (keyword)
  ;  :progress-duration (ms)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [button-id {:keys [badge-content border-color href marker-color on-click-f progress tooltip-content] :as button-props}]
  (merge {:focus-id    button-id
          :icon-family :material-symbols-outlined
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
         (if href            {:click-effect :opacity})
         (if on-click-f      {:click-effect :opacity})
         (-> button-props)
         (if tooltip-content {:tooltip-content (metamorphic-content/compose tooltip-content)})
         (if href            {:on-mouse-up-f dom/blur-active-element!})
         (if on-click-f      {:on-mouse-up-f dom/blur-active-element!})))
