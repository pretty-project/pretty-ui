
(ns pretty-elements.vertical-line.views
    (:require [pretty-elements.vertical-line.attributes :as vertical-line.attributes]
              [pretty-elements.vertical-line.prototypes :as vertical-line.prototypes]
              [pretty-presets.api                :as pretty-presets]
              [random.api                        :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) line-id
  ; @param (map) line-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :height (keyword)(opt)
  ;   :auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :parent
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :strength (px)(opt)
  ;   Default: 1}
  ;
  ; @usage
  ; [vertical-line {...}]
  ;
  ; @usage
  ; [vertical-line :my-vertical-line {...}]
  ([line-props]
   [element (random/generate-keyword) line-props])

  ([line-id line-props]
   (fn [_ line-props] ; XXX#0106 (README.md#parametering)
       (let [line-props (pretty-presets/apply-preset                   line-props)
             line-props (vertical-line.prototypes/line-props-prototype line-props)]
            [:div (vertical-line.attributes/line-attributes line-id line-props)
                  [:div (vertical-line.attributes/line-body-attributes line-id line-props)]]))))
