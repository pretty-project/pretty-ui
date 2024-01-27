
(ns pretty-elements.horizontal-line.views
    (:require [fruits.random.api                          :as random]
              [pretty-elements.horizontal-line.attributes :as horizontal-line.attributes]
              [pretty-elements.horizontal-line.prototypes :as horizontal-line.prototypes]
              [pretty-presets.api                         :as pretty-presets]
              [pretty-engine.api :as pretty-engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) line-id
  ; @param (map) line-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   Default: :muted
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :strength (px)(opt)
  ;   Default: 1
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)
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
   ; @note (tutorials#parametering)
   (fn [_ line-props]
       (let [line-props (pretty-presets/apply-preset                     line-props)
             line-props (horizontal-line.prototypes/line-props-prototype line-props)]
            [:div (horizontal-line.attributes/line-attributes line-id line-props)
                  [:div (horizontal-line.attributes/line-body-attributes line-id line-props)]]))))
