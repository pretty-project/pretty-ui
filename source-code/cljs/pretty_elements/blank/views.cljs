
(ns pretty-elements.blank.views
    (:require [fruits.random.api                :as random]
              [metamorphic-content.api          :as metamorphic-content]
              [pretty-elements.blank.attributes :as blank.attributes]
              [pretty-elements.blank.prototypes :as blank.prototypes]
              [pretty-presets.api               :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- blank
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ; {:content (metamorphic-content)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [blank-id {:keys [content placeholder] :as blank-props}]
  [:div (blank.attributes/blank-attributes blank-id blank-props)
        [:div (blank.attributes/blank-body-attributes blank-id blank-props)
              [metamorphic-content/compose content placeholder]]])

(defn element
  ; @param (keyword)(opt) blank-id
  ; @param (map) blank-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [blank {...}]
  ;
  ; @usage
  ; [blank :my-blank {...}]
  ([blank-props]
   [element (random/generate-keyword) blank-props])

  ([blank-id blank-props]
   ; @note (tutorials#parametering)
   (fn [_ blank-props]
       (let [blank-props (pretty-presets/apply-preset            blank-props)
             blank-props (blank.prototypes/blank-props-prototype blank-props)]
            [blank blank-id blank-props]))))
