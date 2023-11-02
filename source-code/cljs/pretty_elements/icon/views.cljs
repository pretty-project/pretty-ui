
(ns pretty-elements.icon.views
    (:require [pretty-elements.icon.attributes :as icon.attributes]
              [pretty-elements.icon.prototypes :as icon.prototypes]
              [pretty-presets.api       :as pretty-presets]
              [random.api               :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ; {:icon (keyword)}
  [icon-id {:keys [icon] :as icon-props}]
  [:div (icon.attributes/icon-attributes icon-id icon-props)
        [:i (icon.attributes/icon-body-attributes icon-id icon-props) icon]])

(defn element
  ; @info
  ; XXX#0709
  ; Some other items based on the 'icon' element and their documentations link here.
  ;
  ; @param (keyword)(opt) icon-id
  ; @param (map) icon-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :icon (keyword)
  ;  :icon-color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :icon-family (keyword)(opt)
  ;   :material-symbols-filled, :material-symbols-outlined
  ;   Default: :material-symbols-outlined
  ;  :icon-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :m
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [icon {...}]
  ;
  ; @usage
  ; [icon :my-icon {...}]
  ([icon-props]
   [element (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   (fn [_ icon-props] ; XXX#0106 (README.md#parametering)
       (let [icon-props (pretty-presets/apply-preset          icon-props)
             icon-props (icon.prototypes/icon-props-prototype icon-props)]
            [icon icon-id icon-props]))))
