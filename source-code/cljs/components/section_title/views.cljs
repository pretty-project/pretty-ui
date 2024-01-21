
(ns components.section-title.views
    (:require [auto-title.api                      :as auto-title]
              [components.section-title.prototypes :as section-title.prototypes]
              [fruits.random.api                   :as random]
              [pretty-elements.api                 :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- section-title
  ; @param (keyword) title-id
  ; @param (map) title-props
  ; {:content (metamorphic-content)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [title-id {:keys [content placeholder] :as title-props}]
  ; The sensor and title must be placed in a common element.
  ; Otherwise, in some cases the sensor would be in a wrong position!
  [:div.c-section-title [auto-title/sensor {:placeholder placeholder :title content :offset -12}]
                        [pretty-elements/label title-id title-props]])

(defn component
  ; @note
  ; For more information, check out the documentation of the ['label'](/pretty-ui/cljs/pretty-elements/api.html#label) element.
  ;
  ; @param (keyword)(opt) title-id
  ; @param (map) title-props
  ; {:font-size (keyword, px or string)(opt)
  ;   Default: :5xl
  ;  :font-weight (keyword or integer)(opt)
  ;   Default: :semi-bold}
  ;
  ; @usage
  ; [section-title {...}]
  ;
  ; @usage
  ; [section-title :my-section-title {...}]
  ([title-props]
   [component (random/generate-keyword) title-props])

  ([title-id title-props]
   ; @note (tutorials#parametering)
   (fn [_ title-props]
       (let [title-props (section-title.prototypes/title-props-prototype title-props)]
            [section-title title-id title-props]))))
