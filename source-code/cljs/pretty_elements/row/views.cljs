
(ns pretty-elements.row.views
    (:require [fruits.random.api              :as random]
              [metamorphic-content.api        :as metamorphic-content]
              [pretty-elements.row.attributes :as row.attributes]
              [pretty-elements.row.prototypes :as row.prototypes]
              [pretty-presets.api             :as pretty-presets]
              [pretty-engine.api :as pretty-engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:content (metamorphic-content)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [row-id {:keys [content placeholder] :as row-props}]
  [:div (row.attributes/row-attributes row-id row-props)
        [:div (row.attributes/row-body-attributes row-id row-props)
              [metamorphic-content/compose content placeholder]]])

(defn element
  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :gap (keyword, px or string)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;   Default: :left
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :overflow (keyword)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :vertical-align (keyword)(opt)
  ;   Default: :center
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [row {...}]
  ;
  ; @usage
  ; [row :my-row {...}]
  ([row-props]
   [element (random/generate-keyword) row-props])

  ([row-id row-props]
   ; @note (tutorials#parametering)
   (fn [_ row-props]
       (let [row-props (pretty-presets/apply-preset        row-props)
             row-props (row.prototypes/row-props-prototype row-props)]
            [row row-id row-props]))))
