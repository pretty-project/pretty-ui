
(ns components.surface-title.views
    (:require [components.surface-title.helpers    :as surface-title.helpers]
              [components.surface-title.prototypes :as surface-title.prototypes]
              [elements.api                        :as elements]
              [random.api                          :as random]
              [re-frame.api                        :as r]
              [x.ui.api                            :as x.ui]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-title-body
  ; @param (keyword) title-id
  ; @param (map) title-props
  ; {:placeholder (metamorphic-content)(opt)
  ;  :title (metamorphic-content)(opt)}
  [title-id {:keys [content placeholder] :as title-props}]
  [:div.c-surface-title--body (surface-title.helpers/title-body-attributes title-id title-props)
                              (let [viewport-large? @(r/subscribe [:x.environment/viewport-large?])]
                                   [elements/label title-id
                                                   {:content     content
                                                    :font-size   (if viewport-large? :xxl :l)
                                                    :font-weight :extra-bold
                                                    :line-height :block
                                                    :placeholder placeholder}])])

(defn- surface-title
  ; @param (keyword) title-id
  ; @param (map) title-props
  ; {:placeholder (metamorphic-content)(opt)
  ;  :title (metamorphic-content)(opt)}
  [title-id {:keys [content placeholder] :as title-props}]
  ; Ha nem egy közös elemben (pl. div) volt a sensor és a title, akkor bizonoyos
  ; esetekben (pl. horizontal-polarity elemben) nem megfelelő helyen érzékelt a sensor.
  [:div.c-surface-title (surface-title.helpers/title-attributes title-id title-props)
                        [x.ui/app-title-sensor {:placeholder placeholder :title content :offset -12}]
                        [surface-title-body title-id title-props]])

(defn component
  ; @param (keyword)(opt) title-id
  ; @param (map) title-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :indent (map)(opt)
  ;  :outdent (map)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [surface-title {...}]
  ;
  ; @usage
  ; [surface-title :my-surface-title {...}]
  ([title-props]
   [component (random/generate-keyword) title-props])

  ([title-id title-props]
   (let [] ; title-props (surface-title.prototypes/title-props-prototype title-props)
        [surface-title title-id title-props])))
