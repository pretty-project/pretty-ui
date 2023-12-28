
(ns pretty-elements.column.views
    (:require [fruits.random.api                 :as random]
              [metamorphic-content.api           :as metamorphic-content]
              [pretty-elements.column.attributes :as column.attributes]
              [pretty-elements.column.prototypes :as column.prototypes]
              [pretty-presets.api                :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- column
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ; {:content (metamorphic-content)(opt)}
  [column-id {:keys [content] :as column-props}]
  [:div (column.attributes/column-attributes column-id column-props)
        [:div (column.attributes/column-body-attributes column-id column-props)
              [metamorphic-content/compose content]]])

(defn element
  ; @param (keyword)(opt) column-id
  ; @param (map) column-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :content-value-f (function)(opt)
  ;   Default: return
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :gap (keyword, px or string)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;   Default: :center
  ;  :indent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :max-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :max-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :outdent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :placeholder-value-f (function)(opt)
  ;   Default: return
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :vertical-align (keyword)(opt)
  ;   Default: :top
  ;  :wrap-items? (boolean)(opt)
  ;   Default: false
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [elements/column {...}]
  ;
  ; @usage
  ; [elements/column :my-column {...}]
  ([column-props]
   [element (random/generate-keyword) column-props])

  ([column-id column-props]
   ; @note (tutorials#parametering)
   (fn [_ column-props]
       (let [column-props (pretty-presets/apply-preset              column-props)
             column-props (column.prototypes/column-props-prototype column-props)]
            [column column-id column-props]))))
