
(ns components.section-title.views
    (:require [components.section-title.prototypes :as section-title.prototypes]
              [elements.api                        :as elements]
              [random.api                          :as random]
              [x.ui.api                            :as x.ui]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- section-title
  ; @param (keyword) title-id
  ; @param (map) title-props
  ; {:content (metamorphic-content)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [title-id {:keys [content placeholder] :as title-props}]
  ; The sensor and title has to be placed in a common element, otherwise in some
  ; cases the sensor might be in a wrong position!
  [:div.c-section-title [x.ui/app-title-sensor {:placeholder placeholder :title content :offset -12}]
                        [elements/label title-id title-props]])

(defn component
  ; XXX#0439 (source-code/cljs/elements/label/views.cljs)
  ; The section-title component is based on the label element.
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
  ; [section-title {...}]
  ;
  ; @usage
  ; [section-title :my-section-title {...}]
  ([title-props]
   [component (random/generate-keyword) title-props])

  ([title-id title-props]
   (let [title-props (section-title.prototypes/title-props-prototype title-props)]
        [section-title title-id title-props])))
