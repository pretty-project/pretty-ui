
(ns pretty-elements.horizontal-line.views
    (:require [pretty-elements.horizontal-line.attributes :as horizontal-line.attributes]
              [pretty-elements.horizontal-line.prototypes :as horizontal-line.prototypes]
              [pretty-presets.api                  :as pretty-presets]
              [random.api                          :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) line-id
  ; @param (map) line-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :strength (px)(opt)
  ;   Default: 1
  ;  :width (keyword)(opt)
  ;   :auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :auto}
  ;
  ; @usage
  ; [horizontal-line {...}]
  ;
  ; @usage
  ; [horizontal-line :my-horizontal-line {...}]
  ([line-props]
   [element (random/generate-keyword) line-props])

  ([line-id line-props]
   (fn [_ line-props] ; XXX#0106 (README.md#parametering)
       (let [line-props (pretty-presets/apply-preset                     line-props)
             line-props (horizontal-line.prototypes/line-props-prototype line-props)]
            [:div (horizontal-line.attributes/line-attributes line-id line-props)
                  [:div (horizontal-line.attributes/line-body-attributes line-id line-props)]]))))
