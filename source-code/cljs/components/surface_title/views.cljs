
(ns components.surface-title.views
    (:require [components.surface-title.prototypes :as surface-title.prototypes]
              [elements.api                        :as elements]
              [random.api                          :as random]
              [x.ui.api                            :as x.ui]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-title
  ; @param (keyword) title-id
  ; @param (map) title-props
  ; {:content (metamorphic-content)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [title-id {:keys [content placeholder] :as title-props}]
  ; The sensor and title has to be placed in a common element, otherwise in some
  ; cases the sensor might be in a wrong position!
  [:div.c-surface-title [x.ui/app-title-sensor {:placeholder placeholder :title content :offset -12}]
                        [elements/label title-id title-props]])

(defn component
  ; XXX#0439 (source-code/cljs/elements/label/views.cljs)
  ; The surface-title component is based on the label element.
  ; Check out the documentation of the label element for more information.
  ;
  ; @param (keyword)(opt) title-id
  ; @param (map) title-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
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
   (let [title-props (surface-title.prototypes/title-props-prototype title-props)]
        [surface-title title-id title-props])))
